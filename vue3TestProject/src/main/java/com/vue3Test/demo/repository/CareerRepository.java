package com.vue3Test.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vue3Test.demo.model.CareerModel;

public interface CareerRepository extends JpaRepository<CareerModel, String> {
}
