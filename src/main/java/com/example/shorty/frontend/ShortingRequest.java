package com.example.shorty.frontend;

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
