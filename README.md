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
