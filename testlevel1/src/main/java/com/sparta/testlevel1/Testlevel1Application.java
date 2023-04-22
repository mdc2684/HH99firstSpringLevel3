package com.sparta.testlevel1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing  //Timestamp 관련 뭐 사용한다는거같다.
@SpringBootApplication //들어가서보면 componentscan 포함. bean등록하고 ioc컨테이너에 저장.
public class Testlevel1Application {

	public static void main(String[] args) {
		SpringApplication.run(Testlevel1Application.class, args);
	}

}
