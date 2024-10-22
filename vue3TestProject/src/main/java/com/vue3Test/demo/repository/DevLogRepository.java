package com.vue3Test.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vue3Test.demo.model.DevLogModel;

public interface DevLogRepository extends JpaRepository<DevLogModel, Long> {
}
