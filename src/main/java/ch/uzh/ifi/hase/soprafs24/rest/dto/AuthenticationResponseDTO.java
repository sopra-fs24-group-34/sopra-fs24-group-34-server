package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class AuthenticationResponseDTO {
    private Long id;
    private String token;

    private String profilePicture;

    public AuthenticationResponseDTO() {
    }
    public AuthenticationResponseDTO(Long id, String token, String profilePicture) {
        this.id = id;
        this.token = token;
        this.profilePicture = profilePicture;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}

