package me.kwakyunho.springbootdeveloper;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // create application's context for test
@AutoConfigureMockMvc // create MocMvc and auto configure
class TestControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    public void cleanUp() {
        memberRepository.deleteAll();
    }

    @DisplayName("getAllMembers: 아티클 조회에 성공한")
    @Test
    public void getAllMembers() throws Exception {
        // given
        final String url = "/test";
        Member savedMember = memberRepository.save(new Member(1L, "홍길동"));

        // when
        final ResultActions result = mockMvc.perform(get(url) // perform() : 요청을 전송하는 역할. ResultActions 객체를 반환 받음. ResultActions는 andExcept() 메서드를 제공
                .accept(MediaType.APPLICATION_JSON)); // 요청을 보낼 때 어떤 타입으로 반환을 받을지 결정하는 메서드

        // then
        result
                .andExpect(status().isOk()) // 응답을 검증하는 메서드 : TestController 에서 만든 API는 응답으로 Ok(200) 반환. 이를 확인하는 isOk()메서드로 검증하는 과정
                /*
                    응답의 0번째 값이 DB에 저장한 값과 같은지 확인
                    jsonPath("$[0].[필드명]) = JSON의 응답 값을 가져오는 메서드
                    0번째 배열에 있는 객체의 id, name값을 가져오고, 저장된 값과 같은지 확인
                */
                .andExpect(jsonPath("$[0].id").value(savedMember.getId()))
                .andExpect(jsonPath("$[0].name").value(savedMember.getName()));
    }
}