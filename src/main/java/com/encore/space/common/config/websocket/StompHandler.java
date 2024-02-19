//package com.encore.space.common.config.websocket;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.stereotype.Component;
//
///**
// * Websocket 연결 시 요청 header의 jwt token 유효성을 검증하는 코드.
// */
//@Slf4j
//@RequiredArgsConstructor
//@Component
//public class StompHandler implements ChannelInterceptor {
//
////    private final JwtProvider jwtProvider;
////
////    // 다시 확인해볼 것!
////    @Override
////    public Message<?> preSend(Message<?> message, MessageChannel channel) {
////        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
////        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
////            jwtProvider.validateAccessToken(accessor.getFirstNativeHeader("token"));
////        }
////        return message;
////    }
//
//}
