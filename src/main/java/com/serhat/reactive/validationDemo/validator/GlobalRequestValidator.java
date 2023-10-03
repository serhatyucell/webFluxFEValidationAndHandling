package com.serhat.reactive.validationDemo.validator;

import com.serhat.reactive.validationDemo.exception.BadRequestException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class GlobalRequestValidator {
    @Autowired
    Validator validator;

    public <T> Mono<T> validateRequest(T requestObj) {

        //Request objesi null gönderilebilir
        if (requestObj == null) {
            return Mono.error(new BadRequestException("İstek boş gönderilemez."));
        }

        //jakarta validasyonunu kullan
        Set<ConstraintViolation<T>> violatedRules = this.validator.validate(requestObj);

        //Herhangi bir hata yoksa request objesini dön varsa hata dön.
        return (violatedRules == null || violatedRules.isEmpty()) ? Mono.just(requestObj) : Mono.error(new BadRequestException(violatedRules.stream().map(e -> e.getMessage()).toList()));
    }
}
