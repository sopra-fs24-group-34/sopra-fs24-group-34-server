package ch.uzh.ifi.hase.soprafs24.service;
import ch.uzh.ifi.hase.soprafs24.entity.Lobby;
import ch.uzh.ifi.hase.soprafs24.repository.LobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
//

import java.util.UUID;

@Service
@Transactional
public class LobbyService {

    private final Logger log = LoggerFactory.getLogger(LobbyService.class);

    private final LobbyRepository LobbyRepository;
    private final UserRepository UserRepository;
    @Autowired
    public LobbyService(@Qualifier("lobbyRepository") LobbyRepository lobbyRepository,
                        @Qualifier ("userRepository") UserRepository userRepository) {
        this.LobbyRepository = lobbyRepository;
        this.UserRepository = userRepository;
    }

    public Lobby createlobby(Long userId){
        Lobby newlobby = new Lobby();
        newlobby.setToken(UUID.randomUUID().toString());
        newlobby.setUser(UserRepository.findUserById(userId));

        newlobby = LobbyRepository.save(newlobby);
        LobbyRepository.flush();

        log.debug("Created Information for Lobby: {}", newlobby);
        return newlobby;
    }
}
