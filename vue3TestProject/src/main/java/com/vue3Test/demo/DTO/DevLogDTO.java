package com.vue3Test.demo.DTO;

public class DevLogDTO {
	private Long id;
    private String title;
    private String content;
    private String coverImage;
    private String create_dt;
    private String create_user;
    private String update_dt;
    private String update_user;

    
    // 생성자
    public DevLogDTO() {
    	
    }
    
    public DevLogDTO(Long id, String title, String coverImage, String content, String create_dt, String create_user, String update_dt, String update_user) {
        this.id = id;
        this.title = title;
        this.coverImage = coverImage;
        this.content = content;
        this.create_dt = create_dt;
        this.create_user = create_user;
        this.update_dt = update_dt;
        this.update_user = update_user;
    }
    // Getter 및 Setter
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
        return title;
    }

	public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getCreate_dt() {
		return create_dt;
	}

	public void setCreate_dt(String create_dt) {
		this.create_dt = create_dt;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getUpdate_dt() {
		return update_dt;
	}

	public void setUpdate_dt(String update_dt) {
		this.update_dt = update_dt;
	}

	public String getUpdate_user() {
		return update_user;
	}

	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}
}
