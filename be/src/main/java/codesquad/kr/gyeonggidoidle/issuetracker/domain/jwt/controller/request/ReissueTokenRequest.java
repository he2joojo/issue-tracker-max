package codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.controller.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReissueTokenRequest {

    private final String refreshToken;

    @Builder
    private ReissueTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
