package com.sparta.testlevel1.repository;

import com.sparta.testlevel1.entity.Board;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository //안해도 내부적으로 bean으로 등록 해준다함
public interface BoardRepository extends JpaRepository<Board, Long> {  // < 연결하려고하는테이블(클래스) , Id타입 >
    //List<Board> findAllByOrderByModifiedAtDesc();  // 쿼리메소드 ,  수정날짜기준내림차순
    List<Board> findAllByOrderByCreatedAtDesc();  //  작성날짜기준 내림차순


    // cf.   Optional<Board> findByTitle(String title);  "Board에서 title(매개변수)인 것을 찾아주겠다."  이런것도 만들수있음.
}
