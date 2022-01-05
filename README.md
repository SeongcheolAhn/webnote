# WebNote
--- 
- 간단한 메모를 기기에 상관없이 가볍게 공유할 수 있으면 좋겠다는 생각에 시작한 개인 프로젝트입니다. 
- 학습중인 Spring framework를 복습하는 개념으로 프로젝트를 진행중입니다. 
- --
## 프로젝트 설정
- Projrct : Gradle
- Language : Java
- Spring Boot : 2.6.2

- Project Metadata
  - Group : note
  - Artifact: webnote
  - Name : webnote
  - Package name : note.webnote
  - Packagin : Jar
  - Java : 11
- Dependencies : Spring Web, Spring Data JPA, Validation, H2 database, Thymeleaf
---
## 제작 과정
- 도메인 모델
![image](https://user-images.githubusercontent.com/68803008/148298790-e798d272-132c-4ce9-82f1-6a63cf0fdc9a.png)

- 엔티티 설계
![image](https://user-images.githubusercontent.com/68803008/148298884-f513d969-9d1d-4261-af48-0f35e99706d9.png)

- 테이블 설계
![image](https://user-images.githubusercontent.com/68803008/148298934-f0b4f7e2-a9d0-400d-b05a-44cc081f5fb6.png)


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
