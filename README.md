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
