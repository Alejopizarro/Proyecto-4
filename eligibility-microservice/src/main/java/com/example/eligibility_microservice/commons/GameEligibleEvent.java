package com.example.eligibility_microservice.commons;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GameEligibleEvent {
    private Integer gameId;
    private String name;
    private Integer userId;
    private Boolean isEligible;
}
