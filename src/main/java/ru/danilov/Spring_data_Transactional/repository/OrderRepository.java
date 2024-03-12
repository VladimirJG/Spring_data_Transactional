package ru.danilov.Spring_data_Transactional.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danilov.Spring_data_Transactional.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}