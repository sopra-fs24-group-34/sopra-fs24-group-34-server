package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class AuthenticationDTO {
    private Long id;
    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}
}
