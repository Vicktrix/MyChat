package com.MyChat.controller;

import com.MyChat.service.MySavedChats;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class MyHandler {

    private MySavedChats myChats;
    private AtomicInteger sequence = new AtomicInteger(0);

    public MyHandler(MySavedChats myChats) {
        this.myChats = myChats;
    }

    public Mono<ServerResponse> writeToChat(ServerRequest request) {
        var chatName = request.pathVariable("id");
        return request.bodyToMono(String.class)
                .doOnNext(s -> System.out.println(" post : " + s))
                .flatMap(message -> {
//                    myChats.addEvent(message);
                    myChats.addEventToChat(message, chatName);
                    return ServerResponse.ok().build();
                });
    }

    public Mono<ServerResponse> getChat(ServerRequest request) {
        var chatName = request.pathVariable("id");
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
//                .body(myChats.registerChat()
                .body(myChats.registerChat(chatName)
                        .delayElements(Duration.ofMillis(500))
                        .doOnNext(s -> System.out.println("Emit : " + s))
                        .map(message -> ServerSentEvent.<String>builder()
                        .id(String.valueOf(sequence.getAndIncrement()))
//                        .event(request.pathVariable("id"))
                        .event(chatName)
                        .data("Sink : " + message)
                        .build()),
                        ServerSentEvent.class);
    };
    
//   public Mono<ServerResponse> createNewChat(ServerRequest request) {
//        var chatName = request.pathVariable("id");
//        return ServerResponse.ok()
//                .contentType(MediaType.TEXT_EVENT_STREAM)
//                .body(myChats.createNewChat(chatName)
//                        .delayElements(Duration.ofMillis(500))
//                        .doOnNext(s -> System.out.println("Emit : " + s))
//                        .map(message -> ServerSentEvent.<String>builder()
//                        .id(String.valueOf(sequence.getAndIncrement()))
////                        .event(request.pathVariable("id"))
//                        .event(chatName)
//                        .data("Sink : " + message)
//                        .build()),
//                        ServerSentEvent.class);
//    }

    private Flux<ServerSentEvent<String>> map(Flux<String> flux, String chatName) {
        return flux.delayElements(Duration.ofMillis(500))
                .doOnNext(s -> System.out.println("Emit : " + s))
                .map(message -> ServerSentEvent.<String>builder()
                .id(String.valueOf(sequence.getAndIncrement()))
                .event(chatName)
                .data("Sink : " + message)
                .build());
    }
}
