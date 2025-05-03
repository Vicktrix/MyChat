package com.MyChat.controller;

import com.MyChat.service.MySavedChats;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class MyHandler {

    private MySavedChats myChats;
    private AtomicInteger sequence = new AtomicInteger(0);
    private Supplier<String> id = () -> String.valueOf(sequence.getAndIncrement());

    public MyHandler(MySavedChats myChats) {
        this.myChats = myChats;
    }
    public Mono<ServerResponse> writeToChat(ServerRequest request) {
        var chat = request.pathVariable("chat");
        var user = request.pathVariable("user");
        return request.bodyToMono(String.class)
//            .doOnNext(print.apply("write : "+user, chat))
            .doOnNext(m -> System.out.println("in chat "+chat+" user "+user+" say "+m))
            .doOnNext(m -> myChats.write(user, chat, m))
            .flatMap(m -> ServerResponse.ok().build());
    }

    public Mono<ServerResponse> getChat(ServerRequest request) {
        var user = request.pathVariable("user");
//        var chat = request.pathVariable("chat");
        return ServerResponse.ok()
            .contentType(MediaType.TEXT_EVENT_STREAM)
            .body(myChats.getUser(user)
                    .delayElements(Duration.ofMillis(500))
                    .doOnNext(System.out::println)
                    .map(cm -> ServerSentEvent.<String>builder()
                        .id(id.get())
                        .event(cm.chat())
                        .data(cm.message())
                        .build()),
                    ServerSentEvent.class);
    };
    public Mono<ServerResponse> initChat(ServerRequest request) {
        var user = request.pathVariable("user");
//        var chat = request.pathVariable("chat");
        return ServerResponse.ok()
            .contentType(MediaType.TEXT_EVENT_STREAM)
            .body(myChats.getFluxChats(user)
//                    .doOnSubscribe(s -> System.out.println("chats : "))
//                    .doOnNext(System.out::println)
                    ,String.class);
    }
    public Mono<ServerResponse> listUsers(ServerRequest request) {
//        var user = request.pathVariable("user");
//        var chat = request.pathVariable("chat");
        return ServerResponse.ok()
            .contentType(MediaType.TEXT_EVENT_STREAM)
            .body(myChats.getUsers()
//                    .doOnSubscribe(s -> System.out.println("users : "))
//                    .doOnNext(System.out::println)
                    ,String.class);
    }
}
