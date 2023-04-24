package com.sparta.testlevel1.entity;

import com.sparta.testlevel1.dto.BoardRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter  // 눈에는 안보이지만 title.getTitle() 같은 각각 메서드를 모두 만들어줌.
@Entity  // database의 board라는 테이블과 mapping하는 클래스로서 사용하겠다. 즉, JPA의 클래스로써 사용하겠다.
@NoArgsConstructor
public class Board extends Timestamped {   // 게시판에 대한 정보를 가지고 있는 클래스, 속성을 가지고있다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // IDENTITY 테이블마다 각각 독립적 ID 부여하겠다.
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String username;


    @ManyToOne(fetch = FetchType.EAGER)   // ManyToOne기본값 = EAGER -> 즉시로딩: 엔티티를 조회할때 연관된 엔티티도 함께 조회.  LAZY -> 연관된 엔티티를 실제 사용 할 때 조회
    @JoinColumn(name = "USER_ID", nullable = false)  //name은 외래키의명칭  //nullable은 안 줌.
    private User user;

    @OneToMany(mappedBy = "board",fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();


    public Board(BoardRequestDto boardRequestDto,  User user) {
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.user = user;
        this.username = user.getUsername();
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
    }
}
