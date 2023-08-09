package codesquad.kr.gyeonggidoidle.issuetracker.domain.label.integration;

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
public class LabelIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtProvider jwtProvider;

    @DisplayName("라벨의 모드 정보를 가지고 온다.")
    @Test
    void testReadLabelPage() throws Exception {
        Jwt jwt = makeToken();
        ResultActions resultActions = mockMvc.perform(get("/api/labels")
                .header("Authorization", "Bearer " + jwt.getAccessToken()));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneCount").value(4))
                .andExpect(jsonPath("$.labelCount").value(4))
                .andExpect(jsonPath("$.labels.length()").value(4))
                .andExpect(jsonPath("$.labels.[0].name").value("라벨 0"))
                .andDo(print());
    }

    private Jwt makeToken() {
        return jwtProvider.createJwt(Map.of("memberId",1L));
    }
}
