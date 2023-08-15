package codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OauthTokenResponse {
    private String accessToken;
    private String scope;
    private String tokenType;

    @Builder
    public OauthTokenResponse(String accessToken, String scope, String tokenType) {
        this.accessToken = accessToken;
        this.scope = scope;
        this.tokenType = tokenType;
    }
}
