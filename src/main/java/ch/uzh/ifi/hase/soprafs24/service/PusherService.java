package ch.uzh.ifi.hase.soprafs24.service;

import com.pusher.rest.Pusher;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PusherService {
    private final Pusher pusher;

    public void triggerEvent(String channel, String event, Object data) {
        pusher.trigger(channel, event, data);
    }
    public PusherService() {
        pusher = new Pusher("1789620", "a80f75b2d4e6f1aa786a", "8e210648e1f3d99f5923");
        pusher.setCluster("eu");
        pusher.setEncrypted(true);

        pusher.trigger("my-channel", "my-event", Collections.singletonMap("message", "hello world this is pusher"));
    }

    public void startSession(String teamId) {
        triggerEvent("team-" + teamId, "session-update", Collections.singletonMap("status", "on"));
    }

    public void stopSession(String teamId) {
        triggerEvent("team-" + teamId, "session-update", Collections.singletonMap("status", "off"));
    }

}