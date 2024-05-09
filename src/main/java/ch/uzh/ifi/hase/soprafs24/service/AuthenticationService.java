package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.rest.dto.AuthenticationDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.GamePostDTO;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class AuthenticationService {
    private final Gson gson = new Gson();

  @Autowired
  public AuthenticationService() {

  }

//nedim-j: wip
  public Boolean isAuthenticated(String input) {
    // smailalijagic: change settings
      Map<String, Object> requestMap = gson.fromJson(input, Map.class);
      GamePostDTO gamePostDTO = gson.fromJson(gson.toJson(requestMap.get("gamePostDTO")), GamePostDTO.class);
      AuthenticationDTO authenticationDTO = gson.fromJson(gson.toJson(requestMap.get("authenticationDTO")), AuthenticationDTO.class);

      return true;
  }


}
