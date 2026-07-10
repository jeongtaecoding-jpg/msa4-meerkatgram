# Meerkatgram 프로젝트 아키텍처 및 기술 스택 분석

## 1. 개요
Meerkatgram 프로젝트는 이미지 기반의 소셜 미디어(커뮤니티형 웹 애플리케이션)로, 백엔드와 프론트엔드가 완전히 분리된(Decoupled) 구조를 채택하고 있습니다. 백엔드는 RESTful API 서버로서 동작하며 JSON 형식으로 데이터를 통신합니다.

## 2. 주요 기술 스택 (Backend)
- **Language**: Java 17
- **Framework**: Spring Boot 3.5 (SNAPSHOT)
- **Database**: MySQL 8.4
- **ORM / Data Access**: Spring Data JPA, QueryDSL (과거 MyBatis에서 JPA 체제로 마이그레이션 중 혹은 병행 사용 흔적이 존재합니다.)
- **Security & Auth**: Spring Security + JWT (JSON Web Token)
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Build Tool**: Gradle

## 3. 시스템 아키텍처 특성
1. **REST API 서버**: 프론트엔드(Vue 3)와 API로 통신하는 백엔드 서버입니다.
2. **JWT 기반 무상태(Stateless) 인증**: 세션을 유지하지 않고 Access Token과 Refresh Token을 통해 사용자 인증을 인가합니다. 이를 통해 서버의 확장성을 높였습니다.
3. **도메인형 구조 (Package-by-Feature)**: 코드를 기능(Domain) 단위로 나누어 유지보수성을 극대화했습니다. (예: `auth`, `post`, `user` 등)
4. **객체 지향적 데이터 관리 (JPA & QueryDSL)**: SQL 중심의 개발에서 벗어나 JPA를 통해 자바 객체 중심으로 데이터베이스를 관리하며, 복잡한 동적 쿼리는 QueryDSL을 이용해 타입 안정성을 확보하며 작성합니다.

## 만들 때 프롬프트
현재 프로젝트를 아래 사항들을 고려해서 분석해줘.
필요에 따라 파일을 분리해서 작성해줘.
- @doc\analysis에 파일 생성
- 프로젝트 아키텍쳐 관련
- 코드 컨벤션 관련
- 레이어 관련
- 비전공자가 알 수 있도록 전반적인 흐름과 관련된 내용