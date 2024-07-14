package ManagementSystem.fpt.Repositories;

import ManagementSystem.fpt.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByPhone(String phone);
}
