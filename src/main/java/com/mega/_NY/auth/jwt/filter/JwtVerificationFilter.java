package com.mega._NY.auth.jwt.filter;

import com.mega._NY.auth.jwt.JwtToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtToken jwtToken;

    // 로그아웃된 토큰을 저장하는 Set (임시 메모리 저장소) => 회의 후 추후에 변경해야 함!
    private static final Set<String> LOGGED_OUT_TOKENS = new HashSet<>();

    //클레임을 추출해서 Auth~에 저장하는 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("doFilterInternal 메서드");
        try {
            Map<String, Object> claims = jwtToken.verifyJws(request); //클레임 추출
            setAuthtoContext(claims); //Authentication에 저장

        } catch (InsufficientAuthenticationException e) {
            log.error(InsufficientAuthenticationException.class.getSimpleName());

        } catch (MalformedJwtException e1) {
            log.error(MalformedJwtException.class.getSimpleName());

        } catch (ExpiredJwtException e1) {
            log.error(ExpiredJwtException.class.getSimpleName());

        } catch (Exception e1) {
            log.error(Exception.class.getSimpleName());

        }

        filterChain.doFilter(request, response); // 완료되면 다음 필터로 이동
    }

    //토큰 확인 후 예외 처리
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        log.info("shouldNotFilter 진입");
        String authorization = request.getHeader("Authorization"); // Authorization의 밸류값 획득

        if (authorization == null) {
            log.error(NullPointerException.class.getSimpleName());
            return true; // true면 예외 처리가 된다.
        }

        if (!authorization.startsWith("Bearer ")) {
            log.error(MalformedJwtException.class.getSimpleName());
            return true;
        }

        // 로그아웃 됐을 때 토큰의 권한이 없어졌는지 확인
        if (notValidatedToken(request)) {
            log.error(ExpiredJwtException.class.getSimpleName());
            return true;
        }

        return false;
    }

    // 토큰이 로그아웃된 토큰인지 확인하는 메서드
    public boolean notValidatedToken(HttpServletRequest request) {
        String jws = jwtToken.extractJws(request); //토큰에서 Bearer 제거
        return LOGGED_OUT_TOKENS.contains(jws);
    }

    // 권한을 SecurityContextHolder에 저장하는 메서드
    private void setAuthtoContext(Map<String, Object> claims) {
        String username = (String) claims.get("username"); // 해당 키값의 밸류 추출(이메일)
        List<String> roles = (List<String>) claims.get("roles"); //해당 키값의 밸류 추출(역할)
        List<GrantedAuthority> authorities = roles.stream() //추출한 역할을 바탕으로 권한생성
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities); //authentication에 유저네임과 권한 저장
        SecurityContextHolder.getContext().setAuthentication(authentication); //security~~에 저장

        log.info("sch= {}", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    // 로그아웃된 토큰을 저장하는 메서드 (예제에서 추가)
    public static void addLoggedOutToken(String token) {
        LOGGED_OUT_TOKENS.add(token);
    }
}
