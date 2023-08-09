package codesquad.kr.gyeonggidoidle.issuetracker.domain.member.repository;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.member.Member;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.member.repository.vo.MemberDetailsVO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

    private final NamedParameterJdbcTemplate template;

    public Map<Long, List<String>> findAllProfilesByIssueIds(List<Long> issueIds ) {
        return issueIds.stream()
                .collect(Collectors.toUnmodifiableMap(
                        issueId -> issueId,
                        this::findAllProfilesByIssueId
                ));
    }

    public List<String> findAllProfilesByIssueId(Long issueId) {
        String sql = "SELECT m.profile " +
                    "FROM member AS m " +
                    "JOIN issue_assignee AS ia ON m.id = ia.assignee_id " +
                    "WHERE ia.issue_id = :issueId";

        return template.queryForList(sql, Map.of("issueId", issueId), String.class);
    }

    public List<MemberDetailsVO> findAllFilters() {
        String sql = "SELECT id, name, profile " +
                "FROM member " +
                "ORDER BY name";
        return template.query(sql, new MapSqlParameterSource(), memberDetailsVORowMapper());
    }

    public Member findByEmail(String email) {
        String sql = "SELECT id, email, name, password, profile FROM member WHERE email = :email";
        try {
            return template.queryForObject(sql, Map.of("email", email), memberRowMapper());
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void saveMember(Member member) {
        String sql = "INSERT INTO member(name, email, password, profile) VALUES(:name, :email, :password, :profile) ";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", member.getName())
                .addValue("email", member.getEmail())
                .addValue("password", member.getPassword())
                .addValue("profile", member.getProfile());
        template.update(sql, params);
    }

    private final RowMapper<MemberDetailsVO> memberDetailsVORowMapper() {
        return ((rs, rowNum) -> MemberDetailsVO.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .profile(rs.getString("profile"))
                .build());
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
