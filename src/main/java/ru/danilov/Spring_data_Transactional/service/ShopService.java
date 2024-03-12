package ru.danilov.Spring_data_Transactional.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.danilov.Spring_data_Transactional.dto.OrderDto;
import ru.danilov.Spring_data_Transactional.model.Customer;
import ru.danilov.Spring_data_Transactional.model.Order;
import ru.danilov.Spring_data_Transactional.model.Product;
import ru.danilov.Spring_data_Transactional.repository.CustomerRepository;
import ru.danilov.Spring_data_Transactional.repository.OrderRepository;
import ru.danilov.Spring_data_Transactional.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    @Autowired
    public ShopService(CustomerRepository customerRepository, OrderRepository orderRepository, ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void placeOrder(OrderDto orderDto) {
        Customer customer = customerRepository.findById(orderDto.getId()).orElse(null);
        try {
            Double balance = updateCustomerBalance(orderDto);
            List<Product> products = updateProductInventory(orderDto);
            Order order = new Order(orderDto.getId(), balance, customer, products);
            createOrder(order);
        } catch (Exception e) {
            throw new RuntimeException("Error placing order. Rolling back transaction.", e);
        }
    }

    @Transactional(propagation = Propagation.NESTED)
    public void createOrder(Order order) {
        orderRepository.save(order);
    }

    @Transactional(propagation = Propagation.NESTED)
    public List<Product> updateProductInventory(OrderDto orderDto) {
        List<Product> productList = new ArrayList<>();
        for (Integer id : orderDto.getProductsId()) {
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                product.get().setQuantity(product.get().getQuantity() - 1);
                productRepository.save(product.get());
                productList.add(product.get());
            }
        }
        return productList;
    }

    @Transactional(propagation = Propagation.NESTED)
    public Double updateCustomerBalance(OrderDto orderDto) {
        double sum = 0;
        for (Integer id : orderDto.getProductsId()) {
            Optional<Product> product = productRepository.findById(id);
            if (product.isPresent()) {
                sum += product.get().getPrice();
            }
        }
        saveCustomer(orderDto.getCustomerId(), sum);
        return sum;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveCustomer(int id, Double sum) {
        Customer customer = customerRepository.findById(id).orElse(null);
        assert customer != null;
        customer.setBalance(customer.getBalance() - sum);
        customerRepository.save(customer);
    }
}