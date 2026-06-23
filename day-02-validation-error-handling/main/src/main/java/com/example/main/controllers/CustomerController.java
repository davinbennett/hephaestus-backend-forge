package com.example.main.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.main.dto.request.CreateCustomerRequest;
import com.example.main.dto.request.PatchCustomerRequest;
import com.example.main.dto.response.CustomerResponse;
import com.example.main.dto.response.LoanApplicationResponse;
import com.example.main.security.RequiresRoles;
import com.example.main.security.UserRole;
import com.example.main.services.CustomerService;
import com.example.main.services.LoanApplicationService;
import com.example.main.template.Response;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customer Management", description = "Kumpulan API untuk mengelola data pelanggan")
public class CustomerController {

    private final CustomerService customerService;
    private final LoanApplicationService loanApplicationService;

    public CustomerController(CustomerService customerService, LoanApplicationService loanApplicationService) {
        this.customerService = customerService;
        this.loanApplicationService = loanApplicationService;
    }

    @PostMapping
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF})
    @Operation(summary = "Membuat customer baru", description = "Menambahkan data customer baru ke dalam sistem.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Customer berhasil dibuat"),
        @ApiResponse(responseCode = "400", description = "Request tidak valid / Gagal validasi data")
    })
    public ResponseEntity<Response<CustomerResponse>> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        CustomerResponse data = customerService.createCustomer(request);
        // Dibungkus menggunakan Response.created sesuai standarisasi payload Anda
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.created(data, "Customer created successfully"));
    }

    @GetMapping("/{id}")
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF, UserRole.APPROVER})
    @Operation(summary = "Mendapatkan customer berdasarkan ID", description = "Mengambil data detail customer berdasarkan ID unik.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Data customer ditemukan"),
        @ApiResponse(responseCode = "404", description = "Customer tidak ditemukan")
    })
    public ResponseEntity<Response<CustomerResponse>> getCustomerById(@PathVariable Long id) {
        CustomerResponse data = customerService.getCustomerById(id);
        return ResponseEntity.ok(Response.ok(data, "Customer retrieved successfully"));
    }

    @GetMapping
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF, UserRole.APPROVER})
    @Operation(summary = "Mendapatkan semua daftar customer", description = "Mengambil seluruh data customer yang tersimpan di dalam database.")
    @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan seluruh daftar customer")
    public ResponseEntity<Response<List<CustomerResponse>>> getAllCustomers() {
        List<CustomerResponse> data = customerService.getAllCustomers();
        return ResponseEntity.ok(Response.ok(data, "All customers retrieved successfully"));
    }

    @DeleteMapping("/{id}")
    @RequiresRoles({UserRole.ADMIN})
    @Operation(summary = "Menghapus customer berdasarkan ID", description = "Menghapus data customer dari database berdasarkan ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "No Content"),
        @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<Response<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(Response.ok(null, "Customer deleted successfully"));
    }

    @PutMapping("/{id}")
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF})
    @Operation(summary = "Memperbarui data customer secara penuh (PUT)", description = "Mengganti seluruh field data customer lama dengan data baru.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Customer berhasil diperbarui"),
        @ApiResponse(responseCode = "400", description = "Request tidak valid"),
        @ApiResponse(responseCode = "404", description = "Customer tidak ditemukan")
    })
    public ResponseEntity<Response<CustomerResponse>> updateCustomer(
            @PathVariable Long id, 
            @Valid @RequestBody CreateCustomerRequest request) { 
        CustomerResponse data = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(Response.ok(data, "Customer fully updated successfully"));
    }

    @PatchMapping("/{id}")
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF})
    @Operation(summary = "Memperbarui data customer secara parsial (PATCH)", description = "Hanya memperbarui field yang dikirimkan saja.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Customer berhasil diperbarui sebagian"),
        @ApiResponse(responseCode = "400", description = "Constraint validasi field tidak terpenuhi"),
        @ApiResponse(responseCode = "404", description = "Customer tidak ditemukan")
    })
    public ResponseEntity<Response<CustomerResponse>> patchCustomer(
            @PathVariable Long id, 
            @Valid @RequestBody PatchCustomerRequest request) {
        CustomerResponse data = customerService.patchCustomer(id, request);
        return ResponseEntity.ok(Response.ok(data, "Customer partially updated successfully"));
    }

    @GetMapping("/search")
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF, UserRole.APPROVER})
    @Operation(summary = "Mencari customer berdasarkan nama", description = "Mencari data customer menggunakan query parameter 'name' (Case-Insensitive).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan hasil pencarian customer"),
        @ApiResponse(responseCode = "400", description = "Query parameter 'name' kosong atau tidak valid")
    })
    public ResponseEntity<Response<List<CustomerResponse>>> searchCustomers(@RequestParam(name = "name") String name) {
        List<CustomerResponse> data = customerService.searchByName(name);
        return ResponseEntity.ok(Response.ok(data, "Customers search results retrieved"));
    }

    @GetMapping("/{customerId}/loan-applications")
    @RequiresRoles({UserRole.ADMIN, UserRole.STAFF, UserRole.APPROVER})
    @Operation(
        summary = "Mendapatkan daftar pinjaman berdasarkan ID Customer", 
        description = "Mengambil seluruh riwayat atau daftar pengajuan loan milik satu customer tertentu."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Berhasil mendapatkan daftar pinjaman customer"),
        @ApiResponse(responseCode = "404", description = "Customer tidak ditemukan")
    })
    public ResponseEntity<Response<List<LoanApplicationResponse>>> getLoanApplicationsByCustomer(
            @PathVariable(name = "customerId") Long customerId) {
        
        List<LoanApplicationResponse> data = loanApplicationService.getLoansByCustomerId(customerId);
        return ResponseEntity.ok(Response.ok(data, "Loan applications for this customer retrieved"));
    }
}