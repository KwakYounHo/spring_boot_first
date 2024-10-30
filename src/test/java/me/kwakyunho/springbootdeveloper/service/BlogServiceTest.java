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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        blogRepository.deleteAll();
        jdbcTemplate.execute("ALTER TABLE article ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("GET(/api/articles) : findAll() -> 반환값 확인")
    @Test
    public void findAllTest() throws Exception {
        // given
        AddArticleRequest userRequest = new AddArticleRequest("테스트용1 제목", "테스트용1 내용");  // 테스트용 객체 생성
        blogService.save(userRequest);

        // when
        List<Article> articles = blogService.findAll();

        // then
        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(userRequest.getTitle());
        assertThat(articles.get(0).getContent()).isEqualTo(userRequest.getContent());
    }

    @DisplayName("GET(/api/articles/{id} : findById() -> 반환값 확인")
    @Test
    public void findById() throws Exception {
        // given
        AddArticleRequest userRequest = new AddArticleRequest("제목1", "내용1");
        Article savedArticle = blogService.save(userRequest);

        // when
        Article selectedArticle = blogService.findById(savedArticle.getId());
        try {
            Article article = blogService.findById(100L);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        // then
        assertThat(selectedArticle.getTitle()).isEqualTo(userRequest.getTitle());
        assertThat(selectedArticle.getContent()).isEqualTo(userRequest.getContent());
    }
}