package codesquad.kr.gyeonggidoidle.issuetracker.domain.milestone.controller;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.contoller.response.ApiResponse;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.milestone.controller.request.MilestoneCreateRequest;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.milestone.controller.response.MilestonePageResponse;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.milestone.service.MilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MilestoneController {

    private final MilestoneService milestoneService;

    @GetMapping("/api/milestones/open")
    public MilestonePageResponse readOpenMilestones() {
        return MilestonePageResponse.from(milestoneService.readOpenMilestonePage());
    }

    @GetMapping("/api/milestones/closed")
    public MilestonePageResponse readClosedMilestones() {
        return MilestonePageResponse.from(milestoneService.readClosedMilestonePage());
    }

    @PostMapping("/api/milestones")
    public ApiResponse create(@RequestBody MilestoneCreateRequest request) {
        milestoneService.create(MilestoneCreateRequest.to(request));
        return ApiResponse.success(HttpStatus.OK);
    }
}
