package com.academy.mortgage.repositories;

import com.academy.mortgage.model.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConstantsRepository extends JpaRepository<Constants, Integer> {
    List<Constants> findAll();
}
