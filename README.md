# 바로 인턴 과제

## ✅ 프로젝트 개요

이 프로젝트는 Spring Boot 프레임워크를 사용하여 JWT 기반으로 사용자 인증 및 인가 구현 과제입니다.

## ✅ 주요 기능
### 모든 사용자 접근 가능
- 회원가입
- 사용자 로그인
### 관리자 접근 가능
- 관리자 역할 부여

## ✅ 사용 기술
|기술|버전|
|--|--|
|Java|21|
|Spring Boot|3.2.5|
|Spring Security||
|Jsonwebtoken|0.12.5|
|Map struct|1.5.5|
|Lombok||
|Swagger|2.0.2|
|Mockito||
|AssertJ||
|Junit5||
|Gradle||

## ✅ API 명세
|이름|주소|
|--|--|
|Swagger UI|http://3.38.181.117:8080/swagger-ui/index.html|

본 서비스는 사용자 인증 및 권한 관리를 위한 RESTful API를 제공합니다.

### 1. 회원가입 (Sign Up)
- HTTP Method: POST

- URL: http://3.38.181.117:8080/api/v1/users/signup

- API 설명: 사용자가 서비스에 가입할 때 필요한 정보를 입력받아 계정을 생성합니다.

Request Body 예시

```
{
  "username": "test3",
  "password": "12341234",
  "nickname": "test3"
}
```
Response Body 예시

✅ 성공 (HTTP Status: 200 OK)

```
{
  "id": 3,
  "username": "test3",
  "password": "$2a$10$Y9yNjRF9ROrUn1bzffbWs.2TzZPQjOmU9XMwGYp9dkuxRMSos.SHe",
  "nickname": "test3",
  "role": "USER"
}
```
❌ 실패 (HTTP Status: 400 Bad Request) - 이미 가입된 사용자

```
{
  "code": "USER_ALREADY_EXISTS",
  "message": "이미 가입된 사용자입니다."
}
```

### 2. 로그인 (Login)

- HTTP Method: POST

- URL: http://3.38.181.117:8080/api/v1/users/login

- API 설명: 사용자 이름과 비밀번호를 통해 인증을 시도하고, 성공 시 JWT 인증 토큰을 발급합니다. 이 토큰은 보호된 API에 접근할 때 사용됩니다.

Request Body 예시

```
{
  "username": "test1",
  "password": "123123"
}
```
Response Body 예시

✅ 성공 (HTTP Status: 200 OK)

```
{
  "grantType": "Bearer ",
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJpZCI6MiwidHlwZSI6IkFDQ0VTUyIsInN1YiI6InRlc3QxIiwiaWF0IjoxNzUzNTMyMTcwLCJleHAiOjE3NTM1MzkzNzB9.HaegpS_0V7Z4AsGOiFmwlXLzUmly6UsLg2xkmcuEGIM"
}
```
❌ 실패 (HTTP Status: 400 Bad Request) - 잘못된 계정 정보

```
{
  "code": "INVALID_CREDENTIALS",
  "message": "아이디 또는 비밀번호가 올바르지 않습니다."
}
```

### 3. 관리자 권한 부여 (Grant Admin Role)

- HTTP Method: POST

- URL: http://3.38.181.117:8080/admin/users/{userId}/roles

- API 설명: 경로 변수 userId에 해당하는 사용자에게 ADMIN 권한을 부여합니다. 관리자만 이 API를 호출할 수 있습니다.

- Path Variable: {userId} - 권한을 부여할 사용자의 고유 ID

- Path Variable 예시 : http://3.38.181.117:8080/admin/users/15/roles

Response Body 예시

✅ 성공 (HTTP Status: 200 OK)

```
{
  "username": "test3",
  "nickname": "test3",
  "role": "ADMIN"
}
```

❌ 실패 (HTTP Status: 403 Forbidden) - 권한이 부족한 경우 (접근 제한)

```
{
  "code": "ACCESS_DENIED",
  "message": "관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."
}
```

## ✅ 에러 메시지

|Code|Message|httpStatus|
|--|--|--|
|ALREADY_EXISTS|"이미 존재하는 사용자입니다."|HttpStatus.CONFLICT|
|USERNAME_LENGTH_5_NOT_OVER|"유저 아이디가 5글자 미만입니다."|HttpStatus.BAD_REQUEST|
|INVALID_INPUT_VALUE|"필수 입력 값이 누락되었거나 유효하지 않습니다."|HttpStatus.BAD_REQUEST|
|INVALID_CREDENTIALS|"아이디 또는 비밀번호가 일치하지 않아 인증에 실패했습니다."|HttpStatus.BAD_REQUEST|
|INTERNAL_SERVER_ERROR|"서버 오류 발생"|HttpStatus.INTERNAL_SERVER_ERROR|
|NOT_EXISTS_USER|"존재하지 않는 사용자입니다."|HttpStatus.NOT_FOUND|
|ACCESS_DENIED|"관리자 권한이 필요한 요청입니다. 접근 권한이 없습니다."|HttpStatus.FORBIDDEN|

## ✅ 실행 방법
1. 사전 요구사항
Java 21

2. 빌드
프로젝트 루트 디렉토리에서 아래 명령어를 실행하여 프로젝트를 빌드합니다.

`./gradlew build`

4. 실행
빌드가 완료되면 build/libs 경로에 실행 가능한 .jar 파일이 생성됩니다.

아래 명령어로 애플리케이션을 실행합니다.

파일 이름의 버전은 프로젝트 설정에 따라 다를 수 있습니다.

`java -jar build/libs/baro-intern-0.0.1-SNAPSHOT.jar`

애플리케이션은 기본적으로 8080 포트에서 실행됩니다.
