package me.kwakyunho.springbootdeveloper;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity // (name="table_name") 입력시 해당 데이터 테이블과 매핑
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id; // Matches the 'id' column in the database table

    @Column(name = "name", nullable = false)
    private String name; // Matches the 'name' column in the database table

    public void changeName(String name) {
        this.name = name;
    }
}
