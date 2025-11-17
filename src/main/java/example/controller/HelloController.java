package example.controller;

import java.util.concurrent.Callable;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Tags(value = { @Tag(name = "hello", description = "Say hello.") })
@RestController
public class HelloController {
    private final ApplicationEventPublisher applicationEventPublisher;
    
    public HelloController(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Builder
    @AllArgsConstructor
    @Schema(title= "Hello", description = "The hello response.")
    public static class HelloResource {
        @Getter
        private String text;
        @Getter
        private String language;
    }

    @GetMapping(value = "/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                    schema = @Schema(implementation = HelloResource.class))))
    public ResponseEntity<HelloResource> hello() {
        return ResponseEntity.ok(HelloResource.builder().text("Hello").language("English").build());
    }

    @GetMapping(value = "/call", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200", 
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                    schema = @Schema(implementation = HelloResource.class))))
    public Callable<ResponseEntity<HelloResource>> call() {
        System.out.println("--HelloController.call() start--");
        System.out.println(Thread.currentThread().getName());
        System.out.println(TransactionSynchronizationManager.getResourceMap());
        System.out.println("--HelloController.call() end--");
        return () -> {
            System.out.println("--HelloController.callableLambda() start--");
            System.out.println(Thread.currentThread().getName());
            System.out.println(TransactionSynchronizationManager.getResourceMap());
            applicationEventPublisher.publishEvent(new TestEvent());
            System.out.println("--HelloController.callableLambda() end--");
            return ResponseEntity.ok(HelloResource.builder().text("Call").language("English").build());
        };
    }
}
