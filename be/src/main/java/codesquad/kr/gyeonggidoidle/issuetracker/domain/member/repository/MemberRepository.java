package codesquad.kr.gyeonggidoidle.issuetracker.domain.member.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
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

    public void addIssueAssignees(Long issueId, List<Long> assigneeIds) {
        String sql = "INSERT INTO issue_assignee (issue_id, assignee_id) VALUES (:issue_id, :assignee_id)";
        SqlParameterSource[] batchParams = generateParameters(issueId, assigneeIds);
        template.batchUpdate(sql, batchParams);
    }

    public void updateIssueAssignees(Long issueId, List<Long> assigneeIds) {
        String sql = "DELETE FROM issue_assignee WHERE issue_id = :issueId";
        template.update(sql, Map.of("issueId", issueId));
        addIssueAssignees(issueId, assigneeIds);
    }

    private SqlParameterSource[] generateParameters(Long issueId, List<Long> assigneeIds) {
        return assigneeIds.stream()
                .map(assigneeId -> generateParameter(issueId, assigneeId))
                .toArray(SqlParameterSource[]::new);
    }

    private SqlParameterSource generateParameter(Long issueId, Long assigneeId) {
        return new MapSqlParameterSource()
                .addValue("issue_id", issueId)
                .addValue("assignee_id", assigneeId);
    }
}
