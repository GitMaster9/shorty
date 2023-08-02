package com.example.shorty.restapi;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ShortingRequest {
    private String accountId;
    private String password;
    private String url;
    private String redirectType;
}
