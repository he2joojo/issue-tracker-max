package codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.service.condition;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.repository.vo.IssueStatusVO;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IssueStatusCondition {

    private final boolean open;
    private final List<Long> issueIds;

    @Builder
    private IssueStatusCondition(boolean isOpen, List<Long> issueIds) {
        this.open = isOpen;
        this.issueIds = issueIds;
    }

    public static IssueStatusVO to(IssueStatusCondition condition) {
        return IssueStatusVO.builder()
                .isOpen(condition.isOpen())
                .issueIds(condition.getIssueIds())
                .build();
    }
}
