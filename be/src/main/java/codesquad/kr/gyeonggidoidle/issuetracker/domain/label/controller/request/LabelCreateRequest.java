package codesquad.kr.gyeonggidoidle.issuetracker.domain.label.controller.request;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.label.service.condition.LabelCreateCondition;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LabelCreateRequest {

    private final String name;
    private final String description;
    private final String backgroundColor;
    private final String textColor;

    @Builder
    private LabelCreateRequest(String name, String description, String backgroundColor, String textColor) {
        this.name = name;
        this.description = description;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    public static LabelCreateCondition to(LabelCreateRequest request) {
        return LabelCreateCondition.builder()
                .name(request.getName())
                .description(request.getDescription())
                .backgroundColor(request.getBackgroundColor())
                .textColor(request.getTextColor())
                .build();
    }
}
