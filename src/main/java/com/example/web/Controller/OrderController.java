package com.example.web.Controller;

import com.example.web.Entity.Order;
import com.example.web.Entity.Product;
import com.example.web.Repository.OrderRepository;
import com.example.web.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/api/orders")
    public List<Order> getAll() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/api/orders/{id}")
    public Order getById(@PathVariable Long id) {
        return orderRepository.findById(id).orElseThrow();
    }

    @PostMapping("/api/orders")
    public Order create(@RequestBody Order order) {
        if (order.getProduct() != null && order.getProduct().getId() != null) {
            Product product = productRepository.findById(order.getProduct().getId())
                    .orElseThrow();
            order.setProduct(product);
            order.setTotalPrice(product.getPrice());
        }
        return orderRepository.save(order);
    }

    @PutMapping("/api/admin/orders/{id}")
    public Order updateStatus(@PathVariable Long id, @RequestBody Order updatedOrder) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setStatus(updatedOrder.getStatus());
                    order.setNotes(updatedOrder.getNotes());
                    if (updatedOrder.getStatus().equals("COMPLETED") && order.getProduct() != null) {
                        order.getProduct().setSold(true);
                        productRepository.save(order.getProduct());
                    }
                    return orderRepository.save(order);
                }).orElseThrow();
    }

    @DeleteMapping("/api/admin/orders/{id}")
    public void delete(@PathVariable Long id) {
        orderRepository.deleteById(id);
    }

    @GetMapping("/api/admin/orders/status/{status}")
    public List<Order> getByStatus(@PathVariable String status) {
        return orderRepository.findByStatusOrderByCreatedAtDesc(status);
    }
}
