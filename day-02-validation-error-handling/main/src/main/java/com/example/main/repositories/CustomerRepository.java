package com.example.main.repositories;

import java.util.List;

import com.example.main.models.Customer;

public interface CustomerRepository {
    Customer save(Customer customer);
    Customer findById(Long id);
    List<Customer> findAll();
    
    boolean deleteById(Long id);
    List<Customer> searchByName(String name);
}
