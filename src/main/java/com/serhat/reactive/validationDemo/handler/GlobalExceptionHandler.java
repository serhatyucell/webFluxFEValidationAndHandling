package com.serhat.reactive.validationDemo.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serhat.reactive.validationDemo.exception.BadRequestException;
import com.serhat.reactive.validationDemo.response.BadRequestResponse;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalExceptionHandler implements WebExceptionHandler {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    //Mono.error(ex) her call edildiğinde handle metodu otomatik olarak çağırılacaktır.
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        //Mono.error(ex) içerisinde BadRequestException taşıyorsa, bunun için bir kırılım olsun.
        if (ex instanceof BadRequestException) {
            BadRequestException badReqEx = (BadRequestException) ex;
            //exchange içerisindeki response body'yi override etmek için DataBuffer gerekiyor
            //Ayrı bir metod içerisinde halledelim.
            return writeResponseBody(exchange, new BadRequestResponse(badReqEx.getErrorList()));
        }

        //Diğer tüm exception hataları için HTTP statusu 500 olarak setleyip response dönebiliriz.
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return exchange.getResponse().setComplete();

    }

    private Mono<Void> writeResponseBody(ServerWebExchange exchange, Object requestObject) {
        try {
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(objectMapper.writeValueAsBytes(requestObject));
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return exchange.getResponse().writeWith(Mono.just(dataBuffer));
        } catch (Exception e) {
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return exchange.getResponse().setComplete();
    }

}
