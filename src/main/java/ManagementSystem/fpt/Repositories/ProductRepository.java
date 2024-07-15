package ManagementSystem.fpt.Repositories;

import ManagementSystem.fpt.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
  // You can add custom query methods here if needed
}
