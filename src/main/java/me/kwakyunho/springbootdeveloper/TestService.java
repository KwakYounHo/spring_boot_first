package me.kwakyunho.springbootdeveloper;

import org.springframework.stereotype.Service;

@Service
public class TestService {
    @Autowired
    MemberRepository memberRepository;

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }
}
