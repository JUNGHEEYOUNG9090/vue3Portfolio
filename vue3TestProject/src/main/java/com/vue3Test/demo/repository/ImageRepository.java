package com.vue3Test.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vue3Test.demo.model.ImageModel;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
}
