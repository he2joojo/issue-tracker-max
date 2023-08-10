package codesquad.kr.gyeonggidoidle.issuetracker.domain.milestone.controller.request;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.milestone.service.condition.MilestoneCreateCondition;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MilestoneCreateRequest {

    private final String name;
    private final String description;
    private final LocalDate dueDate;

    @Builder
    private MilestoneCreateRequest(String name, String description, LocalDate dueDate) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
    }

    public static MilestoneCreateCondition to(MilestoneCreateRequest request) {
        return MilestoneCreateCondition.builder()
                .name(request.getName())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .build();
    }
}
