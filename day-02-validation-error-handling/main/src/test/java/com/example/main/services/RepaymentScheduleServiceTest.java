package com.example.main.services;

import com.example.main.dto.response.RepaymentScheduleResponse;
import com.example.main.entity.LoanApplicationEntity;
import com.example.main.entity.RepaymentScheduleEntity;
import com.example.main.enums.ScheduleStatus;
import com.example.main.exceptions.NotFoundException;
import com.example.main.repositories.RepaymentScheduleRepository;
import com.example.main.repositories.PaymentTransactionRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepaymentScheduleServiceTest {

    @Mock
    private RepaymentScheduleRepository repaymentScheduleRepository;

    @Mock
    private PaymentTransactionRepository paymentTransactionRepository;

    @InjectMocks
    private RepaymentScheduleService repaymentScheduleService;

    @BeforeEach
    void setUp() {
        // inject annualInterestRate (0.12 = 12%)
        ReflectionTestUtils.setField(repaymentScheduleService, "annualInterestRate", 0.12);
    }

    // ! GENERATE REPAYMENT SCHEDULES SUCCESSFULLY (MATHEMATICS LOGIC)
    @Test
    void should_create_repayment_schedules_successfully_based_on_tenor_and_amount() {
        // given
        LoanApplicationEntity loan = new LoanApplicationEntity();
        loan.setId(100L);
        loan.setLoanAmount(new BigDecimal("12000000")); // Pinjaman 12 Juta
        loan.setTenorMonth(12); // Tenor 12 Bulan

        // Pokok Bulanan    = 12.000.000 / 12 = 1.000.000
        // Bunga Bulanan    = 12.000.000 * (0.12 / 12) = 120.000
        // Total Bulanan    = 1.000.000 + 120.000 = 1.120.000

        when(repaymentScheduleRepository.save(any(RepaymentScheduleEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        repaymentScheduleService.createRepaymentSchedules(loan);

        // then
        // Verifikasi bahwa data disimpan sebanyak jumlah bulan tenor (12 kali loop)
        verify(repaymentScheduleRepository, times(12)).save(any(RepaymentScheduleEntity.class));
    }

    // ! GET SCHEDULE BY ID SUCCESSFULLY
    @Test
    void should_get_repayment_schedule_by_id_successfully() {
        // given
        Long scheduleId = 1L;
        RepaymentScheduleEntity schedule = new RepaymentScheduleEntity();
        schedule.setId(scheduleId);
        schedule.setInstallmentNumber(1);
        schedule.setPrincipalAmount(new BigDecimal("1000000"));
        schedule.setInterestAmount(new BigDecimal("12000"));
        schedule.setTotalAmount(new BigDecimal("1012000"));
        schedule.setStatus(ScheduleStatus.UNPAID);

        when(repaymentScheduleRepository.findByIdWithLoanApplication(scheduleId))
                .thenReturn(Optional.of(schedule));

        // when
        RepaymentScheduleResponse response = repaymentScheduleService.getRepaymentScheduleById(scheduleId);

        // then
        assertNotNull(response);
        assertEquals(scheduleId, response.getId());
        assertEquals(ScheduleStatus.UNPAID, response.getStatus());
        assertEquals(1, response.getInstallmentNumber());
        verify(repaymentScheduleRepository, times(1)).findByIdWithLoanApplication(scheduleId);
    }

    // ! THROW NOT FOUND WHEN SCHEDULE ID DOES NOT EXIST
    @Test
    void should_throw_not_found_when_schedule_id_is_invalid() { 
        // given
        Long invalidId = 999L;
        when(repaymentScheduleRepository.findByIdWithLoanApplication(invalidId))
                .thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> repaymentScheduleService.getRepaymentScheduleById(invalidId));

        assertTrue(exception.getMessage().contains("Repayment schedule not found with ID: " + invalidId));
        verify(repaymentScheduleRepository, times(1)).findByIdWithLoanApplication(invalidId);
    }
}