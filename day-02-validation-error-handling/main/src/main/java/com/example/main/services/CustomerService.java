package com.example.main.services;

import com.example.main.dto.request.CreateCustomerRequest;
import com.example.main.dto.request.PatchCustomerRequest;
import com.example.main.dto.response.CustomerResponse;
import com.example.main.entity.CustomerEntity;
import com.example.main.exceptions.NotFoundException;
import com.example.main.exceptions.DuplicateException;
import com.example.main.repositories.CustomerRepository;
import com.example.main.utils.LogUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        String correlationId = MDC.get("correlation_id");

        if (customerRepository.existsByNik(request.getNik())) {
            log.warn("{{\"level\":\"warn\",\"event\":\"customer_creation_failed\",\"reason\":\"NIK already exists\",\"nik\":\"{}\",\"correlation_id\":\"{}\"}}", 
                    LogUtils.maskNik(request.getNik()), correlationId);
            throw new DuplicateException("NIK already exists");
        }
        if (customerRepository.existsByEmail(request.getEmail())) {
            log.warn("{{\"level\":\"warn\",\"event\":\"customer_creation_failed\",\"reason\":\"Email already exists\",\"email\":\"{}\",\"correlation_id\":\"{}\"}}", 
                    request.getEmail(), correlationId);
            throw new DuplicateException("Email already exists");
        }

        CustomerEntity customer = new CustomerEntity();
        customer.setFullName(request.getFullName());
        customer.setNik(request.getNik());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setCreatedAt(LocalDateTime.now());

        CustomerEntity savedCustomer = customerRepository.save(customer);

        log.info("{{\"level\":\"info\",\"event\":\"customer_created\",\"customer_id\":{},\"nik\":\"{}\",\"phone_number\":\"{}\",\"correlation_id\":\"{}\"}}", 
                savedCustomer.getId(), 
                LogUtils.maskNik(savedCustomer.getNik()), 
                LogUtils.maskPhone(savedCustomer.getPhoneNumber()), 
                correlationId);

        return toResponse(savedCustomer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(Long id) {
        String correlationId = MDC.get("correlation_id");

        return customerRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> {
                    log.warn("{{\"level\":\"warn\",\"event\":\"customer_fetch_failed\",\"reason\":\"Customer not found\",\"customer_id\":{},\"correlation_id\":\"{}\"}}", 
                            id, correlationId);
                    return new NotFoundException("Customer not found");
                });
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCustomer(Long id) {
        String correlationId = MDC.get("correlation_id");

        if (!customerRepository.existsById(id)) {
            log.warn("{{\"level\":\"warn\",\"event\":\"customer_deletion_failed\",\"reason\":\"Customer not found\",\"customer_id\":{},\"correlation_id\":\"{}\"}}", 
                    id, correlationId);
            throw new NotFoundException("Customer not found");
        }
        
        customerRepository.deleteById(id);
        
        log.info("{{\"level\":\"info\",\"event\":\"customer_deleted\",\"customer_id\":{},\"correlation_id\":\"{}\"}}", 
                id, correlationId);
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, CreateCustomerRequest request) {
        String correlationId = MDC.get("correlation_id");

        CustomerEntity existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("{{\"level\":\"warn\",\"event\":\"customer_update_failed\",\"reason\":\"Customer not found\",\"customer_id\":{},\"correlation_id\":\"{}\"}}", 
                            id, correlationId);
                    return new NotFoundException("Customer not found");
                });

        if (!existingCustomer.getNik().equals(request.getNik()) && customerRepository.existsByNik(request.getNik())) {
            log.warn("{{\"level\":\"warn\",\"event\":\"customer_update_failed\",\"reason\":\"NIK already exists\",\"customer_id\":{},\"nik\":\"{}\",\"correlation_id\":\"{}\"}}", 
                    id, LogUtils.maskNik(request.getNik()), correlationId);
            throw new DuplicateException("NIK already exists");
        }
        if (!existingCustomer.getEmail().equals(request.getEmail()) && customerRepository.existsByEmail(request.getEmail())) {
            log.warn("{{\"level\":\"warn\",\"event\":\"customer_update_failed\",\"reason\":\"Email already exists\",\"customer_id\":{},\"email\":\"{}\",\"correlation_id\":\"{}\"}}", 
                    id, request.getEmail(), correlationId);
            throw new DuplicateException("Email already exists");
        }

        existingCustomer.setFullName(request.getFullName());
        existingCustomer.setNik(request.getNik());
        existingCustomer.setEmail(request.getEmail());
        existingCustomer.setPhoneNumber(request.getPhoneNumber());
        existingCustomer.setUpdatedAt(LocalDateTime.now());

        CustomerEntity updatedCustomer = customerRepository.save(existingCustomer);

        log.info("{{\"level\":\"info\",\"event\":\"customer_updated\",\"customer_id\":{},\"type\":\"FULL\",\"correlation_id\":\"{}\"}}", 
                updatedCustomer.getId(), correlationId);

        return toResponse(updatedCustomer);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> searchByName(String name) {
        String correlationId = MDC.get("correlation_id");

        if (name == null || name.trim().isEmpty()) {
            return java.util.Collections.emptyList();
        }

        List<CustomerEntity> customers = customerRepository.findByFullNameContainingIgnoreCase(name.trim());
        
        if (customers.isEmpty()) {
            log.warn("{{\"level\":\"warn\",\"event\":\"customer_search_empty\",\"query\":\"{}\",\"correlation_id\":\"{}\"}}", 
                    name, correlationId);
            throw new NotFoundException("Customer with name containing '" + name + "' not found");
        }
        
        return customers.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerResponse patchCustomer(Long id, PatchCustomerRequest request) {
        String correlationId = MDC.get("correlation_id");

        CustomerEntity existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("{{\"level\":\"warn\",\"event\":\"customer_patch_failed\",\"reason\":\"Customer not found\",\"customer_id\":{},\"correlation_id\":\"{}\"}}", 
                            id, correlationId);
                    return new NotFoundException("Customer not found");
                });

        if (request.getFullName() != null) {
            existingCustomer.setFullName(request.getFullName());
        }

        if (request.getNik() != null) {
            if (!existingCustomer.getNik().equals(request.getNik()) && customerRepository.existsByNik(request.getNik())) {
                log.warn("{{\"level\":\"warn\",\"event\":\"customer_patch_failed\",\"reason\":\"NIK already exists\",\"customer_id\":{},\"nik\":\"{}\",\"correlation_id\":\"{}\"}}", 
                        id, LogUtils.maskNik(request.getNik()), correlationId);
                throw new DuplicateException("NIK already exists");
            }
            existingCustomer.setNik(request.getNik());
        }

        if (request.getEmail() != null) {
            if (!existingCustomer.getEmail().equals(request.getEmail()) && customerRepository.existsByEmail(request.getEmail())) {
                log.warn("{{\"level\":\"warn\",\"event\":\"customer_patch_failed\",\"reason\":\"Email already exists\",\"customer_id\":{},\"email\":\"{}\",\"correlation_id\":\"{}\"}}", 
                        id, request.getEmail(), correlationId);
                throw new DuplicateException("Email already exists");
            }
            existingCustomer.setEmail(request.getEmail());
        }

        if (request.getPhoneNumber() != null) {
            existingCustomer.setPhoneNumber(request.getPhoneNumber());
        }

        CustomerEntity updatedCustomer = customerRepository.save(existingCustomer);

        log.info("{{\"level\":\"info\",\"event\":\"customer_updated\",\"customer_id\":{},\"type\":\"PARTIAL\",\"correlation_id\":\"{}\"}}", 
                updatedCustomer.getId(), correlationId);

        return toResponse(updatedCustomer);
    }

    private CustomerResponse toResponse(CustomerEntity customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setFullName(customer.getFullName());
        response.setNik(customer.getNik());
        response.setEmail(customer.getEmail());
        response.setPhoneNumber(customer.getPhoneNumber());
        return response;
    }
}