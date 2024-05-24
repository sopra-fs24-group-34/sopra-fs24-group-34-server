package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationDTO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class AuthenticationService {
    private final Gson gson = new Gson();

//  @Autowired
//  public AuthenticationService() {
//
//  }

//nedim-j: wip
  public Boolean isAuthenticated(User user, AuthenticationDTO authenticationDTO) {
    return (Objects.equals(user.getId(), authenticationDTO.getId()) && Objects.equals(user.getToken(), authenticationDTO.getToken()));

  }


}
