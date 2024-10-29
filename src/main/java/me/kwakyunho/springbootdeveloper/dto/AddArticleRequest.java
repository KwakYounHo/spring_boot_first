package me.kwakyunho.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.kwakyunho.springbootdeveloper.domain.Article;

@NoArgsConstructor // 매개변수 없는 생성자 (기본 생성자) 추가
@AllArgsConstructor // 모든 필드를 매개변수로 갖는 생성 추가
public class AddArticleRequest {

    private String title;
    private String content;

    public Article toEntity() {
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}