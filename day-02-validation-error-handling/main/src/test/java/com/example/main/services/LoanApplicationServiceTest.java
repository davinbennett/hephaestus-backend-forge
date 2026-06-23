package com.example.main.services;

import com.example.main.dto.request.LoanApplicationRequest;
import com.example.main.dto.response.LoanApplicationResponse;
import com.example.main.entity.CustomerEntity;
import com.example.main.entity.LoanApplicationEntity;
import com.example.main.enums.LoanStatus;
import com.example.main.exceptions.ForbiddenException;
import com.example.main.exceptions.LoanApplicationNotFoundException;
import com.example.main.exceptions.NotFoundException;
import com.example.main.repositories.CustomerRepository;
import com.example.main.repositories.LoanApplicationRepository;
import com.example.main.repositories.RepaymentScheduleRepository;
import com.example.main.security.UserRole;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanApplicationServiceTest {

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private RepaymentScheduleRepository repaymentScheduleRepository;

    @Mock
    private RepaymentScheduleService repaymentScheduleService;

    @InjectMocks
    private LoanApplicationService loanApplicationService;

    // ! SHOULD CREATE LOAN APPLICATION SUCCESSFULLY
    @Test
    void should_create_loan_application_successfully() {
        // given
        LoanApplicationRequest request = new LoanApplicationRequest(1L, new BigDecimal("5000000"), 12, "Business Expansion");
        
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);

        LoanApplicationEntity savedLoan = new LoanApplicationEntity();
        savedLoan.setId(100L);
        savedLoan.setCustomer(customer);
        savedLoan.setLoanAmount(request.getLoanAmount());
        savedLoan.setTenorMonth(request.getTenorMonth());
        savedLoan.setPurpose(request.getPurpose());
        savedLoan.setStatus(LoanStatus.SUBMITTED);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(loanApplicationRepository.save(any(LoanApplicationEntity.class))).thenReturn(savedLoan);

        // when
        LoanApplicationResponse response = loanApplicationService.createLoanApplication(request);

        // then
        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals(LoanStatus.SUBMITTED, response.getStatus());
        verify(loanApplicationRepository, times(1)).save(any(LoanApplicationEntity.class));
    }

    // ! SHOULD THROW NOT FOUND WHEN CUSTOMER DOES NOT EXIST
    @Test
    void should_throw_not_found_when_customer_does_not_exist() {
        // given
        LoanApplicationRequest request = new LoanApplicationRequest(99L, new BigDecimal("5000000"), 12, "Renovation");
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> loanApplicationService.createLoanApplication(request));

        assertEquals("Customer Not Found", exception.getMessage());
        verify(loanApplicationRepository, never()).save(any(LoanApplicationEntity.class));
    }

    // ! SHOULD GET LOAN APPLICATION BY ID SUCCESSFULLY
    @Test
    void should_get_loan_application_by_id_successfully() {
        // given
        Long loanId = 100L;
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);

        LoanApplicationEntity loan = new LoanApplicationEntity();
        loan.setId(loanId);
        loan.setCustomer(customer);
        loan.setLoanAmount(new BigDecimal("2000000"));
        loan.setStatus(LoanStatus.SUBMITTED);

        when(loanApplicationRepository.findById(loanId)).thenReturn(Optional.of(loan));

        // when
        LoanApplicationResponse response = loanApplicationService.getLoanApplicationById(loanId);

        // then
        assertNotNull(response);
        assertEquals(loanId, response.getId());
        verify(loanApplicationRepository, times(1)).findById(loanId);
    }

    // ! SHOULD THROW NOT FOUND WHEN LOAN APPLICATION DOES NOT EXIST
    @Test
    void should_throw_not_found_when_loan_application_does_not_exist() {
        // given
        Long nonExistentLoanId = 999L;
        when(loanApplicationRepository.findById(nonExistentLoanId)).thenReturn(Optional.empty());

        // when & then
        LoanApplicationNotFoundException exception = assertThrows(LoanApplicationNotFoundException.class,
                () -> loanApplicationService.getLoanApplicationById(nonExistentLoanId));

        assertEquals("Loan application not found", exception.getMessage());
    }

    // ! SHOULD APPROVE LOAN WHEN USER IS APPROVER====================================
    @Test
    void should_approve_loan_when_user_is_approver() {
        // given
        Long loanId = 100L;
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);

        LoanApplicationEntity loan = new LoanApplicationEntity();
        loan.setId(loanId);
        loan.setCustomer(customer);
        loan.setLoanAmount(new BigDecimal("5000000"));
        loan.setStatus(LoanStatus.SUBMITTED);

        when(loanApplicationRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanApplicationRepository.save(any(LoanApplicationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        LoanApplicationResponse response = loanApplicationService.approveLoanApplication(loanId, UserRole.APPROVER);

        // then
        assertNotNull(response);
        assertEquals(LoanStatus.APPROVED, response.getStatus());
        verify(repaymentScheduleService, times(1)).createRepaymentSchedules(any(LoanApplicationEntity.class));
        verify(loanApplicationRepository, times(1)).save(loan);
    }

    // ! SHOULD REJECT LOAN
    @Test
    void should_reject_loan_successfully() {
        // given
        Long loanId = 100L;
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);

        LoanApplicationEntity loan = new LoanApplicationEntity();
        loan.setId(loanId);
        loan.setCustomer(customer);
        loan.setStatus(LoanStatus.SUBMITTED);

        when(loanApplicationRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanApplicationRepository.save(any(LoanApplicationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        LoanApplicationResponse response = loanApplicationService.rejectLoanApplication(loanId);

        // then
        assertNotNull(response);
        assertEquals(LoanStatus.REJECTED, response.getStatus());
        verify(loanApplicationRepository, times(1)).save(loan);
    }

    // ! SHOULD THROW FORBIDDEN WHEN MANAGER TRIES TO APPROVE LOAN <= 10M
    @Test
    void should_throw_forbidden_when_manager_tries_to_approve_loan_under_or_equal_to_10m() {
        // given
        Long loanId = 100L;
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);

        LoanApplicationEntity loan = new LoanApplicationEntity();
        loan.setId(loanId);
        loan.setCustomer(customer);
        loan.setLoanAmount(new BigDecimal("10000000")); // Batas kritis batas maksimum manajer ditolak (<= 10.000.000)
        loan.setStatus(LoanStatus.SUBMITTED);

        when(loanApplicationRepository.findById(loanId)).thenReturn(Optional.of(loan));

        // when & then
        ForbiddenException exception = assertThrows(ForbiddenException.class,
                () -> loanApplicationService.approveLoanApplication(loanId, UserRole.MANAGER));

        assertEquals("Manager is only allowed to approve loans above 10,000,000", exception.getMessage());
        verify(loanApplicationRepository, never()).save(any());
        verify(repaymentScheduleService, never()).createRepaymentSchedules(any());
    }

    // ! SHOULD ALLOW MANAGER TO APPROVE LOAN > 10M (HAPPY PATH FOR MANAGER)
    @Test
    void should_allow_manager_to_approve_loan_above_10m() {
        // given
        Long loanId = 100L;
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);

        LoanApplicationEntity loan = new LoanApplicationEntity();
        loan.setId(loanId);
        loan.setCustomer(customer);
        loan.setLoanAmount(new BigDecimal("15000000")); // > 10.000.000 (Valid untuk level Manager)
        loan.setStatus(LoanStatus.SUBMITTED);

        when(loanApplicationRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanApplicationRepository.save(any(LoanApplicationEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        LoanApplicationResponse response = loanApplicationService.approveLoanApplication(loanId, UserRole.MANAGER);

        // then
        assertNotNull(response);
        assertEquals(LoanStatus.APPROVED, response.getStatus());
        verify(loanApplicationRepository, times(1)).save(loan);
        verify(repaymentScheduleService, times(1)).createRepaymentSchedules(any(LoanApplicationEntity.class));
    }
}