package ManagementSystem.fpt.Controller;

import ManagementSystem.fpt.Models.Customer;
import ManagementSystem.fpt.Responses.ApiResponse;
import ManagementSystem.fpt.Services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController extends ApiController {
    @Autowired
    ApiResponse apiResponse;

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer, BindingResult rs) {
        try {
            if (rs.hasErrors()) {
                return apiResponse.failure("Invalid", parseFieldErrors(rs), HttpStatus.UNPROCESSABLE_ENTITY.value());
            } else {
                Customer createdCustomer = customerService.createCustomer(customer);
                return apiResponse.ok("Created customer success", createdCustomer);
            }
        } catch (Exception e) {
            return apiResponse.failure(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
        try {
            Customer customer = customerService.updateCustomer(id, updatedCustomer);
            return apiResponse.ok("Customer updated successfully", customer);
        } catch (IllegalArgumentException e) {
            return apiResponse.failure("Customer not found", null, HttpStatus.NOT_FOUND.value());
        } catch (Exception e) {
            return apiResponse.failure("An error occurred while updating the customer", e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}
