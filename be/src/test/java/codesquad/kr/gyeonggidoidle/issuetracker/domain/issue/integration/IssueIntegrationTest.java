package codesquad.kr.gyeonggidoidle.issuetracker.domain.issue.integration;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import codesquad.kr.gyeonggidoidle.issuetracker.annotation.IntegrationTest;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.entity.Jwt;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.jwt.entity.JwtProvider;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@IntegrationTest
public class IssueIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @DisplayName("열린 이슈의 모든 정보를 다 가지고 온다.")
    @Test
    void openIssueIntegrationTest() throws Exception {
        Jwt jwt = makeToken();
        ResultActions resultActions = mockMvc.perform(get("/api/issues/open")
                .header("Authorization", "Bearer " + jwt.getAccessToken()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openIssueCount").value(2))
                .andExpect(jsonPath("$.milestoneCount").value(4))
                .andExpect(jsonPath("$.issues.length()").value(2))
                .andExpect(jsonPath("$.issues.[0].labels.[0].backgroundColor").value("#F08080"))
                .andExpect(jsonPath("$.issues.[1].title").value("제목 1"))
                .andExpect(jsonPath("$.issues.[1].assigneeProfiles.length()").value(2))
                .andDo(print());
    }

    @DisplayName("닫힌 이슈의 모든 정보를 다 가지고 온다.")
    @Test
    void closedIssueIntegrationTest() throws Exception {
        Jwt jwt = makeToken();
        ResultActions resultActions = mockMvc.perform(get("/api/issues/closed")
                .header("Authorization", "Bearer " + jwt.getAccessToken()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.closedIssueCount").value(3))
                .andExpect(jsonPath("$.labelCount").value(4))
                .andExpect(jsonPath("$.issues.length()").value(3))
                .andExpect(jsonPath("$.issues.[0].labels.length()").value(0))
                .andExpect(jsonPath("$.issues.[0].assigneeProfiles.length()").value(0))
                .andExpect(jsonPath("$.issues.[1].labels.[0].name").value("라벨 1"))
                .andDo(print());
    }

    @DisplayName("메인 화면의 필터 목록을 가지고 온다.")
    @Test
    void testReadFilters() throws Exception {
        Jwt jwt = makeToken();
        ResultActions resultActions = mockMvc.perform(get("/api/filters")
                .header("Authorization", "Bearer " + jwt.getAccessToken()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignees.length()").value(4))
                .andExpect(jsonPath("$.authors.length()").value(3))
                .andExpect(jsonPath("$.labels.length()").value(4))
                .andExpect(jsonPath("$.milestones.length()").value(4))
                .andExpect(jsonPath("$.assignees.[0].name").value("담당자가 없는 이슈"))
                .andDo(print());
    }

    @DisplayName("이슈 화면의 필터 목록을 가지고 온다.")
    @Test
    void testReadFiltersByIssue() throws Exception {
        Jwt jwt = makeToken();
        ResultActions resultActions = mockMvc.perform(get("/api/issues")
                .header("Authorization", "Bearer " + jwt.getAccessToken()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignees.length()").value(3))
                .andExpect(jsonPath("$.authors.length()").value(0))
                .andExpect(jsonPath("$.labels.length()").value(4))
                .andExpect(jsonPath("$.milestones.length()").value(4))
                .andExpect(jsonPath("$.milestones.[0].openIssueCount").value(0))
                .andExpect(jsonPath("$.milestones.[0].closedIssueCount").value(0))
                .andExpect(jsonPath("$.milestones.[1].openIssueCount").value(1))
                .andExpect(jsonPath("$.milestones.[1].closedIssueCount").value(2))
                .andDo(print());
    }

    private Jwt makeToken() {
        return jwtProvider.createJwt(Map.of("memberId",1L));
    }
}
