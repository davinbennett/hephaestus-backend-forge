package com.example.main.services;

import org.springframework.stereotype.Service;

import com.example.main.dto.CreateCustomerRequest;
import com.example.main.dto.CustomerResponse;
import com.example.main.exceptions.CustomerNotFoundException;
import com.example.main.models.Customer;
import com.example.main.repositories.CustomerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());
        customer.setPhoneNumber(request.getPhoneNumber());

        Customer savedCustomer = customerRepository.save(customer);
        
        return toResponse(savedCustomer);
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id);
        if (customer == null) {
            throw new CustomerNotFoundException(id);
        }
        return toResponse(customer);
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteCustomer(Long id) {
        boolean isDeleted = customerRepository.deleteById(id);
        if (!isDeleted) {
            throw new CustomerNotFoundException(id);
        }
    }

    public CustomerResponse updateCustomer(Long id, CreateCustomerRequest request) {
        Customer existingCustomer = customerRepository.findById(id);
        if (existingCustomer == null) {
            throw new CustomerNotFoundException(id);
        }
        
        existingCustomer.setFullName(request.getFullName());
        existingCustomer.setEmail(request.getEmail());
        existingCustomer.setPhoneNumber(request.getPhoneNumber());
        
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        
        return toResponse(updatedCustomer);
    }

    public List<CustomerResponse> searchByName(String name) {
        return customerRepository.searchByName(name).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CustomerResponse toResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setFullName(customer.getFullName());
        response.setEmail(customer.getEmail());
        response.setPhoneNumber(customer.getPhoneNumber());
        return response;
    }
}