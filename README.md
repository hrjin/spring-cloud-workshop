# spring-cloud-workshop

## Hystrix(Circuit Breaker)
Circuit Breaker의 단위
- 에러 비율 통계의 단위
- A 메소드와 B 메소드가 같은 Circuit Breaker를 사용한다면 A,B의 에러 비율이 함께 통계내지고, Circuit이 오픈되면 함께 차단된다.


### Hystrix를 쓰는 것의 또다른 의미
- 별도의 ThreadPool에서 내 Code를 실행하는 것.
- ThreadPool을 여러개의 Circuit Breaker가 공유할 수 있다.

### ThreadPool 사용을 통해 얻을 수 있는 것
- 내 시스템의 Resource가 다양한 기능들을 위해 골고루 분배됨.


### ThreadPool과 Circuit Breaker는 별개
- ThreadPool에 대한 설정을 하지 않으면 Default로 @HystrixCommand가 선언된 클래스 이름이 ThreadPool 이름이 된다.
```
예시로 두개의 메소드(commandKey = 'serviceA', commandKey = 'serviceB')는 각각의 Circuit Breaker를 가지지만 두개의 Command가 'MyService'라는 하나의 ThreadPool에서 수행됨.
```
<br>
<br>

## Ribbon 적용

### 특징
- Client(API Caller) 에 탑재되는 S/W 모듈
- 주어진 서버 목록에 대해 Load Balancing을 수행함
- H/W가 필요없이 S/W로만 가능
- 서버 목록의 동적 변경이 자유로움(단 coding 필요)
- Load Balancing Schema를 마음대로 구성 가능(단 coding 필요)
- Ribbon은 여러 Component에 내장되어 있으며, 이를 통해 Client Load Balancing이 수행 가능하다.
- Ribbon은 매우 다양한 설정이 가능(서버 선택, 실패시 skip 시간, Ping 체크)
- Ribbon에는 Retry 기능이 내장돼있다.
- Eureka와 함께 사용될 때 강력하다.


### 주의점
- Retry를 시도하다가도 HystrixTimeout이 발생하면, 즉시 에러를 리턴.(Hystrix로 Ribbon을 감싸서 호출한 상태이므로)
- classpath에 retry가 존재해야한다.


<br>
<br>

## Eureka(Dynamic Service Discovery)
- Service Registry
- DiscoveryClient : spring-cloud에서 서비스 레지스트리 사용 부분을 추상화
- Eureka 를 통해 Round Robbin 으로 Ribbon 호출하여 Load Balancing


### Eureka in Spring Cloud
- 서버 시작 시 Eureka Server(Registry)에 자동으로 자신의 상태를 등록
> eureka.client.register-with-eureka: true(default)

- 주기적 HeartBeat로 동작함을 알림
> eureka.instance.lease-renewal-interval-in-seconds: 30(default)

- 서버 종료시 자신의 상태 변경(Down)

- Eureka상에 등록된 이름은 spring.application.name

- @EnableEurekaClient를 붙인 Application은 Eureka 서버로부터 남의 주소를 가져와서 로컬, 즉 메모리에 그 주소를 등록. Ribbon 에서는 그 메모리에 있는 주소를 가지고 Round Robbin한다.


### RestTemplate에 Eureka 적용하기
- @LoadBalanced RestTemplate 에는 Ribbon + Eureka 연동
- Ribbon의 Load Balancing과 Retry가 함께 동작

<br>
<br>

## Feign(Declarative Http Client)

### 특징
- Interface 선언을 통해 자동으로 Http Client를 생성
- RestTemplate은 concreate 클래스라 테스트하기 어렵다
> 관심사 : 어떤 URL, 어떻게 파싱할 것인가가 아닌 로컬에서 외부 서비스를 호출하고 리턴을 받아 오는 것
- Spring Cloud에서 Open-Feign 기반으로 Wrapping한 것이 Spring Cloud Feign

### 사용법
- @FeignClient를 Interface에 명시하는 선언만으로 Http Client 구현물을 만들어 줌
- 각 API를 Spring MVC Annotation을 통해 정의
- @Autowired를 통해 DI 받아 사용


