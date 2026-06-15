package com.example.main.impl;

import org.springframework.stereotype.Repository;

import com.example.main.models.Customer;
import com.example.main.repositories.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final Map<Long, Customer> customerDatabase = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public CustomerRepositoryImpl() {
        save(new Customer(null, "bener", "bener@mail.com", "08123456789"));
        save(new Customer(null, "apapa", "apapa@mail.com", "08987654321"));
        save(new Customer(null, "dadada", "dadadad@mail.com", "08111222333"));
    }

    @Override
    public Customer save(Customer customer) {
        if (customer.getId() == null) {
            customer.setId(idGenerator.getAndIncrement());
        }
        customerDatabase.put(customer.getId(), customer);
        return customer;
    }

    @Override
    public Customer findById(Long id) {
        return customerDatabase.get(id);
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customerDatabase.values());
    }

    @Override
    public boolean deleteById(Long id) {
        return customerDatabase.remove(id) != null;
    }

    @Override
    public List<Customer> searchByName(String name) {
        List<Customer> result = new ArrayList<>();
        for (Customer customer : customerDatabase.values()) {
            if (customer.getFullName().toLowerCase().contains(name.toLowerCase())) {
                result.add(customer);
            }
        }
        return result;
    }
}
