package codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.controller.request;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.service.condition.SignUpCondition;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpRequest {

    private final String email;
    private final String password;
    private final String profile;

    @Builder
    private SignUpRequest(String email, String password, String profile) {
        this.email = email;
        this.password = password;
        this.profile = profile;
    }

    public static SignUpCondition to(SignUpRequest request) {
        return SignUpCondition.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .profile(request.getProfile())
                .build();
    }
}
