package ManagementSystem.fpt.Services;

import ManagementSystem.fpt.Models.Customer;
import ManagementSystem.fpt.Repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Customer createCustomer(Customer customer) {
        if (customerRepository.existsByPhone(customer.getPhone())) {
            throw new IllegalArgumentException("Phone number already registered");
        }
        customer.setCreatedDate(new Date());
        customer.setBirthdate(null);
        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (updatedCustomer.getName() != null) {
            existingCustomer.setName(updatedCustomer.getName());
        }
        if (updatedCustomer.getPhone() != null) {
            existingCustomer.setPhone(updatedCustomer.getPhone());
        }
        if (updatedCustomer.getCustomerType() != null) {
            existingCustomer.setCustomerType(updatedCustomer.getCustomerType());
        }
        if (updatedCustomer.getAddress() != null) {
            existingCustomer.setAddress(updatedCustomer.getAddress());
        }
        if (updatedCustomer.getCity() != null) {
            existingCustomer.setCity(updatedCustomer.getCity());
        }
        if (updatedCustomer.getTaxCode() != null) {
            existingCustomer.setTaxCode(updatedCustomer.getTaxCode());
        }
        if (updatedCustomer.getBirthdate() != null) {
            existingCustomer.setBirthdate(updatedCustomer.getBirthdate());
        }
        if (updatedCustomer.getImageUrl() != null) {
            existingCustomer.setImageUrl(updatedCustomer.getImageUrl());
        }
        if (updatedCustomer.getEmail() != null) {
            existingCustomer.setEmail(updatedCustomer.getEmail());
        }
        if (updatedCustomer.getGroupName() != null) {
            existingCustomer.setGroupName(updatedCustomer.getGroupName());
        }
        if (updatedCustomer.getWard() != null) {
            existingCustomer.setWard(updatedCustomer.getWard());
        }
        if (updatedCustomer.getNote() != null) {
            existingCustomer.setNote(updatedCustomer.getNote());
        }
        if (updatedCustomer.getFacebook() != null) {
            existingCustomer.setFacebook(updatedCustomer.getFacebook());
        }
        if (updatedCustomer.getGender() != null) {
            existingCustomer.setGender(updatedCustomer.getGender());
        }

        return customerRepository.save(existingCustomer);
    }


    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
