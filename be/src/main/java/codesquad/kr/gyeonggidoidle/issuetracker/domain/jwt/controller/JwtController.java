package codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.controller;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.controller.request.LoginRequest;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.controller.request.ReissueTokenRequest;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.controller.request.SignUpRequest;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.controller.response.JwtResponse;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.service.JwtService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class JwtController {

    private final JwtService jwtService;

    @PostMapping("/api/login")
    public JwtResponse login(@RequestBody @Valid LoginRequest request) {
        return JwtResponse.from(jwtService.login(LoginRequest.to(request)));
    }

    @PostMapping("/api/signup")
    public void signUp(@RequestBody @Valid SignUpRequest request) {
        jwtService.signUp(SignUpRequest.to(request));
    }

    @PostMapping("/api/auth/reissue")
    public JwtResponse reissueAccessToken(@RequestBody ReissueTokenRequest request) {
        return JwtResponse.from(jwtService.reissueAccessToken(request.getRefreshToken()));
    }
}
