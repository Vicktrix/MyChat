package com.MyChat.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class MySavedChats {

    private Map<String, Sinks.Many<ChatMessage>> users = new HashMap<>();
    private Map<String, MyUser> userHasChat = new HashMap<>();
    private Map<String, List<String>> chatInUsers = new HashMap<>();
    
    public MySavedChats() {             // init 4 user, as frontend has
                                // TODO - create users from front
        List<String> initChat = Arrays.asList("chat1","chat2","chat3","chat4");
        createUser("user1",new ArrayList<>(initChat));
        createUser("user2",new ArrayList<>(initChat));
        createUser("user3",new ArrayList<>(initChat));
        createUser("user4",new ArrayList<>(initChat));
    }
    public Flux<ChatMessage> getUser(String user) {
        return users.get(user).asFlux();
    }
    public Flux<String> getUsers() {
        return Flux.fromIterable(userHasChat.keySet());
    }
    public void write(String user, String chat, String message) {
//        users.get(user).tryEmitNext(new ChatMessage(chat,user+" : "+message));
        chatInUsers.get(chat).forEach(u -> 
        users.get(u).tryEmitNext(new ChatMessage(chat,user+" : "+message)));
    }
    public Flux<String> getFluxChats(String name) {
        return Flux.fromIterable(userHasChat.get(name).list());
    }
    private void createUser(String userName, List<String> chatList) {
        users.put(userName, Sinks.many().replay().all());
        userHasChat.put(userName, new MyUser(userName, chatList));
        fillChatInUsers(userName, chatList);
    };
    private void fillChatInUsers(String userName, List<String> chatList) {
    chatList.forEach(chat -> 
            chatInUsers.computeIfAbsent(chat, k -> new ArrayList<>())
        .add(userName));
    }
}