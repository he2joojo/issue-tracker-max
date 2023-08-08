package codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.repository;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.member.Member;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JwtRepository {

    private final NamedParameterJdbcTemplate template;

    public JwtRepository(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public Member findByRefreshToken(String refreshToken) {
        String sql = "SELECT member.id, member.email, member.name, member.password, member.profile "
                + "FROM member "
                + "JOIN refresh_token "
                + "ON member.id = refresh_token.member_id "
                + "WHERE refresh_token.refresh_token = :refreshToken";
        try {
            return template.queryForObject(sql, Map.of("refreshToken", refreshToken), memberRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void saveRefreshToken(String refreshToken, Long memberId) {
        String sql = "INSERT INTO refresh_token(refresh_token, member_id) VALUES(:refreshToken, :memberId) "
                + "ON DUPLICATE KEY UPDATE refresh_token = :refreshToken";
        template.update(sql, Map.of("refreshToken", refreshToken, "memberId", memberId));
    }

    private final RowMapper<Member> memberRowMapper() {
        return ((rs, rowNum) -> Member.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .password(rs.getString("password"))
                .profile(rs.getString("profile"))
                .build());
    }
}
