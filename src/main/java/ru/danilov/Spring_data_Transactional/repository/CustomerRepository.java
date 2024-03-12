package ru.danilov.Spring_data_Transactional.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.danilov.Spring_data_Transactional.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}