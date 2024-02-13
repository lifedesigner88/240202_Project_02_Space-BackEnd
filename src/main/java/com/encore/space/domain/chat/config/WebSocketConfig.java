package com.encore.space.domain.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@EnableWebSocketMessageBroker   // STOMP 사용을 위해 필요
@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 웹소켓을 위한 메시지 브로커를 설정한다.
     *
     * @param registry 메시지 브로커를 설정하는 데 사용되는 MessageBrokerRegistry 인스턴스
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 구독하는 요청 URL(메시지 수신)
        registry.enableSimpleBroker("/sub");
        // 메시지를 발행하는 요청 URL(메시지 송신)
        registry.setApplicationDestinationPrefixes("/pub");
    }

    /**
     * WebSocket 통신을 위한 STOMP 엔드포인트를 등록한다.
     * STOMP 웹소켓의 연결 엔드포인트는 /ws-stomp이다.
     * 웹소켓 통신이 /ws-stomp로 도착할 때 우리는 해당 통신이 웹 소켓 통신 중에서 STOMP 통신인 것을 확인하고, 이를 연결한다.
     *
     * @param registry 엔드포인트를 등록하는 데 사용되는 StompEndpointRegistry 객체
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
//                .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}
