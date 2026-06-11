package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.Customer;

public class CustomerService {
    private Map<Long, Customer> customerStorage = new HashMap<>();
    private Long sequence = 1L;

    public Map<Long, Customer> getCustomerStorage() {
        return customerStorage;
    }
    public void setCustomerStorage(Map<Long, Customer> customerStorage) {
        this.customerStorage = customerStorage;
    }
    public Long getSequence() {
        return sequence;
    }
    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    public void createCustomer(String fullName, String email, String phoneNumber) {
        Customer customer = new Customer(sequence, fullName, email, phoneNumber);

        customerStorage.put(sequence, customer);

        sequence++;

    }

    public Customer getCustomerById(Long id) {
        return customerStorage.get(id);
    }

    public ArrayList<Customer> getAllCustomer() {
        return new ArrayList<>(customerStorage.values());
    }
}
