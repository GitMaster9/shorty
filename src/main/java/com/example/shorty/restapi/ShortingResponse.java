package com.example.shorty.restapi;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ShortingResponse {
    private String shortUrl;
    private String description;
}
