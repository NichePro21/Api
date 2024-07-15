package ManagementSystem.fpt.Services;


import ManagementSystem.fpt.Models.Product;
import ManagementSystem.fpt.Repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setCategory(productDetails.getCategory());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());
        product.setBarcode(productDetails.getBarcode());
        product.setImageUrl(productDetails.getImageUrl());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

  public int checkInventory(Long productId) {
    Optional<Product> productOptional = productRepository.findById(productId);
    if (productOptional.isPresent()) {
      return productOptional.get().getStock();
    } else {
      throw new RuntimeException("Product not found");
    }
  }

  @GetMapping("/{id}/inventory")
  public ResponseEntity<Integer> checkInventory(@PathVariable Long id) {
    int stock = productService.checkInventory(id);
    return ResponseEntity.ok(stock);
  }
}
