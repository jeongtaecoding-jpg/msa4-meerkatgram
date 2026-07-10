# Meerkatgram 레이어 구조 및 코드 컨벤션 분석

## 1. 패키지 및 레이어 구조 (도메인형 구조)
프로젝트는 `domain`과 `global`로 크게 나뉘어 있습니다.
- **global**: 전역 설정, 에러 핸들러, 공통 응답 포맷, 시큐리티 설정 등 프로젝트 전반에 쓰이는 기능이 위치합니다.
- **domain**: 비즈니스 로직 단위(`post`, `auth`, `user` 등)로 분리되어 있으며, 각 도메인 내부는 다음과 같은 레이어(계층)로 구성됩니다.

### 레이어별 역할
1. **controllers (프레젠테이션 계층)**
   - HTTP 요청을 받고 응답을 반환합니다.
   - Swagger 어노테이션(`@Tag`, `@ApiResponse` 등)을 통해 API 명세를 작성합니다.
   - 파라미터 유효성 검증(Validation)을 수행합니다.
2. **services (비즈니스 로직 계층)**
   - 핵심 비즈니스 로직이 들어있습니다.
   - 트랜잭션(`@Transactional`) 단위로 데이터 처리를 조율합니다.
3. **repositories (데이터 접근 계층)**
   - Spring Data JPA 인터페이스 및 QueryDSL 커스텀 리포지토리를 통해 DB에 직접 접근합니다.
4. **entities (도메인 모델 계층)**
   - DB 테이블과 1:1 매핑되는 클래스입니다.
5. **requests / responses (DTO - Data Transfer Object)**
   - 클라이언트와 데이터를 주고받기 위한 전용 객체입니다.
   - 엔티티를 외부에 직접 노출하지 않기 위해 분리합니다.

## 2. 주요 코드 컨벤션
1. **공통 응답 객체 (`GlobalRes<T>`)**
   - 모든 API 응답은 `GlobalRes`라는 통일된 구조(Record)로 감싸서 반환됩니다.
   - 구성: `code` (결과 코드), `message` (결과 메시지), `data` (실제 반환 데이터).
   - 예시: `ResponseEntity.ok(GlobalRes.success(data))`
2. **DTO 분리 원칙**
   - 사용자 입력값은 `XxxReq` (예: `PostIndexReq`), 서버 반환값은 `XxxRes` (예: `PostIndexRes`) 형태로 네이밍하여 명확히 구분합니다.
3. **Java Record 활용**
   - 불변(Immutable) 데이터 객체인 DTO나 응답 객체를 정의할 때 Java의 `record` 키워드를 적극적으로 사용하여 보일러플레이트 코드(Getters, Setters)를 줄였습니다.
4. **SpringDoc 기반 API 문서화 자동화**
   - 컨트롤러와 메서드에 `@Tag`, `@ApiResponse`, `@Parameter` 등을 붙여서 코드 작성과 동시에 API 문서(Swagger)가 업데이트 되도록 관리합니다.
