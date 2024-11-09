package com.intellipick.intern.domain.repository;

import com.intellipick.intern.domain.model.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUsername(String username);

    @EntityGraph(attributePaths = {"authorities"})
    Optional<Customer> findByUsername(String username);
}
