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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentTransactionService {

    private static final Logger log = LoggerFactory.getLogger(PaymentTransactionService.class);
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final RepaymentScheduleRepository repaymentScheduleRepository;

    public PaymentTransactionService(PaymentTransactionRepository paymentTransactionRepository,
            RepaymentScheduleRepository repaymentScheduleRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.repaymentScheduleRepository = repaymentScheduleRepository;
    }

    @Transactional
    public PaymentTransactionResponse processPayment(PaymentTransactionRequest request) {
        String correlationId = MDC.get("correlation_id");

        RepaymentScheduleEntity schedule = repaymentScheduleRepository.findById(request.getRepaymentScheduleId())
                .orElseThrow(() -> {
                    log.warn("{{\"level\":\"warn\",\"event\":\"payment_failed\",\"reason\":\"Repayment schedule not found\",\"repayment_schedule_id\":{},\"correlation_id\":\"{}\"}}", 
                            request.getRepaymentScheduleId(), correlationId);
                    return new NotFoundException("Repayment schedule not found");
                });

        if (ScheduleStatus.PAID == schedule.getStatus()) {
            log.warn("{{\"level\":\"warn\",\"event\":\"validation_error\",\"reason\":\"Repayment schedule has already been paid\",\"repayment_schedule_id\":{},\"correlation_id\":\"{}\"}}", 
                    schedule.getId(), correlationId);
            throw new BadRequestException("This repayment schedule has already been paid");
        }

        if (request.getPaidAmount().compareTo(schedule.getTotalAmount()) != 0) {
            log.warn("{{\"level\":\"warn\",\"event\":\"validation_error\",\"reason\":\"Invalid paid amount\",\"expected\":{},\"received\":{},\"repayment_schedule_id\":{},\"correlation_id\":\"{}\"}}", 
                    schedule.getTotalAmount(), request.getPaidAmount(), schedule.getId(), correlationId);
            throw new BadRequestException("Invalid paid amount. Expected exactly: " + schedule.getTotalAmount());
        }

        PaymentTransactionEntity transaction = new PaymentTransactionEntity();
        transaction.setRepaymentSchedule(schedule);
        transaction.setPaymentReference(request.getPaymentReference());
        transaction.setPaidAmount(request.getPaidAmount());
        transaction.setPaidAt(request.getPaidAt());
        transaction.setStatus("SUCCESS");
        PaymentTransactionEntity savedTransaction = paymentTransactionRepository.save(transaction);

        schedule.setStatus(ScheduleStatus.PAID);
        repaymentScheduleRepository.save(schedule);

        log.info("{{\"level\":\"info\",\"event\":\"payment_transaction_processed\",\"transaction_id\":{},\"repayment_schedule_id\":{},\"amount\":{},\"correlation_id\":\"{}\"}}", 
                savedTransaction.getId(), schedule.getId(), savedTransaction.getPaidAmount(), correlationId);

        return mapToResponse(savedTransaction);
    }

    @Transactional(readOnly = true)
    public List<PaymentTransactionResponse> getTransactionsByScheduleId(Long scheduleId) {
        if (!repaymentScheduleRepository.existsById(scheduleId)) {
            String correlationId = MDC.get("correlation_id");
            log.warn("{{\"level\":\"warn\",\"event\":\"payment_fetch_failed\",\"reason\":\"Repayment schedule not found\",\"repayment_schedule_id\":{},\"correlation_id\":\"{}\"}}", 
                    scheduleId, correlationId);
            throw new NotFoundException("Repayment schedule not found");
        }

        return paymentTransactionRepository.findByRepaymentScheduleId(scheduleId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PaymentTransactionResponse mapToResponse(PaymentTransactionEntity tx) {
        PaymentTransactionResponse response = new PaymentTransactionResponse();
        response.setId(tx.getId());
        response.setRepaymentScheduleId(tx.getRepaymentSchedule().getId());
        response.setPaymentReference(tx.getPaymentReference());
        response.setPaidAmount(tx.getPaidAmount());
        response.setPaidAt(tx.getPaidAt());
        response.setStatus(tx.getStatus());
        return response;
    }
}