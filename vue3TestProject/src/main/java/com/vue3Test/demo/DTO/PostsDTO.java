package com.vue3Test.demo.DTO;

public class PostsDTO {
	private long id;
    private String title;
    private String update_dt;
    private String type;
    
    public PostsDTO() {}
    
    public PostsDTO(long id, String title,  String update_dt, String type) {
    	this.id = id;
        this.title = title;
        this.update_dt = update_dt;
        this.type = type;
    }
    
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
	public String getUpdate_dt() {
		return update_dt;
	}
	public void setUpdate_dt(String update_dt) {
		this.update_dt = update_dt;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}	
}
   
