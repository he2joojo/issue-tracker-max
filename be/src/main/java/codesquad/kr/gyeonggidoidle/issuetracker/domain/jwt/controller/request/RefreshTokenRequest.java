package codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.controller.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {

    private final String refreshToken;

    @Builder
    private RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
