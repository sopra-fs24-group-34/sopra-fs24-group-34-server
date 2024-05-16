package ch.uzh.ifi.hase.soprafs24.websocket;

import ch.uzh.ifi.hase.soprafs24.service.AuthenticationService;
import ch.uzh.ifi.hase.soprafs24.service.LobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Lob;

@Component
public class ServiceGetter {


    /*
    private final ApplicationContext context;

    @Autowired
    public ServiceGetter(ApplicationContext context) {
        this.context = context;
    }

    public LobbyService getLobbyService() {
        return context.getBean(LobbyService.class);
    }

     */


    private LobbyService lobbyService;
}
