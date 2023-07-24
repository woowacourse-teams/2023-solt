package dev.tripdraw.presentation.member;

import static dev.tripdraw.exception.auth.AuthExceptionType.NO_AUTH_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.tripdraw.dto.auth.LoginUser;
import dev.tripdraw.exception.auth.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthExtractorTest {

    @Test
    void 요청의_헤더에서_LoginUser를_추출한다() {
        // given
        AuthExtractor authExtractor = new AuthExtractor(new Base64Decoder());
        HttpServletRequest request = mock(HttpServletRequest.class);
        String encoded = "7Ya17ZuE7LaU";
        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(encoded);

        // when
        LoginUser loginUser = authExtractor.extract(request);

        // then
        assertThat(loginUser.nickname()).isEqualTo("통후추");
    }

    @Test
    void 요청의_헤더에_인증_정보가_없을_경우_예외를_발생시킨다() {
        // given
        AuthExtractor authExtractor = new AuthExtractor(new Base64Decoder());
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader(HttpServletRequest.BASIC_AUTH)).thenReturn(null);

        // expect
        assertThatThrownBy(() -> authExtractor.extract(request))
                .isInstanceOf(AuthException.class)
                .hasMessage(NO_AUTH_HEADER.getMessage());
    }
}