### Feign의 동작
- Feign Client는 내부적으로 Ribbon을 사용.
> - @FeignClient에 URL 명시 => No Ribbon, No Eureka, No Hystrix (순수 Feign Client로서만 동작)
> - @FeignClient에서 URL만 제거 => Ribbon + Eureka + Hystrix 모드로 동작
어떤 서버 호출하나? @FeignClient(name='product') eureka에서 product 서버 목록을 조회해서 Ribbon을 통해 Load Balancing하며 HTTP호출을 수행


### Feign 정리
- 인터페이스 선언 + 설정으로 아래 가능
- Http Client
- Eureka 타겟 서버 주소 획득
- Ribbon을 통한 Client-Side Load Balancing
- Hystrix를 통한 메소드별 Circuit Breaker


### Feign + Hystrix, Ribbon, Eureka
- 장애 유형별 동작 예
> 특정 API 서버의 인스턴스가 한개 Down된 경우
> - Eureka : Heartbeat 송신이 중단됨으로 일정 시간 후 목록에서 사라짐
> - Ribbon : IOException이 발생하여 다른 인스턴스로 Retry
> - Hystrix : Circuit은 오픈되지 않음. Fallback, Timeout은 동작

> 특정 API가 비정상적인 경우(지연, 에러)
> - Hystrix : 해당 API를 호출하는 Circuit Breaker 오픈. Fallback, Timeout도 동작

<br>
<br>

## API Gateway

### 특징
- 클라이언트와 백엔드 서버 사이의 출입문(front door)
- 특정 URL이 요청왔을 때 내가 원하는 서비스로 라우팅(라우팅, 필터링, API 변환, 클라이언트 어댑터 API, 서비스 프록시)
- 횡단 관심사 cross-sesrvice concerns
> 보안, 인증, 인가
> 일정량 이상의 요청 제한(Rate limiting)
> 계측(metering)


### Netflix Zuul
- Hystrix, Eureka, Ribbon 내장
- 마이크로 프록시
- AWS ELB(Elastic Load Balancer) 앞에 위치해 어느 서버로 보낼 것인지

```
1) Hystrix Command
- Zuul의 모든 API 요청은 Hystrix Command로 구성되어 전달
> 각 API 경로별로 Circuit Breaker 생성
> 하나의 서버군이 장애를 일으켜도 다른 서버군의 서비스에는 영향이 없다
> Circuit Breaker / ThreadPool의 다양한 속성을 통해 서비스 별 속성에 맞는 설정 가능

2) Ribbon
- API를 전달할 서버의 목록을 갖고 Ribbon을 통해 Load Balancing을 수행
> 주어진 서버 목록들을 Round-Robin으로 호출
> 코딩을 통해 Customize 가능

3) Eureka Client
- 각 URL에 매핑된 서비스명을 찾아 Eureka Server를 통해 목록을 조회한다.
- 조회된 서버 목록을 'Ribbon' 클라이언트에게 전달한다.

4) Eureka + Ribbon에 의해 결정된 Server 주소로 HTTP(Apache HTTP Client가 기본) 요청

5) 선택된 첫 서버로의 호출이 실패할 경우 Ribbon에 의해 자동으로 Retry 수행
- Retry 수행 조건
> HTTP Client에서 Exception 발생(IOException)
> 설정된 응답코드 반환(ex. 503)
```

<br>
<br>

```
- 정리
> Zuul의 모든 호출은 HystrixCommand로 실행되므로 Circuit Breaker를 통해 장애 시 Fallback 수행 가능
> Ribbon + Eureka 조합을 통해 현재 서비스가 가능한 서버의 목록을 자동으로 수신
> Ribbon의 Retry 기능을 통해 동일한 종류 서버들로 자동 재시도가 가능
```

<br>
<br>

#### Spring Cloud Zuul의 Isolation
Spring Cloud Zuul의 기본 Isolation은 Semaphore. Semaphore는 network timeout을 격리시켜주지 못함. 그래서 Thread로 변경.
- zuul.ribbon-isolation-strategy: thread

But!! 전체 서비스의 대해 모든 HystrixCommand가 하나의 Threadpool이 생김. (버그...)

그래서 다시 이걸 유레카에 등록된 각 서비스별 Thread로 분리 생성하기 위해서는 설정 2개가 필요.
- zuul.thread-pool.useSeparateThreadPools: true
- zuul.thread-pool.threadPoolKeyPrefix: zuulgw
