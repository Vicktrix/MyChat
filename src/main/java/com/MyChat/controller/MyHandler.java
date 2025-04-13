package com.MyChat.controller;

import com.MyChat.service.MySavedChats;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
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

    public MyHandler(MySavedChats myChats) {
        this.myChats = myChats;
    }

    public Mono<ServerResponse> writeToChat(ServerRequest request) {
        var chatName = request.pathVariable("id");
        return request.bodyToMono(String.class)
            .doOnNext(consoleManager.apply(" post : ").apply(chatName))
            .flatMap(message -> {
                myChats.addEventToChat(message, chatName);
                return ServerResponse.ok().build();
            });
    }

    public Mono<ServerResponse> getChat(ServerRequest request) {
        var chatName = request.pathVariable("id");
        return ServerResponse.ok()
            .contentType(MediaType.TEXT_EVENT_STREAM)
            .body(myChats.registerChat(chatName)
                    .delayElements(Duration.ofMillis(500))
                    .doOnNext(consoleManager.apply(" get : ").apply(chatName))
                    .map(message -> ServerSentEvent.<String>builder()
                        .id(String.valueOf(sequence.getAndIncrement()))
                        .event(chatName).data(message).build()),
                    ServerSentEvent.class);
    };
    private record UserAndHisMessage(String user, String message) {};
    private Function<String, UserAndHisMessage> parseBody = m -> { 
        int index = m.indexOf(" : ");
        return index<1? new UserAndHisMessage("Noname", m) 
                : new UserAndHisMessage(m.substring(0, index), m.substring(index));};
    private Function<String,Function<String,Consumer<String>>> consoleManager = 
            w -> s -> c -> { UserAndHisMessage body = parseBody.apply(c);
        System.out.println("User "+body.user+w+body.message+" in chat "+s);};
}
