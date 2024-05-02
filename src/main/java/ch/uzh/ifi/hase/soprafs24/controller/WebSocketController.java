package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.rest.dto.MessagePostDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/receiveMessage")
    public String sendMessage(String message) {
        //String m = message.getMessage();
        System.out.println("Received message from client: " + message);
        return "Message received: " + message;
        //return message;
    }

}