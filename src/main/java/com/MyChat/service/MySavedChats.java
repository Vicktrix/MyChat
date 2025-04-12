package com.MyChat.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class MySavedChats {
    
    private ConcurrentLinkedQueue<String> eventsList = 
            new ConcurrentLinkedQueue<>(Arrays.asList("There", "are", "only", "some", "data", "from", "our", "client", ":)"));
    private Map<String, Sinks.Many<String>> chats = new HashMap<>();
//    private Sinks.Many<String> sink;

    public MySavedChats() {
//        sink = Sinks.many().replay().all();
//        eventsList.stream().forEach(s -> sink.tryEmitNext(s));
    }
    
//    public void addEvent(String event) {
//        sink.tryEmitNext(event);
//    }
    public void addEventToChat(String event, String chatName) {
        chats.get(chatName).tryEmitNext(event);
    }

//    public Flux<String> registerChat() {
//        return sink.asFlux();
//    }
    public Flux<String> registerChat(String chatName) {
        Sinks.Many<String> sink;
        if(chats.isEmpty()) {                       // INIT CHAT IN DOWNLOAD PAGE
            sink = Sinks.many().replay().all();
            System.out.println("sink = "+sink);
            System.out.println("sink.anme = "+sink.name());
//            sink = chats.put(chatName, Sinks.many().replay().all());
            chats.put(chatName, sink);
//            eventsList.stream().forEach(s -> sink.tryEmitNext(s));
            sink.tryEmitNext("Hello ");
            sink.tryEmitNext("My ");
            sink.tryEmitNext("Friend!");
        }
        sink = chats.get(chatName);
//        return chats.get(chatName).asFlux();
        return sink.asFlux();
    }
//    public Flux<String> createNewChat(String chatName) {
//        chats.put(chatName, Sinks.many().replay().all());
//        return registerChat(chatName);
//    }
}
