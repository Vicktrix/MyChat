package com.MyChat.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

@Configuration
public class MyRouter {
    
    private MyHandler handler;

    public MyRouter(MyHandler handler) {
        this.handler = handler;
    }
    
    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
//                .POST("/chat/{id}", handler::writeToChat)
                .POST("/chat/post/{id}", handler::writeToChat)
//                .POST("/chat/new/{id}", handler::createNewChat)
                .GET("/chat/{id}", handler::getChat)
//                .GET("/chat", request -> ServerResponse.ok().contentType(MediaType.TEXT_HTML)
//                        .body(new ClassPathResource("static/Chat.html"), 
//                                org.springframework.core.io.Resource.class))
//                .GET("/chat", request -> {
//                    ClassPathResource resource = new ClassPathResource("static/Chat.html");
//                    return ServerResponse.ok()
//                            .contentType(MediaType.TEXT_HTML)
//                            .body(DataBufferUtils.read(resource, DataBufferUtils.DEFAULT_BUFFER_SIZE), DataBuffer.class);
//                })
                .GET("/chat", request -> {
                    ClassPathResource resource = new ClassPathResource("static/Chat.html");
                    DataBufferFactory bufferFactory = new DefaultDataBufferFactory();
                    Flux<DataBuffer> dataBufferFlux = DataBufferUtils.read(resource, bufferFactory, 4096); // bufferSize = 4096
                    return ServerResponse.ok()
                            .contentType(MediaType.TEXT_HTML)
                            .body(dataBufferFlux, DataBuffer.class);
                })
                .build();
    }
}