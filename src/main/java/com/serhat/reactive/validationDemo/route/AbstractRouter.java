package com.serhat.reactive.validationDemo.route;

import com.serhat.reactive.validationDemo.exception.BadRequestException;
import com.serhat.reactive.validationDemo.response.BadRequestResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

abstract class AbstractRouter {

    Mono<ServerResponse> handleException(Throwable error, ServerRequest request) {
        BadRequestResponse resp = new BadRequestResponse(((BadRequestException) error).getErrorList());
        return ServerResponse.badRequest().bodyValue(resp);
    }

}
