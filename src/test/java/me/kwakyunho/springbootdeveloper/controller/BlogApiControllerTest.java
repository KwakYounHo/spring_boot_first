package me.kwakyunho.springbootdeveloper.controller;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
    }

    private final String url = "/api/articles";

    @DisplayName("POST(/api/articles) : addArticle() -> JSON 문자열을 요청 본문에 전달, 201, 본문 반환")
    @Test
    public void addArticleTest1() throws Exception {
        // given
        String requestBody = "{\"title\":\"기사 제목\", \"content\":\"기사 내용\"}";

        // when
        ResultActions result = mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody)
        );

        // then
        result
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("기사 제목"));
    }

    @DisplayName("POST(/api/articles) : addArticle() -> 객체를 직렬화 하여 요청, 아티클 레포지토리의 길이 및 0번째 데이터가 일치하는지 확인")
    @Test
    public void addArticleTest2() throws Exception {
        // given
        String title = "title";
        String content = "content";
        AddArticleRequest article = new AddArticleRequest(title, content);
        String requestBody = objectMapper.writeValueAsString(article);

        // when
        ResultActions result = mockMvc
                .perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody));

        // given
        result
                .andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("GET(/api/articles) : findAllArticles() -> List<ResponseArticle>")
    @Test
    public void findAllArticlesTest() throws Exception {
        // given
        AddArticleRequest request1 = new AddArticleRequest("제목1", "내용1");
        AddArticleRequest request2 = new AddArticleRequest("제목2", "내용2");
        AddArticleRequest request3 = new AddArticleRequest("제목3", "내용3");

        String requestBody1 = objectMapper.writeValueAsString(request1);
        String requestBody2 = objectMapper.writeValueAsString(request2);
        String requestBody3 = objectMapper.writeValueAsString(request3);

        // when
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody1));
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody2));
        mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON_VALUE).content(requestBody3));

        ResultActions result = mockMvc
                .perform(get(url)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(request1.getTitle()))
                .andExpect(jsonPath("$[1].title").value(request2.getTitle()))
                .andExpect(jsonPath("$[2].title").value(request3.getTitle()))
                .andExpect(jsonPath("$.length()").value(3));
    }
}