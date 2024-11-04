package com.vue3Test.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PostsModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String title;
    private String update_dt;

    // Getters and Setters

   
    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
	    return title;
	}

	public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateDt() {
        return update_dt;
    }

    public void setUpdateDt(String updateDt) {
        this.update_dt = updateDt;
    }
}
   
