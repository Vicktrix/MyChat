package com.MyChat;

import java.time.Duration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

public class MyWebClient {

    private static WebClient client;
    
    public static void main(String[] args) {
        
        client = WebClient.create("http://localhost:8080");
        String url = "/chat/123";
        
        Flux<String> just = Flux.just("Test", "post", "message", "to", "adding", "into", "our", "server", ":)");
        
        just.delayElements(Duration.ofMillis(500)).collectList().block().stream().forEachOrdered(s -> postEmit(url, s));
    }
    
    public static void postEmit(String url, String message) {
        System.out.println("\tFrom client PostEmit!" + message);
        client.post()
//                .uri(url)
                .uri("/chat/post/chat1")
                .contentType(MediaType.TEXT_PLAIN)
                .bodyValue(message)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
    
    public static void getMessage(String url) {
        client.get()
                .uri(url)
                .retrieve()
                .bodyToFlux(String.class)
                .doOnNext(m -> System.out.println("We receive "+m))
                .collectList().block().forEach(System.out::println);
    }
    private static void sleep() {
        try {
            Thread.sleep(Duration.ofMillis(20000));
        } catch (InterruptedException ex) {
            System.out.println("Error inside sleep!!! \n"+ex);
        }
    }
}
