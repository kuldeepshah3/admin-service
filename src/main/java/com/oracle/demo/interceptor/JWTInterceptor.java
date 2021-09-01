package com.oracle.demo.interceptor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;

    @Value("${jwt.requestParam}")
    private String requestParam;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getParameter(requestParam);
        if (token != null) {
            Claims claims = Jwts.parser().setSigningKey(secret)
                    .parseClaimsJws(token.replace(tokenPrefix, ""))
                    .getBody();
            return issuer.equals(claims.getIssuer());
        }
        return false;
    }

}
