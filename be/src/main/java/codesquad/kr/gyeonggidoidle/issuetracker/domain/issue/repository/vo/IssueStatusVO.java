package codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.repository.vo;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IssueStatusVO {

    private final boolean isOpen;
    private final List<Long> issueIds;

    @Builder
    private IssueStatusVO(boolean isOpen, List<Long> issueIds) {
        this.isOpen = isOpen;
        this.issueIds = issueIds;
    }
}
