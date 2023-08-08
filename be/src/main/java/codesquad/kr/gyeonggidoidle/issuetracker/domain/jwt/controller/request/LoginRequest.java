package codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.controller.request;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.service.condition.LoginCondition;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequest {

    private final String email;
    private final String password;

    @Builder

    private LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static LoginCondition to(LoginRequest request) {
        return LoginCondition.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }
}
