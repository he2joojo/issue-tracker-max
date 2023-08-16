package codesquad.kr.gyeonggidoidle.issuetracker.domain.auth.service.information;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.auth.entity.Jwt;
import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtLoginInformation {

    private final String profile;
    private final Jwt jwt;
    @Builder
    private JwtLoginInformation(String profile, Jwt jwt) {
        this.profile = profile;
        this.jwt = jwt;
    }

    public static JwtLoginInformation from(String profile, Jwt jwt) {
        return JwtLoginInformation.builder()
                .profile(profile)
                .jwt(jwt)
                .build();
    }
}
