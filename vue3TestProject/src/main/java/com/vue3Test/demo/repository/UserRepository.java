package com.vue3Test.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vue3Test.demo.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {
}
