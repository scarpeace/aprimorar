package aprimorar.auth;

import aprimorar.auth.dto.LoginRequest;
import aprimorar.auth.dto.LoginResponse;
import aprimorar.auth.dto.LoginResult;
import aprimorar.auth.exception.AuthException;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final boolean cookieSecure;
    private final long refreshTokenDays;

    public AuthController(
        AuthService authService,
        @Value("${app.auth.cookie-secure}") boolean cookieSecure,
        @Value("${app.auth.refresh-token-days}") long refreshTokenDays
    ) {
        this.authService = authService;
        this.cookieSecure = cookieSecure;
        this.refreshTokenDays = refreshTokenDays;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResult result = authService.login(request);
        ResponseCookie cookie = createRefreshCookie(result.refreshToken());

        return ResponseEntity
            .ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(new LoginResponse(result.accessToken()));
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(
        @CookieValue(value = "refresh_token", required = false) String refreshToken
    ) {
        if (refreshToken == null) {
            throw new AuthException("Invalid refresh token");
        }

        return new LoginResponse(authService.refresh(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
        @CookieValue(value = "refresh_token", required = false) String refreshToken
    ) {
        if (refreshToken != null) {
            authService.logout(refreshToken);
        }

        return ResponseEntity
            .noContent()
            .header(HttpHeaders.SET_COOKIE, deleteRefreshCookie().toString())
            .build();
    }

    private ResponseCookie createRefreshCookie(String refreshToken) {
        return ResponseCookie
            .from("refresh_token", refreshToken)
            .httpOnly(true)
            .secure(cookieSecure)
            .sameSite("Lax")
            .path("/auth")
            .maxAge(Duration.ofDays(refreshTokenDays))
            .build();
    }

    private ResponseCookie deleteRefreshCookie() {
        return ResponseCookie
            .from("refresh_token", "")
            .httpOnly(true)
            .secure(cookieSecure)
            .sameSite("Lax")
            .path("/auth")
            .maxAge(Duration.ZERO)
            .build();
    }
}
