package me.kwakyunho.springbootdeveloper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @AfterEach
    void cleanUp() {
        memberRepository.deleteAll();
    }

    @Sql("/insert-members.sql")
    @Test
    void getAllMembers() {
        // when
        List<Member> members = memberRepository.findAll();

        //then
        assertThat(members.size()).isEqualTo(3);
    }

    @Sql("/insert-members.sql")
    @Test
    void getMemberById() {
        // when
        Member member = memberRepository.findById(2L).get();

        // then
        assertThat(member.getName()).isEqualTo("B");
    }

    @Sql("/insert-members.sql")
    @Test
    void getMemberByName() {
        // when
        Member member = memberRepository.findByName("C").get();

        // then
        assertThat(member.getId()).isEqualTo(3);
    }

    @Test
    void saveMember() {
        // given
        Member member = new Member(4L, "Yun");

        // when
        memberRepository.save(member);

        // then
        assertThat(memberRepository.findById(4L).get().getName()).isEqualTo("Yun");
    }

    @Test
    void saveAllMembers() {
        // given
        List<Member> members = List.of(
                new Member(1L, "Kwak"),
                new Member(2L, "Yun"),
                new Member(3L, "Ho")
        );

        // when
        memberRepository.saveAll(members);

        // given
        assertThat(memberRepository.findAll().size()).isEqualTo(3);
    }

    @Sql("/insert-members.sql")
    @Test
    void deleteMember() {
        // when
        memberRepository.deleteById(2L);

        // given
        assertThat(memberRepository.findById(2L).isEmpty()).isTrue();
        assertThat(memberRepository.findAll().size()).isEqualTo(2);
    }

    @Sql("/insert-members.sql")
    @Test
    void deleteAllMembers() {
        // when
        memberRepository.deleteAll();

        // given
        assertThat(memberRepository.findAll().size()).isZero();
    }
}