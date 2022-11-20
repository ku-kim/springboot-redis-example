# springboot-redis-example
이 저장소는 springboot에서 redis를 사용하고 테스트하는 예제입니다.  

Java : 17  
Spring Boot : 2.7.5  

### docker-compose를 활용하여 redis 사용하기
```bash

# 도커 컴포즈를 활용하여 redis 컨테이너 실행
## 백그라운드 실행 시 -d 옵션 추가
## 레디스 옵션 설정하려면 ./container/conf/redis.conf 를 수정
docker-compose -f ./container/redis.yml up # -d

## 실행된 도커 프로세스 확인
docker ps     
CONTAINER ID   IMAGE         COMMAND                  CREATED         STATUS         PORTS                    NAMES
97213b72da1e   redis:7.0.5   "docker-entrypoint.s…"   6 seconds ago   Up 4 seconds   0.0.0.0:6379->6379/tcp   redis-server

# 레디스 컨테이너가 실행된 뒤 redis-cli를 통해 레디스에 접속
docker exec -it redis-server redis-cli

$127.0.0.1:6379>
```
