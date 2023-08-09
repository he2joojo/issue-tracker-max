package codesquad.kr.gyeonggidoidle.issuetracker.domain.label.controller;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.contoller.response.ApiResponse;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.label.controller.request.LabelCreateRequest;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.label.controller.response.LabelPageResponse;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.label.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class LabelController {

    private final LabelService labelService;

    @GetMapping("/api/labels")
    public LabelPageResponse readLabelPage() {
        return LabelPageResponse.from(labelService.readLabelPage());
    }

    @PostMapping("/api/labels")
    public ApiResponse create(@RequestBody LabelCreateRequest request) {
        labelService.create(LabelCreateRequest.to(request));
        return ApiResponse.success(HttpStatus.OK);
    }
}
