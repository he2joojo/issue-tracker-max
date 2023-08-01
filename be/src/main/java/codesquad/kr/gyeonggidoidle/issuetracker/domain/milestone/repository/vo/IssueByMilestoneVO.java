package codesquad.kr.gyeonggidoidle.issuetracker.domain.milestone.repository.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class IssueByMilestoneVO {

    private final Integer openIssueCount;
    private final Integer closedIssueCount;

    @Builder
    public IssueByMilestoneVO(Integer openIssueCount, Integer closedIssueCount) {
        this.openIssueCount = openIssueCount;
        this.closedIssueCount = closedIssueCount;
    }
}
