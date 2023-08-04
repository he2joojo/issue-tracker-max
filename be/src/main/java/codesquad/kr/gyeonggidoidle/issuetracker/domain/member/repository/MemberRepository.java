package codesquad.kr.gyeonggidoidle.issuetracker.domain.member.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class MemberRepository {

    private final NamedParameterJdbcTemplate template;

    @Autowired
    public MemberRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

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

    public void updateIssueAssignees(Long issueId, List<Long> assigneeIds) {
        String sql = "INSERT INTO issue_assignee (issue_id, assignee_id) VALUES ";
        List<MapSqlParameterSource> batchParams = assigneeIds.stream()
                .map(assignId -> new MapSqlParameterSource()
                        .addValue("issueId", issueId)
                        .addValue("assignee_id", assignId))
                .collect(Collectors.toList());
        template.batchUpdate(sql, batchParams.toArray(new MapSqlParameterSource[0]));
    }
}
