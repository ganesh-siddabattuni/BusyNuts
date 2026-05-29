package com.busynuts.backend.controller;

import com.busynuts.backend.dto.OrderRequest;
import com.busynuts.backend.model.Order;
import com.busynuts.backend.model.OrderItem;
import com.busynuts.backend.model.Product;
import com.busynuts.backend.repository.OrderRepository;
import com.busynuts.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @PostMapping
    @Transactional // Ensures that if any step fails, NO data is saved (prevents ghost stock deductions)
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setShippingAddress(request.getShippingAddress());

        List<OrderItem> items = new ArrayList<>();
        double total = 0.0;

        for (OrderRequest.CartItem cartItem : request.getCartItems()) {
            Optional<Product> productOpt = productRepository.findById(cartItem.getProductId());
            
            if (productOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Product ID " + cartItem.getProductId() + " not found.");
            }

            Product product = productOpt.get();

            // Check if we have enough stock
            if (product.getStockQuantity() < cartItem.getQuantity()) {
                return ResponseEntity.badRequest().body("Insufficient stock for: " + product.getName());
            }

            // Deduct the inventory stock
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);

            // Create the order item
            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(product.getPrice());
            items.add(item);

            total += (product.getPrice() * cartItem.getQuantity());
        }

        order.setItems(items);
        order.setTotalAmount(total);
        
        // Saving the order automatically saves the OrderItems because of CascadeType.ALL
        Order savedOrder = orderRepository.save(order);

        return ResponseEntity.ok(savedOrder);
    }
}