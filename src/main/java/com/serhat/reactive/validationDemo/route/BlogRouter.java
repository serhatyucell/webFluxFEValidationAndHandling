package com.serhat.reactive.validationDemo.route;

import com.serhat.reactive.validationDemo.exception.BadRequestException;
import com.serhat.reactive.validationDemo.request.BlogRequest;
import com.serhat.reactive.validationDemo.response.BadRequestResponse;
import com.serhat.reactive.validationDemo.validator.GlobalRequestValidator;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class BlogRouter extends AbstractRouter {

    @Autowired
    GlobalRequestValidator reqValidator;

    @Bean
    public RouterFunction<ServerResponse> BlogRouter() {
        return RouterFunctions.route()
                .POST("blog", this::blogHandler)
                .onError(BadRequestException.class, this::handleException)
                .build();
    }

    Mono<ServerResponse> blogHandler(ServerRequest request) {
        return request.bodyToMono(BlogRequest.class)
                .flatMap(this.reqValidator::validateRequest)
                .flatMap(reqObject -> {
                    return ServerResponse.ok().bodyValue(reqObject);
                });
    }
}
