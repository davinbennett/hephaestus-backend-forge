package com.example.main.controllers;

import com.example.main.dto.request.LoanApplicationRequest;
import com.example.main.dto.request.UpdateLoanStatusRequest;
import com.example.main.dto.response.LoanApplicationResponse;
import com.example.main.dto.response.RepaymentScheduleResponse;
import com.example.main.security.RequiresRoles;
import com.example.main.security.UserRole;
import com.example.main.services.LoanApplicationService;
import com.example.main.template.Response; // Impor wrapper Response terpusat

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-applications")
@Tag(name = "Loan Application Management", description = "Kumpulan API untuk mengelola pengajuan pinjaman")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    public LoanApplicationController(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    @PostMapping
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF})
    @Operation(
        summary = "Membuat pengajuan pinjaman baru", 
        description = "Menambahkan pengajuan pinjaman baru untuk customer tertentu. Otomatis berstatus SUBMITTED. [Akses: ADMIN, STAFF]"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Pengajuan pinjaman berhasil dibuat"),
        @ApiResponse(responseCode = "400", description = "Request tidak valid / Gagal validasi data"),
        @ApiResponse(responseCode = "404", description = "Customer tidak ditemukan")
    })
    public ResponseEntity<Response<LoanApplicationResponse>> createLoanApplication(
            @Valid @RequestBody LoanApplicationRequest request) {
        LoanApplicationResponse data = loanApplicationService.createLoanApplication(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.created(data, "Loan application submitted successfully"));
    }

    @GetMapping("/{id}")
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF, UserRole.APPROVER, UserRole.MANAGER})
    @Operation(
        summary = "Mendapatkan detail pengajuan pinjaman", 
        description = "Mengambil data detail pengajuan pinjaman berdasarkan ID unik loan. [Akses: Semua Role]"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Data pengajuan pinjaman ditemukan"),
        @ApiResponse(responseCode = "404", description = "Data pengajuan pinjaman tidak ditemukan")
    })
    public ResponseEntity<Response<LoanApplicationResponse>> getLoanApplicationById(
            @Parameter(description = "ID unik dari pengajuan pinjaman", example = "1") 
            @PathVariable Long id) {
        LoanApplicationResponse data = loanApplicationService.getLoanApplicationById(id);
        return ResponseEntity.ok(Response.ok(data, "Loan application details retrieved successfully"));
    }

    @PatchMapping("/{id}/approve")
    @RequiresRoles({UserRole.ADMIN, UserRole.APPROVER, UserRole.MANAGER})
    @Operation(
        summary = "Menyetujui (Approve) pengajuan pinjaman", 
        description = "Mengubah status loan menjadi APPROVED. Manager hanya boleh menyetujui loan dengan nominal di atas 10.000.000. [Akses: ADMIN, APPROVER, MANAGER]"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pengajuan pinjaman berhasil disetujui"),
        @ApiResponse(responseCode = "403", description = "Akses ditolak karena batasan nominal Manager atau hak akses tidak sah"),
        @ApiResponse(responseCode = "404", description = "Data pengajuan pinjaman tidak ditemukan")
    })
    public ResponseEntity<Response<LoanApplicationResponse>> approveLoanApplication(
            @Parameter(description = "ID unik dari pengajuan pinjaman", example = "1") @PathVariable Long id,
            @Parameter(hidden = true) @RequestAttribute("USER_ROLE") UserRole userRole) { 
        
        LoanApplicationResponse data = loanApplicationService.approveLoanApplication(id, userRole);
        return ResponseEntity.ok(Response.ok(data, "Loan application approved successfully"));
    }

    @PatchMapping("/{id}/reject")
    @RequiresRoles({UserRole.ADMIN, UserRole.APPROVER, UserRole.MANAGER})
    @Operation(
        summary = "Menolak (Reject) pengajuan pinjaman", 
        description = "Mengubah status loan menjadi REJECTED. [Akses: ADMIN, APPROVER, MANAGER]"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pengajuan pinjaman berhasil ditolak"),
        @ApiResponse(responseCode = "404", description = "Data pengajuan pinjaman tidak ditemukan")
    })
    public ResponseEntity<Response<LoanApplicationResponse>> rejectLoanApplication(
            @Parameter(description = "ID unik dari pengajuan pinjaman", example = "1") 
            @PathVariable Long id) {
        LoanApplicationResponse data = loanApplicationService.rejectLoanApplication(id);
        return ResponseEntity.ok(Response.ok(data, "Loan application rejected successfully"));
    }

    @PatchMapping("/{id}/cancel")
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF})
    @Operation(
        summary = "Membatalkan (Cancel) pengajuan pinjaman", 
        description = "Mengubah status loan menjadi CANCELLED sebelum dana dicairkan. [Akses: ADMIN, STAFF]"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Pengajuan pinjaman berhasil dibatalkan"),
        @ApiResponse(responseCode = "404", description = "Data pengajuan pinjaman tidak ditemukan")
    })
    public ResponseEntity<Response<LoanApplicationResponse>> cancelLoanApplication(
            @Parameter(description = "ID unik dari pengajuan pinjaman", example = "1") 
            @PathVariable Long id) {
        LoanApplicationResponse data = loanApplicationService.cancelLoanApplication(id);
        return ResponseEntity.ok(Response.ok(data, "Loan application cancelled successfully"));
    }
    
    @GetMapping
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF, UserRole.APPROVER, UserRole.MANAGER})
    @Operation(
        summary = "Mendapatkan daftar pinjaman (Filter by Status opsional)", 
        description = "Mengambil daftar pengajuan pinjaman. Bisa difilter berdasarkan status seperti SUBMITTED, APPROVED, dll."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan daftar pengajuan pinjaman"),
        @ApiResponse(responseCode = "400", description = "Status yang dikirimkan tidak valid")
    })
    public ResponseEntity<Response<List<LoanApplicationResponse>>> getLoanApplications(
            @RequestParam(name = "status", required = false) String status) {
        
        List<LoanApplicationResponse> data = loanApplicationService.getLoansByStatus(status);
        return ResponseEntity.ok(Response.ok(data, "Loan applications retrieved successfully"));
    }

    @PatchMapping("/{id}/status")
    @RequiresRoles({UserRole.ADMIN, UserRole.APPROVER, UserRole.MANAGER})
    @Operation(
        summary = "Memperbarui status loan secara dinamis (Generic Status Update)", 
        description = "Mengubah status pengajuan pinjaman berdasarkan input string di request body. Nilai status diubah menjadi Enum LoanStatus. [Akses: ADMIN, APPROVER, MANAGER]"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status loan berhasil diperbarui"),
        @ApiResponse(responseCode = "400", description = "Nilai status tidak valid atau menyalahi aturan transisi"),
        @ApiResponse(responseCode = "404", description = "Data pengajuan pinjaman tidak ditemukan")
    })
    public ResponseEntity<Response<LoanApplicationResponse>> updateLoanStatus(
            @Parameter(description = "ID unik dari pengajuan pinjaman", example = "1") @PathVariable Long id,
            @Valid @RequestBody UpdateLoanStatusRequest request) {
        
        LoanApplicationResponse data = loanApplicationService.updateLoanStatus(id, request.getStatus());
        return ResponseEntity.ok(Response.ok(data, "Loan status dynamically updated successfully"));
    }

    @GetMapping("/{loanApplicationId}/repayment-schedules")
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF, UserRole.APPROVER, UserRole.MANAGER})
    @Operation(
        summary = "Mendapatkan jadwal pembayaran berdasarkan ID Loan", 
        description = "Mengambil seluruh daftar tenor/jadwal cicilan (Repayment Schedule) dari satu pengajuan pinjaman tertentu. [Akses: Semua Role]"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan daftar jadwal pembayaran"),
        @ApiResponse(responseCode = "404", description = "Data pengajuan pinjaman tidak ditemukan")
    })
    public ResponseEntity<Response<List<RepaymentScheduleResponse>>> getRepaymentSchedulesByLoan(
            @Parameter(description = "ID unik dari pengajuan pinjaman", example = "1") 
            @PathVariable(name = "loanApplicationId") Long loanApplicationId) {
        
        List<RepaymentScheduleResponse> data = loanApplicationService.getRepaymentSchedulesByLoanId(loanApplicationId);
        return ResponseEntity.ok(Response.ok(data, "Repayment schedules for this loan retrieved successfully"));
    }
}