package ch.uzh.ifi.hase.soprafs24.websocket;

import javax.servlet.http.HttpSession;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class WSHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            //nedim-j: for debug purposes
            //System.out.println("WSHANDLER before:"  + wsHandler);

            /*
            HttpHeaders headers = new HttpHeaders();
            headers.addAll(request.getHeaders());
            System.out.println("Headers? " + headers);
            System.out.println("Attributes? " + attributes);

             */
            /*
            HttpHeaders headers = request.getHeaders();
            String userId = headers.getFirst("userId");
            System.out.println("Request Headers:");
            headers.forEach((key, value) -> System.out.println(key + ": " + value));
            System.out.println("User ID Header: " + userId);

             */

            /*
            String userId = request.getHeaders().getFirst("userId");
            if (userId != null) {
                // Store userId in WebSocket session attributes
                attributes.put("userId", userId);
            }
            System.out.println("UserId on handshake: "+ userId);
            System.out.println("Uri: "+ request.getURI().getQuery());

             */

            /*
            String userId = ((ServletServerHttpRequest) request).getServletRequest().getParameter("userId");
            System.out.println("ServletRequest header userId: "+ ((ServletServerHttpRequest) request).getServletRequest().getHeaders().get);
            System.out.println("ServletRequest header userId: "+ ((ServletServerHttpRequest) request).getServletRequest().getHeader("userId"));
            System.out.println("ServletRequest session attribute names: "+ ((ServletServerHttpRequest) request).getServletRequest().getSession().getAttributeNames());
            System.out.println("ServletRequest parametermap: "+ ((ServletServerHttpRequest) request).getServletRequest().getParameterMap());

            if (userId != null) {
                // Store userId in WebSocket session attributes
                attributes.put("userId", userId);
                System.out.println("Stored userId in attributes: " + userId);
            }

             */
        }
        return true;
    }

    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception ex) {
        //System.out.println("WSHANDLER after:"  + wsHandler);
    }
}
