package com.vue3Test.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vue3Test.demo.model.DevLogModel;

public interface DevLogRepository extends JpaRepository<DevLogModel, Long> {
	@Query("SELECT d FROM DevLogModel d ORDER BY d.update_dt, d.id DESC")
	List<DevLogModel> findAllByOrderByUpdateDtDesc();
}
