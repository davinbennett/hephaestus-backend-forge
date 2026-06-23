package com.example.main.services;

import com.example.main.dto.request.LoanApplicationRequest;
import com.example.main.dto.response.LoanApplicationResponse;
import com.example.main.dto.response.RepaymentScheduleResponse;
import com.example.main.entity.CustomerEntity;
import com.example.main.entity.LoanApplicationEntity;
import com.example.main.entity.RepaymentScheduleEntity;
import com.example.main.enums.LoanStatus;
import com.example.main.exceptions.BadRequestException;
import com.example.main.exceptions.ForbiddenException;
import com.example.main.exceptions.LoanApplicationNotFoundException;
import com.example.main.exceptions.NotFoundException;
import com.example.main.repositories.CustomerRepository;
import com.example.main.repositories.LoanApplicationRepository;
import com.example.main.repositories.RepaymentScheduleRepository;
import com.example.main.security.UserRole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanApplicationService {

    private static final Logger log = LoggerFactory.getLogger(LoanApplicationService.class);
    private final LoanApplicationRepository loanApplicationRepository;
    private final CustomerRepository customerRepository;
    private final RepaymentScheduleRepository repaymentScheduleRepository;
    private final RepaymentScheduleService repaymentScheduleService;

    private static final BigDecimal MANAGER_MINIMUM_AMOUNT = new BigDecimal("10000000");

    public LoanApplicationService(LoanApplicationRepository loanApplicationRepository,
            CustomerRepository customerRepository, RepaymentScheduleRepository repaymentScheduleRepository, RepaymentScheduleService repaymentScheduleService) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.customerRepository = customerRepository;
        this.repaymentScheduleRepository = repaymentScheduleRepository;
        this.repaymentScheduleService = repaymentScheduleService;
    }

    @Transactional
    public LoanApplicationResponse createLoanApplication(LoanApplicationRequest request) {
        String correlationId = MDC.get("correlation_id");

        CustomerEntity customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> {
                    log.warn("{{\"level\":\"warn\",\"event\":\"loan_submission_failed\",\"reason\":\"Customer not found\",\"customer_id\":{},\"correlation_id\":\"{}\"}}", 
                            request.getCustomerId(), correlationId);
                    return new NotFoundException("Customer Not Found");
                });

        LoanApplicationEntity loan = new LoanApplicationEntity();
        loan.setCustomer(customer);
        loan.setLoanAmount(request.getLoanAmount());
        loan.setTenorMonth(request.getTenorMonth());
        loan.setPurpose(request.getPurpose());
        loan.setStatus(LoanStatus.SUBMITTED);

        LoanApplicationEntity savedLoan = loanApplicationRepository.save(loan);

        // 🟢 INFO: loan_application_submitted
        log.info("{{\"level\":\"info\",\"event\":\"loan_application_submitted\",\"application_id\":{},\"customer_id\":{},\"correlation_id\":\"{}\"}}", 
                savedLoan.getId(), customer.getId(), correlationId);

        return mapToLoanApplicationResponse(savedLoan);
    }

    @Transactional(readOnly = true)
    public List<LoanApplicationResponse> getAllLoanApplications(String status, Long customerId) {
        List<LoanApplicationEntity> loans;

        if (status != null && customerId != null) {
            loans = loanApplicationRepository.findLoansByCustomerId(customerId).stream()
                    .filter(l -> l.getStatus() == LoanStatus.valueOf(status.toUpperCase()))
                    .collect(Collectors.toList());
        } else if (status != null) {
            loans = loanApplicationRepository.findByStatus(LoanStatus.valueOf(status.toUpperCase()));
        } else if (customerId != null) {
            loans = loanApplicationRepository.findLoansByCustomerId(customerId);
        } else {
            loans = loanApplicationRepository.findAll();
        }

        return loans.stream().map(this::mapToLoanApplicationResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LoanApplicationResponse getLoanApplicationById(Long id) {
        String correlationId = MDC.get("correlation_id");

        LoanApplicationEntity loan = loanApplicationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("{{\"level\":\"warn\",\"event\":\"loan_fetch_failed\",\"reason\":\"Loan application not found\",\"application_id\":{},\"correlation_id\":\"{}\"}}", 
                            id, correlationId);
                    return new LoanApplicationNotFoundException("Loan application not found");
                });
        return mapToLoanApplicationResponse(loan);
    }

    @Transactional
    public LoanApplicationResponse approveLoanApplication(Long id, UserRole userRole) {
        String correlationId = MDC.get("correlation_id");

        LoanApplicationEntity loan = loanApplicationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("{{\"level\":\"warn\",\"event\":\"loan_approval_failed\",\"reason\":\"Loan application not found\",\"application_id\":{},\"correlation_id\":\"{}\"}}", 
                            id, correlationId);
                    return new LoanApplicationNotFoundException("Loan application not found");
                });

        if (userRole == UserRole.MANAGER && loan.getLoanAmount().compareTo(MANAGER_MINIMUM_AMOUNT) <= 0) {
            // 🟡 WARN: access_denied / bisnis limit rejection
            log.warn("{{\"level\":\"warn\",\"event\":\"access_denied\",\"role\":\"MANAGER\",\"endpoint\":\"approve\",\"error_code\":\"FORBIDDEN\",\"application_id\":{},\"correlation_id\":\"{}\"}}", 
                    id, correlationId);
            throw new ForbiddenException("Manager is only allowed to approve loans above 10,000,000");
        }

        loan.setStatus(LoanStatus.APPROVED);
        LoanApplicationEntity updatedLoan = loanApplicationRepository.save(loan);

        repaymentScheduleService.createRepaymentSchedules(updatedLoan);

        // 🟢 INFO: loan_application_approved
        log.info("{{\"level\":\"info\",\"event\":\"loan_application_approved\",\"application_id\":{},\"role\":\"{}\",\"correlation_id\":\"{}\"}}", 
                id, userRole.name(), correlationId);

        return mapToLoanApplicationResponse(updatedLoan);
    }

    @Transactional
    public LoanApplicationResponse rejectLoanApplication(Long id) {
        String correlationId = MDC.get("correlation_id");

        LoanApplicationEntity loan = loanApplicationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("{{\"level\":\"warn\",\"event\":\"loan_rejection_failed\",\"reason\":\"Loan application not found\",\"application_id\":{},\"correlation_id\":\"{}\"}}", 
                            id, correlationId);
                    return new LoanApplicationNotFoundException("Loan application not found");
                });

        loan.setStatus(LoanStatus.REJECTED);
        LoanApplicationEntity updatedLoan = loanApplicationRepository.save(loan);

        // 🟢 INFO: loan_application_rejected (Aksi bisnis penolakan berhasil diproses sistem)
        log.info("{{\"level\":\"info\",\"event\":\"loan_application_rejected\",\"application_id\":{},\"correlation_id\":\"{}\"}}", 
                id, correlationId);

        return mapToLoanApplicationResponse(updatedLoan);
    }

    @Transactional
    public LoanApplicationResponse cancelLoanApplication(Long id) {
        String correlationId = MDC.get("correlation_id");

        LoanApplicationEntity loan = loanApplicationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("{{\"level\":\"warn\",\"event\":\"loan_cancel_failed\",\"reason\":\"Loan application not found\",\"application_id\":{},\"correlation_id\":\"{}\"}}", 
                            id, correlationId);
                    return new LoanApplicationNotFoundException("Loan application not found");
                });

        loan.setStatus(LoanStatus.CANCELLED);
        LoanApplicationEntity updatedLoan = loanApplicationRepository.save(loan);

        log.info("{{\"level\":\"info\",\"event\":\"loan_application_cancelled\",\"application_id\":{},\"correlation_id\":\"{}\"}}", 
                id, correlationId);

        return mapToLoanApplicationResponse(updatedLoan);
    }

    @Transactional(readOnly = true)
    public List<LoanApplicationResponse> getLoansByCustomerId(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new NotFoundException("Customer not found");
        }
        return loanApplicationRepository.findLoansByCustomerId(customerId).stream()
                .map(this::mapToLoanApplicationResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LoanApplicationResponse> getLoansByStatus(String statusStr) {
        List<LoanApplicationEntity> loans;

        if (statusStr != null && !statusStr.trim().isEmpty()) {
            try {
                LoanStatus statusEnum = LoanStatus.valueOf(statusStr.trim().toUpperCase());
                loans = loanApplicationRepository.findByStatus(statusEnum);
            } catch (IllegalArgumentException e) {
                String correlationId = MDC.get("correlation_id");
                log.warn("{{\"level\":\"warn\",\"event\":\"validation_error\",\"reason\":\"Invalid status parameter\",\"value\":\"{}\",\"correlation_id\":\"{}\"}}", 
                        statusStr, correlationId);
                throw new BadRequestException("Invalid loan status value: " + statusStr);
            }
        } else {
            loans = loanApplicationRepository.findAll();
        }

        return loans.stream().map(this::mapToLoanApplicationResponse).collect(Collectors.toList());
    }

    @Transactional
    public LoanApplicationResponse updateLoanStatus(Long id, String statusStr) {
        String correlationId = MDC.get("correlation_id");

        LoanApplicationEntity loan = loanApplicationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("{{\"level\":\"warn\",\"event\":\"loan_status_update_failed\",\"reason\":\"Loan application not found\",\"application_id\":{},\"correlation_id\":\"{}\"}}", 
                            id, correlationId);
                    return new LoanApplicationNotFoundException("Loan application not found");
                });

        LoanStatus newStatus;
        try {
            newStatus = LoanStatus.valueOf(statusStr.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("{{\"level\":\"warn\",\"event\":\"validation_error\",\"reason\":\"Invalid status transition value\",\"value\":\"{}\",\"correlation_id\":\"{}\"}}", 
                    statusStr, correlationId);
            throw new BadRequestException("Invalid loan status value: " + statusStr);
        }

        if (loan.getStatus() == LoanStatus.CLOSED || loan.getStatus() == LoanStatus.REJECTED) {
            log.warn("{{\"level\":\"warn\",\"event\":\"loan_status_update_failed\",\"reason\":\"Terminal status modification attempt\",\"current_status\":\"{}\",\"application_id\":{},\"correlation_id\":\"{}\"}}", 
                    loan.getStatus(), id, correlationId);
            throw new BadRequestException("Cannot change status of a " + loan.getStatus() + " loan application");
        }

        loan.setStatus(newStatus);
        LoanApplicationEntity updatedLoan = loanApplicationRepository.save(loan);

        log.info("{{\"level\":\"info\",\"event\":\"loan_status_updated\",\"application_id\":{},\"new_status\":\"{}\",\"correlation_id\":\"{}\"}}", 
                id, newStatus.name(), correlationId);

        return mapToLoanApplicationResponse(updatedLoan);
    }

    @Transactional(readOnly = true)
    public List<RepaymentScheduleResponse> getRepaymentSchedulesByLoanId(Long loanApplicationId) {
        if (!loanApplicationRepository.existsById(loanApplicationId)) {
            throw new LoanApplicationNotFoundException("Loan application not found");
        }
        return repaymentScheduleRepository.findByLoanApplicationId(loanApplicationId).stream()
                .map(this::mapToScheduleResponse)
                .collect(Collectors.toList());
    }

    private RepaymentScheduleResponse mapToScheduleResponse(RepaymentScheduleEntity schedule) {
        RepaymentScheduleResponse response = new RepaymentScheduleResponse();
        response.setId(schedule.getId());
        response.setInstallmentNumber(schedule.getInstallmentNumber());
        response.setDueDate(schedule.getDueDate());
        response.setPrincipalAmount(schedule.getPrincipalAmount());
        response.setInterestAmount(schedule.getInterestAmount());
        response.setTotalAmount(schedule.getTotalAmount());
        response.setStatus(schedule.getStatus()); 
        return response;
    }

    private LoanApplicationResponse mapToLoanApplicationResponse(LoanApplicationEntity loan) {
        return new LoanApplicationResponse(
                loan.getId(),
                loan.getCustomer().getId(),
                loan.getLoanAmount(),
                loan.getTenorMonth(),
                loan.getPurpose(),
                loan.getStatus());
    }
}