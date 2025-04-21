package DTOs;

// DTO for JSON parsing
public class LoginRequest {
    private String username;
    private String password;
    private String email;

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
	public Object getEmail() {
		
		return this.email;
	}
}

