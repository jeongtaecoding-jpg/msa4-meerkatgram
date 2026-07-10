# API Response 종류 정리

이 문서는 MSA4-Meerkatgram 프로젝트의 각 API별 응답 코드를 대분류(API), 중분류(HttpStatus), 소분류(에러코드 및 정상코드)로 정리한 문서입니다. 모든 API는 `GlobalRes` 객체 포맷을 사용하여 공통된 응답 구조를 가집니다.

## 1. Auth API (인증) - `AuthController`

### 대분류: `POST /api/login` (로그인 처리)
- **중분류: HttpStatus 200 (OK)**
  - `소분류: 00` - 로그인 완료
- **중분류: HttpStatus 400 (Bad Request)**
  - `소분류: E21` - 요청 파라미터 이상 (MethodArgumentNotValidException)
- **중분류: HttpStatus 401 (Unauthorized)**
  - `소분류: E01` - 로그인 에러 (NotRegisteredException)
- **중분류: HttpStatus 500 (Internal Server Error)**
  - `소분류: E80` - DB 에러
  - `소분류: E99` - 시스템 에러

### 대분류: `POST /api/reissue-token` (토큰 재발급)
- **중분류: HttpStatus 200 (OK)**
  - `소분류: 00` - 토큰 재발급 완료
- **중분류: HttpStatus 401 (Unauthorized)**
  - `소분류: E04` - 토큰 이상 (InvalidTokenException)
  - `소분류: E02` - 인증 필요 (AuthenticationException)
- **중분류: HttpStatus 500 (Internal Server Error)**
  - `소분류: E99` - 시스템 에러

### 대분류: `POST /api/logout` (로그아웃)
- **중분류: HttpStatus 200 (OK)**
  - `소분류: 00` - 로그아웃 성공
- **중분류: HttpStatus 401 (Unauthorized)**
  - `소분류: E02` - 로그인이 필요한 서비스입니다 (AuthenticationException)
  - `소분류: E04` - 토큰 이상 (InvalidTokenException)
- **중분류: HttpStatus 500 (Internal Server Error)**
  - `소분류: E99` - 시스템 에러

### 대분류: `POST /api/registration` (회원가입)
- **중분류: HttpStatus 200 (OK)**
  - `소분류: 00` - 회원가입 완료
- **중분류: HttpStatus 400 (Bad Request)**
  - `소분류: E21` - 요청 파라미터 이상 (MethodArgumentNotValidException)
- **중분류: HttpStatus 409 (Conflict)**
  - `소분류: E11` - 중복 레코드 에러 (DuplicatedRecordException)
- **중분류: HttpStatus 500 (Internal Server Error)**
  - `소분류: E80` - DB 에러
  - `소분류: E99` - 시스템 에러

---

## 2. File API (파일 관리) - `FileController`

### 대분류: `POST /api/files/profiles` (프로필 사진 업로드)
- **중분류: HttpStatus 200 (OK)**
  - `소분류: 00` - 파일 저장 성공
- **중분류: HttpStatus 401 (Unauthorized)**
  - `소분류: E02` - 인증 필요
- **중분류: HttpStatus 500 (Internal Server Error)**
  - `소분류: E40` - 파일 업로드 실패 (FileManagedException)
  - `소분류: E99` - 시스템 에러

### 대분류: `POST /api/files/posts` (게시글 사진 업로드)
- **중분류: HttpStatus 200 (OK)**
  - `소분류: 00` - 파일 저장 성공
- **중분류: HttpStatus 401 (Unauthorized)**
  - `소분류: E02` - 인증 필요
- **중분류: HttpStatus 500 (Internal Server Error)**
  - `소분류: E40` - 파일 업로드 실패 (FileManagedException)
  - `소분류: E99` - 시스템 에러

---

## 3. Post API (게시글) - `PostController`

### 대분류: `GET /api/posts` (게시글 목록 조회)
- **중분류: HttpStatus 200 (OK)**
  - `소분류: 00` - 정상처리
- **중분류: HttpStatus 400 (Bad Request)**
  - `소분류: E21` - 요청 파라미터 이상 (MethodArgumentTypeMismatchException 등)
- **중분류: HttpStatus 401 (Unauthorized)**
  - `소분류: E02` - 인증 필요
- **중분류: HttpStatus 500 (Internal Server Error)**
  - `소분류: E80` - DB 에러
  - `소분류: E99` - 시스템 에러

### 대분류: `GET /api/posts/{id}` (게시글 상세 조회)
- **중분류: HttpStatus 200 (OK)**
  - `소분류: 00` - 게시글 상세 정상 처리
- **중분류: HttpStatus 400 (Bad Request)**
  - `소분류: E21` - 요청 파라미터 이상 (MethodArgumentTypeMismatchException 등)
- **중분류: HttpStatus 401 (Unauthorized)**
  - `소분류: E02` - 인증 필요
- **중분류: HttpStatus 404 (Not Found)**
  - `소분류: E10` - 삭제된 레코드 에러 (DeletedRecordException)
- **중분류: HttpStatus 500 (Internal Server Error)**
  - `소분류: E80` - DB 에러
  - `소분류: E99` - 시스템 에러

---

## 공통 발생 가능 예외 응답 (GlobalExceptionHandler 기준)
위의 API 외에도 애플리케이션 전반에서 권한 및 시스템 이슈로 인해 필터나 글로벌 단에서 아래와 같은 공통 응답이 발생할 수 있습니다.
- **중분류: HttpStatus 403 (Forbidden)**
  - `소분류: E03` - 권한이 부족합니다. (AccessDeniedException)
- **중분류: HttpStatus 500 (Internal Server Error)**
  - `소분류: E80` - DB 에러 (SQLException)
  - `소분류: E99` - 시스템 에러 (Exception)
