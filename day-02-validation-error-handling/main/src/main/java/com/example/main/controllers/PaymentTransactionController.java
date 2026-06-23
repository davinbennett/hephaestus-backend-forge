package com.example.main.controllers;

import com.example.main.dto.request.PaymentTransactionRequest;
import com.example.main.dto.response.PaymentTransactionResponse;
import com.example.main.security.RequiresRoles;
import com.example.main.security.UserRole;
import com.example.main.services.PaymentTransactionService;
import com.example.main.template.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment-transactions")
@Tag(name = "Payment Transaction Management", description = "Kumpulan API untuk mencatat dan mengelola pembayaran cicilan")
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    public PaymentTransactionController(PaymentTransactionService paymentTransactionService) {
        this.paymentTransactionService = paymentTransactionService;
    }

    @PostMapping
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF})
    @Operation(
        summary = "Mencatat transaksi pembayaran cicilan baru", 
        description = "Mengeksekusi pembayaran cicilan, memvalidasi nominal, serta mengubah status jadwal cicilan menjadi PAID jika sukses. [Akses: ADMIN, STAFF]"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pembayaran berhasil diproses"),
        @ApiResponse(responseCode = "400", description = "Nominal tidak sesuai / Cicilan sudah lunas"),
        @ApiResponse(responseCode = "404", description = "Jadwal cicilan tidak ditemukan")
    })
    public ResponseEntity<Response<PaymentTransactionResponse>> createTransaction(
            @Valid @RequestBody PaymentTransactionRequest request) {
        
        PaymentTransactionResponse data = paymentTransactionService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.created(data, "Payment transaction processed successfully"));
    }
}