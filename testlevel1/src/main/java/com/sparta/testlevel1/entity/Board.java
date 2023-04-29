package com.sparta.testlevel1.entity;

import com.sparta.testlevel1.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter  // 눈에는 안보이지만 title.getTitle() 같은 각각 메서드를 모두 만들어줌.
@Entity  // database의 board라는 테이블과 mapping하는 클래스로서 사용하겠다. 즉, JPA의 클래스로써 사용하겠다.
@NoArgsConstructor
@DynamicInsert
public class Board extends Timestamped {   // 게시판에 대한 정보를 가지고 있는 클래스, 속성을 가지고있다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // IDENTITY 테이블마다 각각 독립적 ID 부여하겠다.
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    @ColumnDefault("0")
    private int liked;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)  // ************* "board"는 연관관계의주인인 Comment의 필드명 board를 말하는것. Board가 아님!
    // cascade는 특정 Entity의 영속성 상태가 변경 되었을 때, 이와 연관된 Entity에도 영속성을 전파 시킬지 말지 선택하는 옵션
    private List<Comment> commentList = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)   // ManyToOne기본값 = EAGER -> 즉시로딩: 엔티티를 조회할때 연관된 엔티티도 함께 조회.  LAZY -> 연관된 엔티티를 실제 사용 할 때 조회
    @JoinColumn(name = "USER_ID")  //name은 외래키의명칭  //nullable은 안 줌.
    private User user;


    public Board(BoardRequestDto boardRequestDto,  User user) {
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.user = user;
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
    }

    public void plusLiked() {
        liked +=  1;
    }
    public void minusLiked() {
        liked -= 1;
    }
}
