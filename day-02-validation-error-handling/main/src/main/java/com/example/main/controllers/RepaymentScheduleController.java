package com.example.main.controllers;

import com.example.main.dto.response.PaymentTransactionResponse;
import com.example.main.dto.response.RepaymentScheduleResponse;
import com.example.main.security.RequiresRoles;
import com.example.main.security.UserRole;
import com.example.main.services.RepaymentScheduleService;
import com.example.main.template.Response; // Impor wrapper Response terpusat

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/repayment-schedules") // KOREKSI: Path disesuaikan dengan domain entitasnya
@Tag(name = "Repayment Schedule Management", description = "Kumpulan API untuk mengelola jadwal cicilan pembayaran")
public class RepaymentScheduleController { // KOREKSI: Indentasi dan nama kelas diselaraskan

    private final RepaymentScheduleService repaymentScheduleService;

    public RepaymentScheduleController(RepaymentScheduleService repaymentScheduleService) {
        this.repaymentScheduleService = repaymentScheduleService;
    }

    @GetMapping("/{id}")
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF, UserRole.APPROVER, UserRole.MANAGER})
    @Operation(
        summary = "Mendapatkan detail jadwal cicilan berdasarkan ID", 
        description = "Mengambil satu data spesifik jadwal pembayaran cicilan beserta rincian pokok dan bunganya. [Akses: Semua Role]"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Data jadwal cicilan ditemukan"),
        @ApiResponse(responseCode = "404", description = "ID jadwal cicilan tidak ditemukan")
    })
    public ResponseEntity<Response<RepaymentScheduleResponse>> getRepaymentScheduleById(
            @Parameter(description = "ID unik dari jadwal cicilan", example = "1") 
            @PathVariable Long id) {
        
        RepaymentScheduleResponse data = repaymentScheduleService.getRepaymentScheduleById(id);
        return ResponseEntity.ok(Response.ok(data, "Repayment schedule details retrieved successfully"));
    }

    @GetMapping("/{repayment_schedule_id}/payment-transactions")
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF, UserRole.APPROVER, UserRole.MANAGER})
    @Operation(
        summary = "Mendapatkan daftar transaksi pembayaran untuk suatu jadwal cicilan", 
        description = "Mengambil semua riwayat mutasi pembayaran (berhasil/gagal) yang terkait dengan ID jadwal cicilan tertentu."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Daftar transaksi pembayaran berhasil diambil"),
        @ApiResponse(responseCode = "404", description = "ID jadwal cicilan tidak ditemukan")
    })
    public ResponseEntity<Response<List<PaymentTransactionResponse>>> getTransactionsByScheduleId(
            @Parameter(description = "ID dari jadwal cicilan", example = "1") 
            @PathVariable("repayment_schedule_id") Long repaymentScheduleId) {
        
        List<PaymentTransactionResponse> data = repaymentScheduleService.getPaymentTransactionsByScheduleId(repaymentScheduleId);
        return ResponseEntity.ok(Response.ok(data, "Payment transactions for this schedule retrieved successfully"));
    }
}