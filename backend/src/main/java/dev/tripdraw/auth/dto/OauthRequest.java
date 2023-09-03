package dev.tripdraw.auth.dto;

import dev.tripdraw.common.auth.OauthType;
import io.swagger.v3.oas.annotations.media.Schema;

public record OauthRequest(

        @Schema(description = "소셜 로그인 타입", example = "KAKAO")
        OauthType oauthType,

        @Schema(description = "Access Token", example = TOKEN_SAMPLE)
        String oauthToken
) {

    private static final String TOKEN_SAMPLE = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMzMiLCJleHAiOjE2OTE4OTA0NjZ9.NlzZEEDWdjovafRPCD1ZvddeRUccZfyZpcUWzGPAI4oK-PKyPM64TIMJ3HyOy29vJtg_MET1c4omWUkGOb4qyQ";
}
