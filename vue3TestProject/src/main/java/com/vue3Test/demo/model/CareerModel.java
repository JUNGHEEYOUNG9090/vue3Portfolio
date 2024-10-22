package com.vue3Test.demo.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "career_info")
public class CareerModel {
	@Id
	
	private String career_id;
	@PrePersist
	public void generateId() {
	       this.career_id = UUID.randomUUID().toString(); // 새로운 UUID를 생성
	   }
	
    private String text;
    
	public String getCarrer_id() {
		return career_id;
	}

	public void setCarrer_id(String carrer_id) {
		this.career_id = carrer_id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
    
    
    
}
