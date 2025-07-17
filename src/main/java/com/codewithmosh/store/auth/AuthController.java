package com.codewithmosh.store.auth;

import com.codewithmosh.store.users.UserDto;
import com.codewithmosh.store.users.UsersMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UsersMapper usersMapper;
    private final JwtConfig jwtConfig;
    private final AuthService authService;

    @Autowired
    public AuthController(UsersMapper usersMapper, JwtConfig jwtConfig, AuthService authService) {
        this.usersMapper = usersMapper;
        this.jwtConfig = jwtConfig;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ){

        var loginResult = authService.login(request);
        var refreshToken = loginResult.getRefreshToken().toString();
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(loginResult.getAccessToken().toString()));
    }

    @PostMapping("/refresh")
    ResponseEntity<JwtResponse> refresh(
            @CookieValue(value = "refreshToken") String refreshToken){
        var accessToken = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        var user = authService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        var userDto = usersMapper.toDto(user);

        return ResponseEntity.ok(userDto);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentials(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
