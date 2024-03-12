package ru.danilov.Spring_data_Transactional.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danilov.Spring_data_Transactional.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}