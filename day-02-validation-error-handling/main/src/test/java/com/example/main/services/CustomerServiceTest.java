package com.example.main.services;

import com.example.main.dto.request.CreateCustomerRequest;
import com.example.main.dto.response.CustomerResponse;
import com.example.main.entity.CustomerEntity;
import com.example.main.exceptions.DuplicateException;
import com.example.main.exceptions.NotFoundException;
import com.example.main.repositories.CustomerRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    // ! SHOULD CREATE CUSTOMER SUCCESSFULLY
    @Test
    void should_create_customer_successfully() {
        // given
        CreateCustomerRequest request = new CreateCustomerRequest(
                "Davin Bennett", "3171012345678901", "davin.bennett@example.com", "081234567890"
        );
        
        CustomerEntity savedEntity = new CustomerEntity();
        savedEntity.setId(1L);
        savedEntity.setFullName(request.getFullName());
        savedEntity.setNik(request.getNik());
        savedEntity.setEmail(request.getEmail());
        savedEntity.setPhoneNumber(request.getPhoneNumber());

        when(customerRepository.existsByNik(request.getNik())).thenReturn(false);
        when(customerRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(savedEntity);

        // when
        CustomerResponse response = customerService.createCustomer(request);

        // then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Davin Bennett", response.getFullName());
        assertEquals("3171012345678901", response.getNik());
        verify(customerRepository, times(1)).save(any(CustomerEntity.class));
    }

    // ! SHOULD GET CUSTOMER BY ID SUCCESSFULLY
    @Test
    void should_get_customer_by_id_successfully() {
        Long customerId = 1L;
        CustomerEntity customer = new CustomerEntity();
        customer.setId(customerId);
        customer.setFullName("Jane Doe");
        customer.setNik("3171098765432109");
        customer.setEmail("jane.doe@example.com");
        customer.setPhoneNumber("081298765432");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        CustomerResponse response = customerService.getCustomerById(customerId);

        assertNotNull(response);
        assertEquals(customerId, response.getId());
        assertEquals("Jane Doe", response.getFullName());
        verify(customerRepository, times(1)).findById(customerId);
    }

    // ! SHOULD THROW NOT FOUND WHEN CUSTOMER DOES NOT EXIST
    @Test
    void should_throw_not_found_when_customer_does_not_exist() {
        // given
        Long nonExistentId = 99L;
        when(customerRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> customerService.getCustomerById(nonExistentId));

        assertEquals("Customer not found", exception.getMessage());
        verify(customerRepository, times(1)).findById(nonExistentId);
    }

    // ! SHOULD RETURN ALL CUSTOMERS
    @Test
    void should_return_all_customers() {
        // given
        CustomerEntity customer1 = new CustomerEntity();
        customer1.setId(1L);
        customer1.setFullName("Customer One");

        CustomerEntity customer2 = new CustomerEntity();
        customer2.setId(2L);
        customer2.setFullName("Customer Two");

        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer1, customer2));

        // when
        List<CustomerResponse> responses = customerService.getAllCustomers();

        // then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Customer One", responses.get(0).getFullName());
        assertEquals("Customer Two", responses.get(1).getFullName());
        verify(customerRepository, times(1)).findAll();
    }

    // ! SHOULD THROW DUPLICATE WHEN NIK ALREADY EXISTS (NEGATIVE PATH)
    @Test
    void should_throw_duplicate_when_nik_already_exists() {
        // given
        CreateCustomerRequest request = new CreateCustomerRequest(
                "John Doe", "3171012345678901", "john.doe@example.com", "081234567890"
        );

        when(customerRepository.existsByNik(request.getNik())).thenReturn(true);

        // when & then
        DuplicateException exception = assertThrows(DuplicateException.class,
                () -> customerService.createCustomer(request));

        assertEquals("NIK already exists", exception.getMessage());
        verify(customerRepository, never()).save(any(CustomerEntity.class));
    }
}