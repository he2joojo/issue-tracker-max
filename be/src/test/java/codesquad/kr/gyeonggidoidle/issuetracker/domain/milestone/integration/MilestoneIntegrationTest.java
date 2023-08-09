package codesquad.kr.gyeonggidoidle.issuetracker.domain.milestone.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class MilestoneIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @DisplayName("열린 마일스톤의 모드 정보를 가지고 온다.")
    @Test
    void testReadOpenMilestones() throws Exception {
        Jwt jwt = makeToken();
        ResultActions resultActions = mockMvc.perform(get("/api/milestones/open")
                .header("Authorization", "Bearer " + jwt.getAccessToken()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openMilestoneCount").value(3))
                .andExpect(jsonPath("$.closeMilestoneCount").value(1))
                .andExpect(jsonPath("$.labelCount").value(4))
                .andExpect(jsonPath("$.milestones.length()").value(3))
                .andExpect(jsonPath("$.milestones.[0].name").value("마일스톤 0"))
                .andDo(print());
    }

    @DisplayName("닫힌 마일스톤의 모드 정보를 가지고 온다.")
    @Test
    void testReadClosedMilestones() throws Exception {
        Jwt jwt = makeToken();
        ResultActions resultActions = mockMvc.perform(get("/api/milestones/closed")
                .header("Authorization", "Bearer " + jwt.getAccessToken()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openMilestoneCount").value(3))
                .andExpect(jsonPath("$.closeMilestoneCount").value(1))
                .andExpect(jsonPath("$.labelCount").value(4))
                .andExpect(jsonPath("$.milestones.length()").value(1))
                .andExpect(jsonPath("$.milestones.[0].name").value("마일스톤 2"))
                .andDo(print());
    }

    private Jwt makeToken() {
        return jwtProvider.createJwt(Map.of("memberId",1L));
    }
}
