package com.example.main.services;

import com.example.main.dto.response.PaymentTransactionResponse;
import com.example.main.dto.response.RepaymentScheduleResponse;
import com.example.main.entity.LoanApplicationEntity;
import com.example.main.entity.RepaymentScheduleEntity;
import com.example.main.enums.ScheduleStatus;
import com.example.main.exceptions.NotFoundException;
import com.example.main.repositories.RepaymentScheduleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class RepaymentScheduleService {

    private static final Logger log = LoggerFactory.getLogger(RepaymentScheduleService.class);
    private final RepaymentScheduleRepository repaymentScheduleRepository;
    private final PaymentTransactionService paymentTransactionService;

    @Value("${loan.interest.annual-rate:0.12}")
    private double annualInterestRate;

    public RepaymentScheduleService(RepaymentScheduleRepository repaymentScheduleRepository, 
                                    @Lazy PaymentTransactionService paymentTransactionService) {
        this.repaymentScheduleRepository = repaymentScheduleRepository;
        this.paymentTransactionService = paymentTransactionService;
    }

    @Transactional(readOnly = true)
    public RepaymentScheduleResponse getRepaymentScheduleById(Long id) {
        String correlationId = MDC.get("correlation_id");

        RepaymentScheduleEntity schedule = repaymentScheduleRepository.findByIdWithLoanApplication(id)
                .orElseThrow(() -> {
                    log.warn("{{\"level\":\"warn\",\"event\":\"repayment_schedule_not_found\",\"repayment_schedule_id\":{},\"correlation_id\":\"{}\"}}", 
                            id, correlationId);
                    return new NotFoundException("Repayment schedule not found with ID: " + id);
                });

        return mapToScheduleResponse(schedule);
    }

    @Transactional(readOnly = true)
    public List<PaymentTransactionResponse> getPaymentTransactionsByScheduleId(Long scheduleId) {
        return paymentTransactionService.getTransactionsByScheduleId(scheduleId);
    }

    @Transactional
    public void createRepaymentSchedules(LoanApplicationEntity loan) {
        String correlationId = MDC.get("correlation_id");
        
        int tenor = loan.getTenorMonth();
        BigDecimal loanAmount = loan.getLoanAmount();

        // principal_amount = loan_amount / tenor_month
        BigDecimal monthlyPrincipal = loanAmount.divide(BigDecimal.valueOf(tenor), 2, RoundingMode.HALF_UP);

        // monthly_interest_rate = annual_interest_rate / 12
        BigDecimal monthlyInterestRate = BigDecimal.valueOf(annualInterestRate)
                .divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);

        // interest_amount = loan_amount x monthly_interest_rate
        BigDecimal monthlyInterest = loanAmount.multiply(monthlyInterestRate).setScale(2, RoundingMode.HALF_UP);

        // total_amount = principal_amount + interest_amount
        BigDecimal monthlyTotal = monthlyPrincipal.add(monthlyInterest);

        LocalDate nextDueDate = LocalDate.now().plusMonths(1);

        for (int i = 1; i <= tenor; i++) {
            RepaymentScheduleEntity schedule = new RepaymentScheduleEntity();
            schedule.setLoanApplication(loan);
            schedule.setInstallmentNumber(i);
            schedule.setDueDate(nextDueDate);
            schedule.setPrincipalAmount(monthlyPrincipal);
            schedule.setInterestAmount(monthlyInterest);
            schedule.setTotalAmount(monthlyTotal);
            schedule.setStatus(ScheduleStatus.UNPAID);

            repaymentScheduleRepository.save(schedule);

            nextDueDate = nextDueDate.plusMonths(1);
        }

        log.info("{{\"level\":\"info\",\"event\":\"repayment_schedules_generated\",\"loan_id\":{},\"total_installments\":{},\"monthly_total\":{},\"correlation_id\":\"{}\"}}", 
                loan.getId(), tenor, monthlyTotal, correlationId);
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
}