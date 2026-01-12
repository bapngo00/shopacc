package com.example.web.Controller;

import com.example.web.Entity.Product;
import com.example.web.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/api/products")
    public List<Product> getAll() {
        // Chỉ trả về sản phẩm chưa bán cho người dùng công khai
        return productRepository.findAll().stream()
                .filter(p -> !p.isSold())
                .toList();
    }

    @GetMapping("/api/admin/products")
    public List<Product> getAllForAdmin() {
        // Admin có thể xem tất cả sản phẩm, kể cả đã bán
        return productRepository.findAll();
    }

    @GetMapping("/api/products/{id}")
    public Product getById(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        // Nếu sản phẩm đã bán, không cho phép xem (trả về 404)
        if (product.isSold()) {
            throw new RuntimeException("Sản phẩm không tồn tại");
        }
        return product;
    }

    @PostMapping("/api/admin/products")
    public Product add(@RequestBody Product product) {
        if (product.getMsCode() == null || product.getMsCode().isEmpty()) {
            // Tự động tăng mã số MS
            List<Product> allProducts = productRepository.findAll();
            int maxMsNumber = 0;
            
            for (Product p : allProducts) {
                if (p.getMsCode() != null && p.getMsCode().startsWith("#MS")) {
                    try {
                        String numberPart = p.getMsCode().substring(3); // Bỏ qua "#MS"
                        int num = Integer.parseInt(numberPart);
                        if (num > maxMsNumber) {
                            maxMsNumber = num;
                        }
                    } catch (NumberFormatException e) {
                        // Bỏ qua nếu không parse được
                    }
                }
            }
            
            product.setMsCode("#MS" + (maxMsNumber + 1));
        }
        product.setSold(false);
        return productRepository.save(product);
    }

    @PutMapping("/api/admin/products/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product newProduct) {
        return productRepository.findById(id)
                .map(p -> {
                    if (newProduct.getMsCode() != null) {
                        p.setMsCode(newProduct.getMsCode());
                    }
                    p.setTitle(newProduct.getTitle());
                    p.setPrice(newProduct.getPrice());
                    p.setServer(newProduct.getServer());
                    p.setClassName(newProduct.getClassName());
                    p.setLevel(newProduct.getLevel());
                    if (newProduct.getTtgt() != null) {
                        p.setTtgt(newProduct.getTtgt());
                    }
                    if (newProduct.getLogoImage() != null) {
                        p.setLogoImage(newProduct.getLogoImage());
                    }
                    if (newProduct.getImgData() != null) {
                        p.setImgData(newProduct.getImgData());
                    }
                    if (newProduct.getFacebookLink() != null) {
                        p.setFacebookLink(newProduct.getFacebookLink());
                    }
                    if (newProduct.getHighlightInfo() != null) {
                        p.setHighlightInfo(newProduct.getHighlightInfo());
                    }
                    p.setSold(newProduct.isSold());
                    return productRepository.save(p);
                }).orElseThrow();
    }

    @DeleteMapping("/api/admin/products/{id}")
    public void delete(@PathVariable Long id) {
        productRepository.deleteById(id);
    }
}
