package com.fastcampus.jpa.bookmanager.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Builder
@Entity //@Entity에는 pk primary key가 꼭 필요함
public class User { 
	@Id
	@GeneratedValue
	private Long id;
	@NonNull
	private String name;
	@NonNull
	private String email;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt; 
	
}


// getter setter 만드는 법 두가지
// 첫번째로 직접 손으로 쳐서 혹은 단축키를 써서 작성해주기
// 두번째로 lombok 사용하기
// 클래스 위에 @Getter, @Setter 어노테이션 추가를 해주면 모든 필드에 대하여 getter, setter 사용 가능
// 필드에서 하면 해당 getter, setter만 생성해줌