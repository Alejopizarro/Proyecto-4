package com.example.gateway_service.service;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    private final RouterValidator validator;
    private final JwtUtils jwtUtils;

    public AuthenticationFilter(RouterValidator validator, JwtUtils jwtUtils) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            var request = exchange.getRequest();
            var path = request.getURI().getPath();

            ServerHttpRequest serverHttpRequest = null;
            if (validator.isSecured.test(request)) {
                System.out.println("Protected route: " + path);
                if (authMissing(request)) {
                    System.out.println("Authorization header missing for route: " + path);
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                } else {
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                if (jwtUtils.isExpired(authHeader)) {
                    return onError(exchange, HttpStatus.UNAUTHORIZED);
                }

                serverHttpRequest = exchange.getRequest()
                        .mutate()
                        .header("userIdRequest", jwtUtils.extractUserId(authHeader).toString())
                        .build();
            }

            return chain.filter(exchange.mutate().request(serverHttpRequest).build());
        }));
    }

    private Mono<Void> onError(ServerWebExchange exchange, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return null;
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION);
    }

    public static class Config {}
}
