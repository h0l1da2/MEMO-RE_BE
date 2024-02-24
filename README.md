# 📨 MEMO:RE

## 기억하고 되짚을래요. 태그 메모 서비스.

notion : [MEMO:RE](https://www.notion.so/MEMO-RE-4b0c4a898ac146f099c3fdc43a278860?pvs=21)

tistory : [https://hyuil.tistory.com/category/프로젝트/팀 프로젝트) MEMO%3ARE](https://hyuil.tistory.com/category/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/%ED%8C%80%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%29%20MEMO%3ARE)


![logo](https://github.com/h0l1da2/MEMO-RE_BE/assets/116418443/27649640-e4b9-4caa-b944-308a5324ab8d)

![main_img](https://github.com/h0l1da2/MEMO-RE_BE/assets/116418443/ea866e22-6690-4f53-9bfe-31b23c6c61b8)

<aside>
👾 10일 동안 진행한 짧은 미니 프로젝트입니다. 태그를 기준으로 메모를 작성하고 분류해서 필요한 아이디어를 얻을 수 있습니다.

</aside>

## 작업자
기획자 권순우

디자이너 박송이

FE 이송은

BE 강휴일

## 작업 기간
2023.08.04 ~ 2023.08.13

## 🕹️ 기술 스택

- Java , Spring Boot, Spring Security(JWT)
- MySQL , JPA
- RestApi

## 🤗 역할

- **BackEnd 기능 개발**
- 아이디어 제안과 기능 회의 및 개발
  - 원활한 서비스를 위해 태그 수정 제한을 제안
  - JWT 인증 로그인 및 회원가입, 메모 CRUD 와 List 등 각종 기능 개발
  - RestApi 로 Front 서버와 통신

## 주소
*프로젝트 일기(https://hyuil.tistory.com/category/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8/%ED%8C%80%20%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%29%20MEMO%3ARE)


## 데이터베이스 구조
<img width="447" alt="image" src="https://github.com/h0l1da2/MEMO-RE_BE/assets/116418443/289121c8-beac-4d74-a3ff-764e1b12800f"> 

- 메모 하나에 여러 개의 태그를 삽입할 수 있고, 태그는 독립된 데이터.
- `tag_memo` 테이블로 태그와 메모를 한 번에 관리할 수 있도록 처리.
    - 처음 설계에서는 PK, FK 만 가지고 있는 테이블을 만들어 쓸데없는 데이터를 조회하지 않고 `memo_id` 로 빠르게 조회하기 위해 만들었다.
    - 그러나, 연결 테이블은 조회보다는 두 테이블에 공통으로 추가되는 데이터를 만들기 위한 목적이 더 강하다는 것을 후에 깨달았다.
        - 다음부터는 조회만을 위해 연결 테이블을 만들 필요는 없을 것 같다.

## 🐳 Backend

- [JWT 로 유저 인증](https://hyuil.tistory.com/246)
- [통합 테스트 코드 작성](https://github.com/h0l1da2/MEMO-RE_BE/tree/9a3f5209ea2877f6556eddb175939300fe46f34f/src/test/java/sori/jakku/kkunkkyu/memore)
  - 통합 테스트는 굉장히 무거워 실무에서는 정말 필요한 경우에만 작성한다는 것을 깨닫고 난 뒤…
  - [단위 테스트 추가 및 통합 테스트 삭제](https://github.com/h0l1da2/MEMO-RE_BE/tree/master/src/test/java/sori/jakku/kkunkkyu/memore/domain)
  - [테스트 및 빌드 속도 개선](https://hyuil.tistory.com/289)
- [Rest Api Cors 통신](https://hyuil.tistory.com/240)
  - Credientals → `true`  (스프링 시큐리티를 사용할 때 해당 설정 필수)
    - Origin 에서 와일드카드 사용 X
    - 헤더는 와일드카드로 두되, 클라이언트 Origin 과 사용하는 메서드만 허용
- DB 커넥션 문제 최소화
  - [Database 서적 학습을 통해 트랜잭션의 올바른 사용법에 대해 공부.](https://hyuil.tistory.com/236)
  - [필요한 순간에만 @Transactional 애노테이션 부가](https://github.com/h0l1da2/MEMO-RE_BE/blob/master/src/main/java/sori/jakku/kkunkkyu/memore/tag/service/TagService.java)
- [사용자 정의 예외로 에러 핸들링](https://github.com/h0l1da2/MEMO-RE_BE/tree/master/src/main/java/sori/jakku/kkunkkyu/memore/common/exception)
  - `BadRequestException` → 4xx 에러
  - `InternalErrorException` → 5xx 에러
  - `Exception` → 에러 코드


# 주요 기능

### 로그인 회원가입

```swift
회원가입
- 아이디 중복 확인 후 가입

로그인
- SpringSecurity Filter 를 이용해 JWT 로그인 인증
- 클라이언트와 HttpHeader(AUTHORIZATION) 에 담긴 Access Token 으로 인증하며 통신
```


### 게시판



```swift
메모
- 태그를 기준으로 정렬 가능
- 다중 태그 허용
- 중복 메모 불허용
```


### 기타


```swift
- 여러 컨트롤러에서 반복되는 중복 코드 줄이기 위해 WebService 구현
 * Header 에서 id 를 가져오는 코드

- 예외 컨트롤을 편하게 하기 위해 사용자 정의 예외, Exception(enum) 구현
 * BadRequestException : 4xx Error
 * InternalErrorException : 5xx Error
 * Exception : Status , message 정의
 
- AWS RDS 이용

- Test
 * 통합 테스트 (현재 삭제)
   - Service Test
   - @AutoConfigureMockMvc 를 이용한 Api 테스트
 * 단위 테스트
```





## 프로젝트를 진행하며…

### 😵 힘들었던 점

- **다른 직무 분들과 소통하는 어려움.**
  - 그 분들은 개발을 모르시니 요청하신 기능이나 서비스가 왜 구현하기 어렵거나 시간이 필요한지 알기 쉽게 설명해야했다.
  - 개발 외적인 소통도 필요하구나 하는 생각과 고민이 들었다.
  - [원활한 소통으로 직무별 최고 동료상을 받을 수 있었다. 😭](https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbk0CMK%2FbtsrctuDkPY%2FZaM73jHrLXyIZJuoc6Tve0%2Fimg.png)
- **Cors 정책으로 프론트 통신에 어려움이 있었다.**
  - [그 동안 공부했던 Network 지식과 Security 지식을 이용해 Cors 관련 설정 완료.](https://hyuil.tistory.com/240)
- **트랜잭션은 어떻게 해야 올바르게 사용하는 것일까 의문.**
  - [Database 서적 학습을 통해 트랜잭션의 올바른 사용법에 대해 공부.](https://hyuil.tistory.com/236)
- **10일 간의 개발 기간이 너무도 짧다. 내가 개발을 얼마나 빨리 할 수 있는지 일정 관리가 안 된다.**
  - 애자일 방식을 이용해 스프린트를 나눠 개발하여 두루뭉술한 일정에 따라가는 것이 아니라 역량에 따라 개발할 수 있었다.