package dev.tripdraw.presentation.member;

import static dev.tripdraw.exception.ExceptionCode.NO_AUTH_HEADER;
import static jakarta.servlet.http.HttpServletRequest.BASIC_AUTH;
import static org.springframework.util.StringUtils.hasText;

import dev.tripdraw.dto.LoginUser;
import dev.tripdraw.exception.BadRequestException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthExtractor {

    private final BasicAuthorizationDecoder basicAuthorizationDecoder;

    public LoginUser extract(HttpServletRequest request) {
        String authorization = request.getHeader(BASIC_AUTH);
        if (!hasText(authorization)) {
            throw new BadRequestException(NO_AUTH_HEADER);
        }

        String nickname = basicAuthorizationDecoder.decode(authorization);

        return new LoginUser(nickname);
    }
}
