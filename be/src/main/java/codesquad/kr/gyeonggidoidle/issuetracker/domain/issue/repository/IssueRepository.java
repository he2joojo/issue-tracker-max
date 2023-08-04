package codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.repository;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.Issue;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.repository.vo.IssueStatusVO;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.repository.vo.IssueVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;


@Repository
public class IssueRepository {

    private final NamedParameterJdbcTemplate template;

    @Autowired
    public IssueRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<IssueVO> findOpenIssues() {
        String sql = "SELECT i.id, " +
                "m.name AS milestone_name, " +
                "me.name AS author_name, " +
                "i.title, " +
                "i.created_at " +
                "FROM issue i " +
                "LEFT JOIN member me ON i.author_id = me.id " +
                "LEFT JOIN milestone m ON i.milestone_id = m.id " +
                "WHERE i.is_open = true " +
                "ORDER BY i.id DESC";

        return template.query(sql, issueVOMapper);
    }

    public List<IssueVO> findClosedIssues() {
        String sql = "SELECT i.id, " +
                "m.name AS milestone_name, " +
                "me.name AS author_name, " +
                "i.title, " +
                "i.created_at " +
                "FROM issue i " +
                "LEFT JOIN member me ON i.author_id = me.id " +
                "LEFT JOIN milestone m ON i.milestone_id = m.id " +
                "WHERE i.is_open = false " +
                "ORDER BY i.id DESC";

        return template.query(sql, issueVOMapper);
    }

    public void updateIssuesStatus(IssueStatusVO vo) {
        String sql = "UPDATE issue SET is_open = :is_open WHERE id IN (:issueIds)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("is_open", vo.isOpen())
                .addValue("issueIds", vo.getIssueIds());
        template.update(sql, params);
    }

    public Long createIssue(Issue issue) {
        String sql = "INSERT INTO issue (author_id, milestone_id, title) VALUES (:author_id, :milestone_id, :title)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("author_id", issue.getAuthorId())
                .addValue("milestone_id", issue.getMilestoneId())
                .addValue("title", issue.getTitle());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, params, keyHolder, new String[]{"id"});
        return keyHolder.getKey().longValue();
    }

    private final RowMapper<IssueVO> issueVOMapper = (rs, rowNum) -> IssueVO.builder()
            .id(rs.getLong("id"))
            .author(rs.getString("author_name"))
            .milestone(rs.getString("milestone_name"))
            .title(rs.getString("title"))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .build();
}
