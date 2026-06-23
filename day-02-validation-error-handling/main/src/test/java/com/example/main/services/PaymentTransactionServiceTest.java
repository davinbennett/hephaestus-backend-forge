package com.example.main.services;

import com.example.main.dto.request.PaymentTransactionRequest;
import com.example.main.dto.response.PaymentTransactionResponse;
import com.example.main.entity.PaymentTransactionEntity;
import com.example.main.entity.RepaymentScheduleEntity;
import com.example.main.enums.ScheduleStatus;
import com.example.main.exceptions.BadRequestException;
import com.example.main.exceptions.NotFoundException;
import com.example.main.repositories.PaymentTransactionRepository;
import com.example.main.repositories.RepaymentScheduleRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentTransactionServiceTest {

    @Mock
    private PaymentTransactionRepository paymentTransactionRepository;

    @Mock
    private RepaymentScheduleRepository repaymentScheduleRepository;

    @InjectMocks
    private PaymentTransactionService paymentTransactionService;

    // ! PROCESS PAYMENT SUCCESSFULLY (HAPPY PATH)
    @Test
    void should_process_payment_successfully() {
        // given
        LocalDateTime now = LocalDateTime.now();
        PaymentTransactionRequest request = new PaymentTransactionRequest();
        request.setRepaymentScheduleId(10L);
        request.setPaymentReference("REF-PAY-001");
        request.setPaidAmount(new BigDecimal("1250000"));
        request.setPaidAt(now);

        RepaymentScheduleEntity schedule = new RepaymentScheduleEntity();
        schedule.setId(10L);
        schedule.setStatus(ScheduleStatus.UNPAID); // Anggap status awalnya UNPAID
        schedule.setTotalAmount(new BigDecimal("1250000"));

        PaymentTransactionEntity savedTx = new PaymentTransactionEntity();
        savedTx.setId(1L);
        savedTx.setRepaymentSchedule(schedule);
        savedTx.setPaymentReference(request.getPaymentReference());
        savedTx.setPaidAmount(request.getPaidAmount());
        savedTx.setPaidAt(request.getPaidAt());
        savedTx.setStatus("SUCCESS");

        when(repaymentScheduleRepository.findById(10L)).thenReturn(Optional.of(schedule));
        when(paymentTransactionRepository.save(any(PaymentTransactionEntity.class))).thenReturn(savedTx);
        when(repaymentScheduleRepository.save(any(RepaymentScheduleEntity.class))).thenReturn(schedule);

        // when
        PaymentTransactionResponse response = paymentTransactionService.processPayment(request);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("SUCCESS", response.getStatus());
        assertEquals(ScheduleStatus.PAID, schedule.getStatus()); // Memastikan status entity berubah menjadi PAID

        verify(repaymentScheduleRepository, times(1)).save(schedule);
        verify(paymentTransactionRepository, times(1)).save(any(PaymentTransactionEntity.class));
    }

    // ! THROW NOT FOUND WHEN REPAYMENT SCHEDULE NOT FOUND
    @Test
    void should_throw_not_found_when_repayment_schedule_does_not_exist() {
        // given
        PaymentTransactionRequest request = new PaymentTransactionRequest();
        request.setRepaymentScheduleId(99L);

        when(repaymentScheduleRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> paymentTransactionService.processPayment(request));

        assertEquals("Repayment schedule not found", exception.getMessage());
        verify(paymentTransactionRepository, never()).save(any());
    }

    // ! THROW BAD REQUEST WHEN SCHEDULE IS ALREADY PAID
    @Test
    void should_throw_bad_request_when_schedule_is_already_paid() {
        // given
        PaymentTransactionRequest request = new PaymentTransactionRequest();
        request.setRepaymentScheduleId(10L);

        RepaymentScheduleEntity schedule = new RepaymentScheduleEntity();
        schedule.setId(10L);
        schedule.setStatus(ScheduleStatus.PAID); // Sudah lunas

        when(repaymentScheduleRepository.findById(10L)).thenReturn(Optional.of(schedule));

        // when & then
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> paymentTransactionService.processPayment(request));

        assertEquals("This repayment schedule has already been paid", exception.getMessage());
        verify(paymentTransactionRepository, never()).save(any());
    }

    // ! THROW BAD REQUEST WHEN PAID AMOUNT IS INVALID
    @Test
    void should_throw_bad_request_when_paid_amount_does_not_match() {
        // given
        PaymentTransactionRequest request = new PaymentTransactionRequest();
        request.setRepaymentScheduleId(10L);
        request.setPaidAmount(new BigDecimal("1000000")); // Bayar 1 Juta

        RepaymentScheduleEntity schedule = new RepaymentScheduleEntity();
        schedule.setId(10L);
        schedule.setStatus(ScheduleStatus.UNPAID);
        schedule.setTotalAmount(new BigDecimal("1250000")); // Tagihan 1.25 Juta

        when(repaymentScheduleRepository.findById(10L)).thenReturn(Optional.of(schedule));

        // when & then
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> paymentTransactionService.processPayment(request));

        assertTrue(exception.getMessage().contains("Invalid paid amount. Expected exactly:"));
        verify(paymentTransactionRepository, never()).save(any());
    }

    // ! GET TRANSACTIONS BY SCHEDULE ID SUCCESSFULLY
    @Test
    void should_return_transactions_by_schedule_id() {
        // given
        Long scheduleId = 10L;
        RepaymentScheduleEntity schedule = new RepaymentScheduleEntity();
        schedule.setId(scheduleId);

        PaymentTransactionEntity tx1 = new PaymentTransactionEntity();
        tx1.setId(1L);
        tx1.setRepaymentSchedule(schedule);
        tx1.setPaymentReference("REF-1");
        
        when(repaymentScheduleRepository.existsById(scheduleId)).thenReturn(true);
        when(paymentTransactionRepository.findByRepaymentScheduleId(scheduleId)).thenReturn(Arrays.asList(tx1));

        // when
        List<PaymentTransactionResponse> responses = paymentTransactionService.getTransactionsByScheduleId(scheduleId);

        // then
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("REF-1", responses.get(0).getPaymentReference());
        verify(paymentTransactionRepository, times(1)).findByRepaymentScheduleId(scheduleId);
    }

    // ! THROW NOT FOUND ON GET TRANSACTIONS IF SCHEDULE NOT EXIST
    @Test
    void should_throw_not_found_on_get_transactions_when_schedule_does_not_exist() {
        // given
        Long nonExistentScheduleId = 99L;
        when(repaymentScheduleRepository.existsById(nonExistentScheduleId)).thenReturn(false);

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> paymentTransactionService.getTransactionsByScheduleId(nonExistentScheduleId));

        assertEquals("Repayment schedule not found", exception.getMessage());
        verify(paymentTransactionRepository, never()).findByRepaymentScheduleId(anyLong());
    }
}