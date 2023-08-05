package codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.contoller;

import codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.contoller.request.IssueCreateRequest;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.contoller.request.IssueStatusRequest;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.contoller.response.ApiResponse;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.contoller.response.FilterResponse;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.service.IssueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class IssueController {

    private final IssueService issueService;

    @GetMapping("/api/issues/open")
    public FilterResponse readOpenIssues() {
        return FilterResponse.from(issueService.readOpenIssues());
    }

    @GetMapping("/api/issues/closed")
    public FilterResponse readCloseIssues() {
        return FilterResponse.from(issueService.readClosedIssues());
    }

    @PatchMapping("/api/issues")
    public ApiResponse updateIssuesStatus(@RequestBody IssueStatusRequest request) {
        issueService.updateIssuesStatus(IssueStatusRequest.to(request));
        return ApiResponse.success(HttpStatus.OK);
    }

    @PostMapping("/api/issues")
    public ApiResponse create(@RequestBody IssueCreateRequest request) {
        issueService.createIssue(IssueCreateRequest.to(request));
        return ApiResponse.success(HttpStatus.OK);
    }

    @DeleteMapping("/api/issues/{issueId}")
    public ApiResponse delete(@PathVariable Long issueId) {
        issueService.deleteIssue(issueId);
        return ApiResponse.success(HttpStatus.OK);
    }

    @PatchMapping("/api/issues/{issueId}")
    public ApiResponse updateIssueStatus(@PathVariable Long issueId, @RequestBody IssueStatusRequest request) {
        issueService.updateIssuesStatus(IssueStatusRequest.to(issueId, request));
        return ApiResponse.success(HttpStatus.OK);
    }
}
