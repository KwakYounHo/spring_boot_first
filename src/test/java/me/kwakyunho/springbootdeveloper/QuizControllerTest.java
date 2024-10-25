package me.kwakyunho.springbootdeveloper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class QuizControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @DisplayName("quiz() : GET('/quiz?code=1') 이면, 응답 코드는 201, 응답 본문은 'Created!'를 리턴")
    @Test
    public void getQuiz1() throws Exception {
        // given
        final String url = "/quiz";

        // when
        ResultActions result = mockMvc
                .perform(get(url)
                        .param("code", "1")
                        .accept(MediaType.APPLICATION_JSON)
                );

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(content().string("Created!"));
    }

    @DisplayName("quiz() : GET('quiz?code=2') 이면, 응답코드는 400, 응답 본문은 'Bad Request!'를 리턴")
    @Test
    public void getQuiz2() throws Exception {
        // given
        final String url = "/quiz";

        // when
        ResultActions result = mockMvc
                .perform(get(url)
                        .param("code", "2")
                        .accept(MediaType.APPLICATION_JSON)
                );

        // then
        result
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request!"));

    }

}