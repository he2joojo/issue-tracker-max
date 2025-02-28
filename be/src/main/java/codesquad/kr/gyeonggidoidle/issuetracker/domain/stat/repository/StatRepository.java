package codesquad.kr.gyeonggidoidle.issuetracker.domain.stat.repository;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.stat.repository.vo.IssueByMilestoneVO;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.stat.repository.vo.MilestoneStatVO;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.stat.repository.vo.StatVO;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class StatRepository {

    private final NamedParameterJdbcTemplate template;

    @Autowired
    public StatRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
    }

    public StatVO countOverallStats() {
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM issue WHERE (is_open = TRUE AND is_deleted = FALSE)) AS open_issue_count, " +
                "(SELECT COUNT(*) FROM issue WHERE (is_open = FALSE AND is_deleted = FALSE)) AS closed_issue_count, " +
                "(SELECT COUNT(*) FROM milestone WHERE is_deleted = FALSE) AS milestone_count, " +
                "(SELECT COUNT(*) FROM label WHERE is_deleted = FALSE) AS label_count;";
        SqlParameterSource parameterSource = new MapSqlParameterSource();

        return template.queryForObject(sql, parameterSource, statVORowMapper);
    }

    public MilestoneStatVO countMilestoneStats() {
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM milestone WHERE (is_open = TRUE AND is_deleted = FALSE)) AS open_milestone_count, " +
                "(SELECT COUNT(*) FROM milestone WHERE (is_open = FALSE AND is_deleted = FALSE)) AS closed_milestone_count, " +
                "(SELECT COUNT(*) FROM label WHERE is_deleted = FALSE) AS label_count;";

        return template.queryForObject(sql, new MapSqlParameterSource(), milestoneStatVORowMapper());
    }

    public StatVO countLabelStats() {
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM milestone WHERE is_deleted = FALSE) AS milestone_count, " +
                "(SELECT COUNT(*) FROM label WHERE is_deleted = FALSE) AS label_count";

        return template.queryForObject(sql, new MapSqlParameterSource(), countLabelStatsRowMapper());
    }

    public Map<Long, IssueByMilestoneVO> findIssuesCountByMilestoneIds(List<Long> milestoneIds) {
        return milestoneIds.stream()
                .collect(Collectors.toUnmodifiableMap(
                        milestoneId -> milestoneId, this::findIssuesCountByMilestoneId
                ));
    }

    public IssueByMilestoneVO findIssuesCountByMilestoneId(Long milestoneId) {
        String sql = "SELECT " +
                "(SELECT COUNT(*) FROM milestone AS m LEFT JOIN issue AS i ON m.id = i.milestone_id " +
                "WHERE (i.is_open = TRUE AND i.is_deleted = FALSE AND m.id = :milestoneId)) AS open_issue_count, " +
                "(SELECT COUNT(*) FROM milestone AS m LEFT JOIN issue AS i ON m.id = i.milestone_id " +
                "WHERE (i.is_open = FALSE AND i.is_deleted = FALSE AND m.id = :milestoneId)) AS closed_issue_count";

        return template.queryForObject(sql, Map.of("milestoneId", milestoneId), issueByMilestoneVORowMapper());
    }

    private final RowMapper<StatVO> statVORowMapper = (rs, rowNum) -> StatVO.builder()
            .openIssueCount(rs.getInt("open_issue_count"))
            .closedIssueCount(rs.getInt("closed_issue_count"))
            .milestoneCount((rs.getInt("milestone_count")))
            .labelCount(rs.getInt("label_count"))
            .build();

    private final RowMapper<StatVO> countLabelStatsRowMapper() {
        return ((rs, rowNum) -> StatVO.builder()
                .milestoneCount(rs.getInt("milestone_count"))
                .labelCount(rs.getInt("label_count"))
                .build()
        );
    }

    private final RowMapper<MilestoneStatVO> milestoneStatVORowMapper() {
        return ((rs, rowNum) -> MilestoneStatVO.builder()
                .openMilestoneCount(rs.getInt("open_milestone_count"))
                .closeMilestoneCount(rs.getInt("closed_milestone_count"))
                .labelCount(rs.getInt("label_count"))
                .build());
    }

    private final RowMapper<IssueByMilestoneVO> issueByMilestoneVORowMapper() {
        return ((rs, rowNum) -> IssueByMilestoneVO.builder()
                .openIssueCount(rs.getInt("open_issue_count"))
                .closedIssueCount(rs.getInt("closed_issue_count"))
                .build());
    }
}
