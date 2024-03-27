package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class AuthenticationResponseDTO {
    private Long id;
    private String token;

    public AuthenticationResponseDTO() {
    }
    public AuthenticationResponseDTO(Long id, String token) {
        this.id = id;
        this.token = token;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}
}
