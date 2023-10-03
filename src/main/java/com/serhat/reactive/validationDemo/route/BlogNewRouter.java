package com.serhat.reactive.validationDemo.route;

import com.serhat.reactive.validationDemo.handler.GlobalRequestValidationHandler;
import com.serhat.reactive.validationDemo.request.BlogRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class BlogNewRouter {
    @Autowired
    GlobalRequestValidationHandler reqValidator;

    @Bean
    public RouterFunction<ServerResponse> BlogNewRouter() {
        return RouterFunctions.route()
                .POST("blogNew", this::blogNewHandler)
                .build();
    }

    Mono<ServerResponse> blogNewHandler(ServerRequest request) {
        return request.bodyToMono(BlogRequest.class)
                .flatMap(this.reqValidator::validateRequest)
                .flatMap(reqObject -> {
                    return ServerResponse.ok().bodyValue(reqObject);
                });
    }
}
