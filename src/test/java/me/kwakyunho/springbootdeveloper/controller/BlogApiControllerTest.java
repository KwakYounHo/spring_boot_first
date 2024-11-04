package me.kwakyunho.springbootdeveloper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kwakyunho.springbootdeveloper.domain.Article;
import me.kwakyunho.springbootdeveloper.dto.AddArticleRequest;
import me.kwakyunho.springbootdeveloper.dto.UpdateArticleRequest;
import me.kwakyunho.springbootdeveloper.repository.BlogRepository;
import me.kwakyunho.springbootdeveloper.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Autowired
    private BlogService blogService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
        jdbcTemplate.execute("ALTER TABLE article ALTER COLUMN id RESTART WITH 1");
    }

    private final String url = "/api/articles";

    @DisplayName("POST(/api/articles) : addArticle() -> 객체를 직렬화 하여 요청, 아티클 레포지토리의 길이 및 0번째 데이터가 일치하는지 확인")
    @Test
    public void addArticleTest() throws Exception {
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

        List<Article> articles = blogService.findAll();

        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);
    }

    @DisplayName("GET(/api/articles) : findAllArticles() -> List<ResponseArticle>")
    @Test
    public void findAllArticlesTest() throws Exception {
        // given
        AddArticleRequest request1 = new AddArticleRequest("제목1", "내용1");
        AddArticleRequest request2 = new AddArticleRequest("제목2", "내용2");
        AddArticleRequest request3 = new AddArticleRequest("제목3", "내용3");

        blogService.save(request1);
        blogService.save(request2);
        blogService.save(request3);

        // when
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

    @DisplayName("DELETE(api/articles/{id} : delete() -> void")
    @Test
    public void deleteArticleTest() throws Exception {
        // given
        AddArticleRequest requestArticle = new AddArticleRequest("name", "content");
        blogService.save(requestArticle);
        long targetId = 1L;

        // when
        mockMvc.perform(delete("/api/articles/"+targetId))
                .andExpect(status().isOk());

        List<Article> result = blogService.findAll();

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("PUT(api/articles/{id} : updateArticle -> Article")
    @Test
    public void updateArticleTest() throws Exception{
        // given
        AddArticleRequest previousArticle = new AddArticleRequest("title", "content");
        Article savedArticle = blogService.save(previousArticle);

        String updateTitle = "업데이트 된 타이틀";
        String updateContent = "해당 내용이 보이면 성공입니다.";

        String url = "/api/articles/{id}";
        UpdateArticleRequest request = new UpdateArticleRequest(updateTitle, updateContent);

        // when
        ResultActions result = mockMvc
                .perform(put(url, savedArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON));

        // then
        result
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updateTitle))
                .andExpect(jsonPath("$.content").value(updateContent));
    }
}