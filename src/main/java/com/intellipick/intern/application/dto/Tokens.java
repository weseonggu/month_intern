package com.intellipick.intern.application.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonFilter("TokenFilter")
public class Tokens {
    private String access_token;
    private String refresh_token;
}
