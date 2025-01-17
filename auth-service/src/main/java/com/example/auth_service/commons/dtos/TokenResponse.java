package com.example.auth_service.commons.dtos;

import lombok.*;
import org.hibernate.annotations.SecondaryRow;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenResponse {
    private String accessToken;
}
