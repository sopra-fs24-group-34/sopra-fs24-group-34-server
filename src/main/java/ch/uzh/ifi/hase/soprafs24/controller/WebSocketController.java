package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.rest.dto.MessagePostDTO;
import ch.uzh.ifi.hase.soprafs24.service.ChatServiceWebSockets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ch.uzh.ifi.hase.soprafs24.service.ChatServiceWebSockets;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendMessage")
    public String sendMessage(String message) {
        //String m = message.getMessage();
        System.out.println("Received message from client: " + message);

        String destination = "/game/10/receiveMessage";
        messagingTemplate.convertAndSend(destination, message); // smailalijagic: message is sent

        return "Message received: " + message;
        //return message;
    }

}