package com.encore.space.common.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {



    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // 클라이언트가 구독할 수 있는 endpoint의 접두사를 설정합니다.
        // 구독은 클라이언트가 서버로부터 메시지를 받기 위해 사용됩니다.
        // SimpMessagingTemplate의 convertAndSend() 메소드는 이 endpoint를 사용하여 메시지를 전송하며,
        // 구독하고 있는 모든 클라이언트가 이 메시지를 받게 됩니다.
        registry.enableSimpleBroker("/sub");

        // 클라이언트에서 메시지를 보내는 데 사용되는 endpoint의 접두사를 설정합니다.
        // 클라이언트는 이 접두사를 사용하여 메시지를 보내며, 이 메시지는 @MessageMapping으로 어노테이션이 달린 컨트롤러 메소드에 전달됩니다.
        registry.setApplicationDestinationPrefixes("/pub");

        // 특정 클라이언트 만을 대상으로하는 메시지를 지원하기 위한 설정입니다.
        // 지정된 사용자 접두사로 시작하는 목적지는 특정 사용자 세션에서만 사용되도록 변환됩니다.
        // /user/queue/updates와 같은 목적지를 사용하면 서버는 사용자 세션을 기반으로 메시지를 전달합니다.
//        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    /**
     * 메시지 크기 제한을 통해서 클라이언트의 악의적인 요청을 차단
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(128 * 1024);
    }
}
