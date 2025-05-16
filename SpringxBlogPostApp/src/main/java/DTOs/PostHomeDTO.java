package DTOs;


public class PostHomeDTO {
		
	private int id;
	
	private String title;
	
	private String created;
	
	private String updated;
	
	private String url;
	
	private String summary;
	
	private String authorUsername;
	
	public PostHomeDTO() {
		super();
	}

	public PostHomeDTO(int id, String title, String created, String updated, String url, String summary,String authorUsername) {
		super();
		this.id = id;
		this.title = title;
		this.created = created;
		this.updated = updated;
		this.url = url;
		this.summary = summary;
		this.authorUsername = authorUsername;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getAuthorUsername() {
		return authorUsername;
	}

	public void setAuthorUsername(String authorUsername) {
		this.authorUsername = authorUsername;
	}
	
	
}
