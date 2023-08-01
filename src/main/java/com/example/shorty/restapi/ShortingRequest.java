package com.example.shorty.restapi;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ShortingRequest {
    private String accountId;
    private String password;
    private String url;
    private String redirectType;
}
