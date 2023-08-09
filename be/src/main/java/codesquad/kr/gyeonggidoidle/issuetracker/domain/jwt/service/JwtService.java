package codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.service;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.JwtAuthorizationFilter;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.controller.response.JwtTokenType;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.entity.Jwt;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.entity.JwtProvider;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.repository.JwtRepository;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.service.condition.LoginCondition;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.service.condition.SignUpCondition;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.member.Member;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.member.repository.MemberRepository;
import codesquad.kr.gyeonggidoidle.issuetracker.exception.IllegalJwtTokenException;
import codesquad.kr.gyeonggidoidle.issuetracker.exception.IllegalPasswordException;
import codesquad.kr.gyeonggidoidle.issuetracker.exception.MemberDuplicationException;
import io.jsonwebtoken.Claims;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final JwtRepository jwtRepository;
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public Jwt login(LoginCondition condition) {
        Member member = memberRepository.findBy(condition.getEmail());
        if (!verifyPassword(member, condition.getPassword())) {
            throw new IllegalPasswordException();
        }
        Jwt jwt = jwtProvider.createJwt(generateMemberClaims(member));
        jwtRepository.saveRefreshToken(jwt.getRefreshToken(), member.getId());
        return jwt;
    }

    @Transactional
    public void signUp(SignUpCondition condition) {
        String email = condition.getEmail();
        if (existMember(memberRepository.findBy(email))) {
            throw new MemberDuplicationException(email);
        }
        memberRepository.saveMember(SignUpCondition.to(condition));
    }

    @Transactional
    public Jwt reissueAccessToken(String refreshToken) {
        Member member = jwtRepository.findByRefreshToken(refreshToken);
        if (member == null) {
            throw new IllegalJwtTokenException(JwtTokenType.REFRESH);
        }
        return jwtProvider.reissueAccessToken(generateMemberClaims(member), refreshToken);
    }

    public void logout(HttpServletRequest request) {
        String token = JwtAuthorizationFilter.getToken(request);
        Claims claims = jwtProvider.getClaims(token);
        Long memberId = Long.parseLong(claims.get("memberId").toString());
        if (!jwtRepository.deleteRefreshToken(memberId)) {
            throw new IllegalJwtTokenException(JwtTokenType.REFRESH);
        }
    }

    private Map<String, Object> generateMemberClaims(Member member) {
        return Map.of("memberId", member.getId());
    }

    private boolean existMember(Member member) {
        return member != null;
    }

    private boolean verifyPassword(Member member, String password) {
        return existMember(member) && member.getPassword().equals(password);
    }
}
