package me.kwakyunho.springbootdeveloper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kwakyunho.springbootdeveloper.domain.Article;
import me.kwakyunho.springbootdeveloper.dto.AddArticleRequest;
import me.kwakyunho.springbootdeveloper.repository.BlogRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class BlogServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }

    @DisplayName("findAll() 메서드 실행 반환값 확인")
    @Test
    public void findAllTest() throws Exception {
        // given
        AddArticleRequest userRequest = new AddArticleRequest("테스트용1 제목", "테스트용1 내용");  // 테스트용 객체 생성
        String requestBody = objectMapper.writeValueAsString(userRequest);  // json으로 직렬화
        ResultActions result = mockMvc  // 요청 전달
                .perform(post("/api/articles")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody));

        // when
        List<Article> articles = blogService.findAll();

        // then
        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(userRequest.getTitle());
        assertThat(articles.get(0).getContent()).isEqualTo(userRequest.getContent());
    }

}