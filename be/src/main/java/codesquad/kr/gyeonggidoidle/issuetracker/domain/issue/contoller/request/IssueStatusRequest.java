package codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.contoller.request;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.service.condition.IssueStatusCondition;
import java.util.List;
import lombok.Builder;
import lombok.Getter;


@Getter
public class IssueStatusRequest {

    private final boolean isOpen;
    private final List<Long> issues;


    @Builder
    private IssueStatusRequest(boolean isOpen, List<Long> issues) {
        this.isOpen = isOpen;
        this.issues = issues;
    }

    public static IssueStatusCondition to(IssueStatusRequest request) {
        return IssueStatusCondition.builder()
                .isOpen(request.isOpen)
                .issueIds(request.getIssues())
                .build();
    }
}
