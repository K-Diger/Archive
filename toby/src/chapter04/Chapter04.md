# 4.1 사라진 SQLException

### 이전 코드
```java
public void deleteAll() throws SQLException {
    this.jdbcContenxt.executeSql("delete from users");    
}
```

### JdbcTemplate 적용 코드

```java
public void deleteAll() {
    this.jdbcTemplate.update("delete from users")    
}
```

SQLException은 어디로 간거지?

# 4.1.1 초난감 예외처리

### 예외 블랙홀
```java
try {
    
} catch(SQLException e) {}
```

예외를 잡고 아무것도 하지 않기 때문에 예외 처리를 했다고 볼 수 없다. 위험한 코드이다.

### 초난감 예외처리 코드 2

```java
} catch (SQLException e) {
    System.out.println(e);    
}
```

### 초난감 예외처리 코드 3

```java
} catch (SQLException e) {
    e.printStackTrace();  
}
```

위와 같은 코드도 로깅을 하는게 아니기 때문에 콘솔을 계속 지켜보지 않는 이상 의미없는 위험한 코드이다.

### 그나마 괜찮은 예외처리

```java
} catch (SQLExeption e) {
    e.printStackTrace();
    System.exit(1);
}
```

위 코드는 그나마 괜찮은거지 이것도 초난감하다.

### 초난감 예외처리 4
```java
public void method1() throws Exception {
    method2();    
}

public void method2() throws Exception {
    method3();    
}

public void method3() throws Exception {
    
}
```

모든 메서드에 이런식으로 최상위 예외인 Exception을 던져버리면 곤란하다. 무슨 예외가 터지는지 파악하기 힘들다.

# 4.1.2 예외의 종류와 특징

## 예외를 발생시킬 수 있는 세 가지

### Error

java.lang.Error 클래스의 서브클래스를 활용하여 처리하는 것인데, 주로 시스템에 뭔가 비정상적인 상황이 발생했을 때 사용하여 딱히 신경쓰지 않아도 되는 방법이다.

### Exception과 체크 예외

java.lang.Exception 클래스와 그 서브클래스로 정의되는 예외들은 코드를 짜다가 발생하는 예외를 가리킨다.

Exception 클래스는 **체크 예외**와 **언체크 예외**로 구분된다.

- **체크 예외**는 Exception 클래스의 서브클래스이며, RuntimeException 클래스를 상속하지 않는 것이다. 

- **언체크 예외**는 RuntimeException을 상속한 클래스들을 말한다.

일반적으로 예외라고 한다면, Exception 클래스의 서브클래스 중, RuntimeException을 상속하지 않은 것만을 가리키는 **체크 예외**라고 생각하면 된다.

**체크 예외**는 catch문으로 잡던지, 다시 thorws를 정의하여 메서드 밖으로 던져야한다.

언체크 예외는 프로그램의 오류가 있을 때 발생하도록 의도된 것으로, NPE, IllegalArgument 등이 있다.

# 4.1.3 예외처리 방법

## 예외 복구

예외를 파악하고, 정상 상태를 돌려놓는 것이다.

- 파일 읽기 시 IOException이 발생했을 때

사용자에게 상황을 알려주고 다른 파일을 읽도록 유도

- DB 연결 실패 시 SQLException

연결 재시도

### 재시도를 통해 예외 복구

```java
int maxretry = MAX_RETRY;
while(maxretry -- > 0) {
    try {
        return;
    } catch(SomeException e) {
        // 로그 출력 후 정해진 시간만큼 대기
    } finally {
        // 리소스 반납, 정리 작업
    }
}
throw new RetryFailedException(); // 최대 재시도 횟수를 넘기면 직접 예외를 발생시킨다.
```

## 예외 회피

예외처리를 자신이 처리하지 않고 자신을 호출한 곳으로 던지는 것이다.

### 예외 회피 1
```java
public void add() throws SQLException {
    // JDBC API    
}
```

```java
public void add() throws SQLException {
    try {
        // JDBC API
    } catch(SQLException e) {
        // 로그 출력
        throw e;
    }
}
```

예외 회피는 예외를 복구 하는 것 처럼 **의도가 분명해야**한다. 콜백/템플릿 처럼 관계가 있는 다른 오브젝트에게 책임을 분명하게 지게 해야한다.

## 예외 전환

### 예외 전환의 목적 2가지

- 내부에서 발생한 예외를 그대로 던지는 것이, 그 예외 상황에 대한 적절한 의미를 부여하지 못하여 의미를 분명하게 해줄 수 있는 예외로 바꿔준다.

새로운 사용자가 등록을 시도했을 때, 같은 아이디의 사용자가 이미 존재하는 경우 DB에러가 발생하면서 JDBC API는 SQLException을 발생시킨다.

이때 SQLException을 그대로 던지는 것보다, DuplicateUserIdException과 같은 예외로 바꿔서 던져주면 인식하기 더 좋다.


- 예외를 처리하기 쉽고 단순하게 만들기 위해 포장하는 것이다.

의미를 명확하게만 하려고 예외로 전환하는 것은 아니다. 포장을 함으로써 예외처리를 강제하는 체크 예외를 언체크 예외인 런타임 예외로 바꿀 수 있다.

### 예외 포장

```java
try {
    OrderHome orderHome = EJBHomeFactory.getInstance().getOrderHome();
    Order order = orderHome.findByPrimaryKey(Integer id);
} catch (NamingException ne) {
    throw new EJBException(ne);
} catch (SQLException se) {
    throw new EJBException(ne);    
} catch (RemoteException re) {
    throw new EJBException(ne);
}
```

EJBException은 RuntimeException 클래스를 상속했기 때문에 런타임 예외이다.

따라서 런타임 예외이기 때문에 예외가 발생한다면 트랜잭션을 자동으로 롤백해준다.

# 4.1.4 예외처리 전략

### 예외 커스텀하기

```java
public class DuplicateUserIdException extends RuntimeException {

    public DuplicateUserIdException(Throwable cause) {
        super(cause);
    }
}
```
