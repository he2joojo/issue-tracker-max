package codesquad.kr.gyeonggidoidle.issuetracker.domain.label.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import codesquad.kr.gyeonggidoidle.issuetracker.annotation.IntegrationTest;
import codesquad.kr.gyeonggidoidle.issuetracker.domain.label.controller.request.LabelRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@IntegrationTest
public class LabelIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("라벨의 모든 정보를 가지고 온다.")
    @Test
    void testReadLabelPage() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/labels"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.milestoneCount").value(4))
                .andExpect(jsonPath("$.labelCount").value(4))
                .andExpect(jsonPath("$.labels.length()").value(4))
                .andExpect(jsonPath("$.labels.[0].name").value("라벨 0"))
                .andDo(print());
    }

    @DisplayName("라벨을 받아 저장한다.")
    @Test
    void create() throws Exception {
        // given
        LabelRequest request = LabelRequest.builder()
                .name("label1")
                .description("설명")
                .backgroundColor("##")
                .textColor("#")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/labels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andDo(print());
    }

    @DisplayName("하나의 라벨 정보를 가져온다.")
    @Test
    void read() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/api/labels/1"));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(1L),
                        jsonPath("$.name").value("라벨 1"),
                        jsonPath("$.description").doesNotExist()
                );
    }

    @DisplayName("라벨을 받아 내용을 수정한다.")
    @Test
    void update() throws Exception {
        // given
        LabelRequest request = LabelRequest.builder()
                .name("updatedTitle")
                .description("설명")
                .backgroundColor("##")
                .textColor("#")
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/labels/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andDo(print());
    }

    private <T> String toJson(T data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
    }
}
