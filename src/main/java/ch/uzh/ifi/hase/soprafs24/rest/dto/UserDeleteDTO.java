package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class UserDeleteDTO {
    private String username;

    private Long id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
