package com.serhat.reactive.validationDemo.request;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogRequest {

    @NotBlank(message="Title can not be null.")
    private String title;

    @NotBlank(message="Content can not be null.")
    private String content;
}
