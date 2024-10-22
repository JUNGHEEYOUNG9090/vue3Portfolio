package com.vue3Test.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vue3Test.demo.model.CareerModel;
import com.vue3Test.demo.repository.CareerRepository;

@Service
public class CarrerService {
	@Autowired
	private CareerRepository careerRepository;
	
	public CareerModel getCareerById(String id) {
	        return careerRepository.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("해당 ID에 대한 경력을 찾을 수 없습니다: " + id));
	    }
}
