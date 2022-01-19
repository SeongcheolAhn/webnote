# WebNote
--- 
- 간단한 메모를 기기에 상관없이 가볍게 공유할 수 있으면 좋겠다는 생각에 시작한 개인 프로젝트입니다. 
- 학습중인 Spring framework를 복습하는 개념으로 프로젝트를 진행중입니다. 
- [13.125.24.152:8080](http://13.125.24.152:8080)를 통해 사용할 수 있습니다.
- --
## 프로젝트 환경  
- AWS EC2
  - 스프링 부트 빌드 파일 (jar) + MySQL

## 프로젝트 설정
- Project : Gradle
- Language : Java
- Spring Boot : 2.6.2

- Project Metadata
  - Group : note
  - Artifact: webnote
  - Name : webnote
  - Package name : note.webnote
  - Packaging : Jar
  - Java : 11
- Dependencies : Spring Web, Spring Data JPA, Validation, H2 database, Thymeleaf, lombok, spring security, MySQL
---
## 제작 과정
- 도메인 모델  
![image](https://user-images.githubusercontent.com/68803008/150144522-745e2a80-bd2d-4ffc-9e66-ccc55865df38.png)

- 엔티티 설계  
![image](https://user-images.githubusercontent.com/68803008/150144411-5830b320-d4fd-4122-9bd6-bc43bacff228.png)

- 테이블 설계  
![image](https://user-images.githubusercontent.com/68803008/150146062-5448616d-56b4-429b-8eb4-6e80cf1597fb.png)


---
## 요구사항
- 나의 노트를 다른 사람들과 공유할 수 있다.
- 나의 노트를 공유받은 사람이 권한이 있다면 편집도 할 수 있다.

### 기능 목록
- 회원
  - 회원 가입
  - 나의 노트에 참가한 공유자의 권한 설정 가능 (편집 가능, 읽기만 가능)
- 노트
  - 노트 등록, 조회, 수정, 삭제
