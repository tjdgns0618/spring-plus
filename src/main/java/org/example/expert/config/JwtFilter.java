package org.example.expert.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.enums.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (requestURI.startsWith("/ws")) {
            filterChain.doFilter(request,response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            log.info("JWT 토큰이 필요합니다.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰이 필요합니다.");
            return;
        }

        String jwt = jwtUtil.substringToken(authorizationHeader);

        Claims claims;
        // JWT 토큰 유효성 검사
        try {
            claims = jwtUtil.extractClaims(jwt);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
            return;
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
            return;
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
            return;
        } catch (Exception e) {
            log.error("유효하지 않은 JWT 토큰입니다: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"유효하지 않은 토큰입니다.\"}");
            return;
        }

        // JWT payload에 담긴 사용자 정보 꺼내기
        Long userId = Long.parseLong(claims.getSubject());
        String nickname = claims.get("nickname", String.class);
        String email = claims.get("email", String.class);
        UserRole userRole = UserRole.of(claims.get("userRole", String.class));

        // 꺼낸 정보로 AuthUser 객체 생성
        // 이 객체가 컨트롤러에서 @Auth AuthUser authUser 파라미터로 주입되는 실체
        AuthUser authUser = new AuthUser(userId, nickname, email, userRole);

        // AuthUser를 Spring Security의 인증 객체로 감싸서 SecurityContext에 저장
        // - principal: AuthUser (나중에 AuthUserArgumentResolver가 여기서 꺼냄)
        // - credentials: null (JWT 방식에서는 비밀번호 불필요)
        // - authorities: authUser.getAuthorities() → ROLE_ADMIN / ROLE_NORMAL (URL 접근 권한 체크에 사용)
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities())
        );

        filterChain.doFilter(request, response);
    }
}