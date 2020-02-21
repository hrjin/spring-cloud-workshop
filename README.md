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