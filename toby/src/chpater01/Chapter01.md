<details>
<summary><h2> 1. 스프링이란 무엇인가? p.40 </h2></summary>

| 분류           | 상세                   |
|--------------|----------------------|
| 애플리케이션의 기본 틀 | 스프링 컨테이너             |
| 공통 프로그래밍 모델  | IoC/DI, 서비스 추상화, AOP |
| 기술 API       | 스프링이 지원하는 API        |

스프링을 사용한다는 것은 이 세 요소를 활용하여 애플리케이션을 개발한다는 것이다.
</details>

<details>
<summary><h2>2. 스프링 컨테이너는 무엇인가? (개요) p.40</h2></summary>

스프링은 스프링 컨테이너(애플리케이션 컨텍스트)이라는 런타임 엔진을 제공한다.

설정정보를 참고하여 오브젝트를 생성하고 관리한다.
</details>

<details>
<summary><h2>3. IoC/DI 는 무엇인가? (개요) p.40</h2></summary>

객체의 생명주기와 의존관계에 대한 프로그래밍 모델이다.

위에서 언급한 스프링 컨테이너 또한 IoC/DI를 기반으로 만들어져 있으며 유연하고 확장성이 뛰어난 코드를 만들 수 있도록 유도한다.
</details>

<details>
<summary><h2>4. 서비스 추상화(PSA)는 무엇인가? (개요) p.41</h2></summary>

구체적인 기술과 환경에 종속되지 않도록 유연한 추상 계층을 두는 방법이다.
</details>

<details>
<summary><h2>5. AOP는 무엇인가? (개요) p.41</h2></summary>

애플리케이션 전반적으로 나타나는 부가적인 기능을 독립적으로 모듈화하는 프로그래밍 모델이다.
</details>

<details>
<summary><h2>6. 스프링이 Java를 채택한 이유 p.44</h2></summary>

객체지향 프로그래밍이 가능하다. --> 스프링의 관심 대상은 객체이다.

스프링은 객체를 설계/사용/개선에 대한 기준을 제공한다.

스프링을 사용한다면 자연스럽게 객체지향 기술/설계/구현에 관한 패턴을 자연스럽게 적용하게 된다.
</details>

<details>
<summary><h2>7. 초난감 DAO의 문제점 p.54 ~ p.59</h2></summary>

```java
package com.example.tobyspring.chapter01;

public class User {

    String id;
    String name;
    String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

package com.example.tobyspring.chapter01;

    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;

public class UserDao {

    public void add(User user) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
            "jdbc:mysql://localhost/springbook", "spring", "book"
        );
        PreparedStatement preparedStatement = connection.prepareStatement(
            "insert into users(id, name, password) values(?, ?, ?)");

        preparedStatement.setString(1, user.getId());
        preparedStatement.setString(2, user.getName());
        preparedStatement.setString(3, user.getPassword());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        ;
        connection.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
            "jdbc:mysql://localhost/springbook", "spring", "book"
        );
        PreparedStatement preparedStatement = connection.prepareStatement(
            "select * from users where id = ?");

        preparedStatement.setString(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return user;
    }
}

package com.example.tobyspring.chapter01;

    import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao dao = new UserDao();

        User user = new User();
        user.setId("diger");
        user.setName("김도현");
        user.setPassword("passw0rd");

        dao.add(user);

        System.out.println(user.getId() + "등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user.getName());
        System.out.println(user.getPassword());

        System.out.println(user2.getId() + "조회 성공");
    }
}
```

객체지향적이지 않다. 그러므로 변화에 대비되어있지 않다.
</details>

<details>
<summary><h2>8. 관심사의 분리는 무엇일까? p.60 ~ p.70</h2></summary>

1. 객체지향 세계에서는 모든 것이 변한다.

2. 객체를 설계할 때는 미래의 변화를 어떻게 대비할 것이냐에 주목해야한다.

3. 변화에 대비하는 것에 가장 좋은 방법은 변화의 폭을 최소한으로 줄이는 것이다.

4. 3.의 내용을 결론을 코드에 적용하려면 관심사를 분리하는 것과 확장을 고려한 설계가 필요하다.

관심사의 분리는, 관심이 같은 것 끼리 하나의 객체안으로 혹은 가까운 객체로 모이도록 하고

관심이 다른 것은 가능한 서로 영향을 주지 않도록 분리하는 것을 말한다.

</details>

<details>
<summary><h2>9. 관심사의 분리를 초난감 DAO에 적용하기 p.62</h2></summary>

### UserDao의 관심사항

- DB와 연결을 위한 커넥션

- Statement와 입력값을 바인딩, Statement를 실행

- Statement, Connection의 오브젝트를 닫아주기

- 예외 처리

### 관심사 분리 적용 - 커넥션

```java
package com.example.tobyspring.chapter01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "insert into users(id, name, password) values(?, ?, ?)");

        preparedStatement.setString(1, user.getId());
        preparedStatement.setString(2, user.getName());
        preparedStatement.setString(3, user.getPassword());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        ;
        connection.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "select * from users where id = ?");

        preparedStatement.setString(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return user;
    }

    // 커넥션에 대한 관심사 분리를 위한 priavte 메서드
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
            "jdbc:mysql://localhost/springbook", "spring", "book"
        );
        return connection;
    }
}
```

여러 메서드에 등장하는 중복되는 관심사항을 별도의 메서드로 분리하는 것이 관심사 분리의 시작이다.

그런데, 만약 Connection을 가져오는 과정에서 각 클라이언트 마다 다른 방법으로 가져올 수 있도록 하는 방법은 무엇인가?

즉, 커넥션을 가져오는 로직을 단 하나만 사용하는게 아닌 여러 개로 사용하는 방법은?

이 때, 상속 기능을 활용하여 할 수 있는 방법이 있다.

### 관심사 분리 적용(템플릿 메서드 패턴) - 커넥션

```java
package com.example.tobyspring.chapter01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class UserDao {

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "insert into users(id, name, password) values(?, ?, ?)");

        preparedStatement.setString(1, user.getId());
        preparedStatement.setString(2, user.getName());
        preparedStatement.setString(3, user.getPassword());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "select * from users where id = ?");

        preparedStatement.setString(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return user;
    }

    public abstract Connection getConnection() throws ClassNotFoundException, SQLException;
}

package com.example.tobyspring.chapter01;

    import java.sql.Connection;
    import java.sql.SQLException;

public class NUserDao extends UserDao {

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        // N 사 DB Connection 생성 코드
        return null;
    }
}

package com.example.tobyspring.chapter01;

    import java.sql.Connection;
    import java.sql.SQLException;

public class DUserDao extends UserDao {

    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        // D 사 DB Connection 생성 코드
        return null;
    }
}
```

- 기존 getConnection()메서드를 추상 메서드로 변경한다.

- 추상 메서드로 선언되어있기 때문에 기존의 add(), get() 메서드에서 getConnection()메서드를 호출하는 것에 문제는 없다.

- N, D 라는 유저가 있고 각 유저마다 고유한 커넥션 가져오기 로직을 구현체로 만들어 사용하기만 하면 된다.

이렇게 슈퍼 클래스에 기본적인 로직(변하지 않는 부분)의 흐름을 만들고

서브 클래스에서 니즈에 맞게 구현하여 사용하는 방법을 **템플릿 메서드 패턴**이라고 한다.

또한 현재 NUserDao, DUserDao는 구체적인 객체 생성 방법을 결정하기도 하는데 이를 **팩터리 메서드 패턴**이라고도 한다.

</details>

<details>
<summary><h2>10. 상속을 통한 해결방법의 문제점 p.70</h2></summary>

- UserDao가 이미 다른 클래스로부터 상속 받고 있다면, 자바는 다중 상속을 지원하지 않기 때문에 상속을 통한 해결방법에는 한계가 있다.

- 또한 상속을 통한 상하위 클래스의 관계는 강하게 결합되어있다.
    - 상위 클래스의 변경이 있을 때 모든 서브 클래스를 수정해야할 상황이 발생할 수도 있다.

- 확장하여 만든 DB 커넥션 기능을 다른 DAO 클래스에서는 적용할 수 없다.
    - 따라서 상속을 통해 만들어진 getConnection()의 구현코드는 매 DAO 클래스마다 중복될 수 있다.

</details>

<details>
<summary><h2>11. 확장을 고려한 DAO p.71 ~ p.82</h2></summary>

모든 객체는 변하지만, 동일한 방식으로 변하지는 않는다. 이는 변화의 이유/시기/주기 등이 다르다는 것이다.

UserDao의 관심사는 JDBC를 사용할 것인가?/DB 전용 API를 사용할 것인가?/어떤 SQL을 만들 것인가? 등을 모아둔 것이다.

상속 관계가 아닌 독립적인 클래스로 관심사를 분리하여 적용한다면 다음과 같다.

### 독립적인 클래스로 분리하기

```java
package com.example.tobyspring.chapter01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private SimpleConnectionMaker simpleConnectionMaker;

    public UserDao() {
        simpleConnectionMaker = new SimpleConnectionMaker();
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection connection = simpleConnectionMaker.makeNewConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "insert into users(id, name, password) values(?, ?, ?)");

        preparedStatement.setString(1, user.getId());
        preparedStatement.setString(2, user.getName());
        preparedStatement.setString(3, user.getPassword());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection connection = simpleConnectionMaker.makeNewConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "select * from users where id = ?");

        preparedStatement.setString(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return user;
    }
}

package com.example.tobyspring.chapter01;

    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.SQLException;

public class SimpleConnectionMaker {

    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
            "jdbc:mysql://localhost/springbook", "spring", "book"
        );
        return connection;
    }
}

```

이렇게 커넥션의 관한 관심사를 독립적인 클래스로 분리한 것의 문제점은 무엇일까?

- NUserDao, DUserDao에서 각 사용자마다 고유의 커넥션 로직으로 커넥션을 만들 수가 없다. SimpleConnectionMaker라는 특정 클래스에 종속되어있기
  때문이다.

- DB 커넥션을 제공하는 클래스가 어떤 것인지 UserDao가 구체적으로 알고 있어야한다.

이 문제들의 근본적인 원인은 UserDao가 바뀔 수 있는 정보에 대해 너무 많이 알고 있다. 구체적인 클래스를 알아야한다는 점과 그 클래스에서 어떤 메서드가 커넥션을 가져오는지
까지 알아야한다.

### 독립적인 클래스로 분리한 것을 개선한다. - 인터페이스 적용

```java
package com.example.tobyspring.chapter01;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {

    public Connection makeConnection() throws ClassNotFoundException, SQLException;

}

package com.example.tobyspring.chapter01;

import java.sql.Connection;
import java.sql.SQLException;

public class DConnectionMaker implements ConnectionMaker {

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        // D사의 커넥션 로직
        return null;
    }
}

package com.example.tobyspring.chapter01;

import java.sql.Connection;
import java.sql.SQLException;

public class NConnectionMaker implements ConnectionMaker {

    @Override
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        // N사의 커넥션 로직
        return null;
    }
}


package com.example.tobyspring.chapter01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private ConnectionMaker connectionMaker;

    public UserDao() {
        connectionMaker = new DConnectionMaker();
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection connection = connectionMaker.makeConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "insert into users(id, name, password) values(?, ?, ?)");

        preparedStatement.setString(1, user.getId());
        preparedStatement.setString(2, user.getName());
        preparedStatement.setString(3, user.getPassword());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection connection = connectionMaker.makeConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "select * from users where id = ?");

        preparedStatement.setString(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return user;
    }
}
```

인터페이스로 분리하여 강한 결합도를 지니는 상황도 어느정도 해결하고자 했지만 여전히 문제가 남아있다.

UserDao 에서 ConnectionMaker의 구현체를 알고 있어야한다는 점이다.

그 이유는 UserDao안에 또 다른 관심사항이 존재하는 것이 원인이다.

그 관심사항은, UserDao가 사용할 ConnectionMaker의 구현체의 관계를 맺는 관심사를 말한다.

객체간의 관계는 런타임 시 다른 오브젝트의 참조를 갖고 있는 방식으로 만들어진다.

그러기 위해선 이미 만들어진 객체가 있어야하는데, 위 코드처럼 생성자를 직접 호출하는 방법도 있지만 더 좋은 방법이 있다.

외부에서 만들어준 객체를 가져오는 방법인데, 이는 UserDao내에서 굳이 관계를 맺기위한 객체를 만들 필요를 없애준다.

이는 의존관계로도 불리는데, 앞서 말한 듯이 특정 객체에 관하여 그 객체와 관련있는 관계를 외부에서 맺어주는 것을 말한다.

### UserDao - 외부로부터 의존관계를 설정

```java
package com.example.tobyspring.chapter01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private ConnectionMaker connectionMaker;

    public UserDao(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection connection = connectionMaker.makeConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "insert into users(id, name, password) values(?, ?, ?)");

        preparedStatement.setString(1, user.getId());
        preparedStatement.setString(2, user.getName());
        preparedStatement.setString(3, user.getPassword());

        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection connection = connectionMaker.makeConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(
            "select * from users where id = ?");

        preparedStatement.setString(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        User user = new User();
        user.setId(resultSet.getString("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));

        resultSet.close();
        preparedStatement.close();
        connection.close();

        return user;
    }
}

package com.example.tobyspring.chapter01;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao dao = new UserDao(connectionMaker);

        User user = new User();
        user.setId("diger");
        user.setName("김도현");
        user.setPassword("passw0rd");

        dao.add(user);

        System.out.println(user.getId() + "등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user.getName());
        System.out.println(user.getPassword());

        System.out.println(user2.getId() + "조회 성공");
    }
}
```

위 코드로써 UserDao를 사용하는 클라이언트에게 UserDao가 사용할 ConnectionMaker를 연결시켜주는 책임을 넘겼다.

또한 UserDao는 위 코드를 통해 DB와의 질의 소통만 할 수 있게 되었다.
</details>

<details>
<summary><h2>12. 개선된 초난감 DAO로 부터 얻어갈 이론은? p.83 ~ p.86</h2></summary>

## OCP (개방 폐쇄 원칙)

- 클래스나 모듈은 확장에는 열려있어야하고, 변경에는 닫혀 있어야한다.

UserDao는 DB Connection에 관한 기능을 확장하는 것에는 열려있다. UserDao가 직접 손쓰는 것이 아닌

UserDao를 사용하는 클라이언트에서 니즈에 따라 바꿔 끼워주면 되기 때문이다.

또한 이를 통해 UserDao는 자신의 코드를 수정하지 않고도 Connection에 관계없이 책임을 완수할 수 있다.

- 개방 폐쇄 원칙은 높은 응집도와 낮은 결합도 라는 원리로도 설명이 가능하다.

### 높은 응집도는 무엇인가??

- 하나의 모듈이 하나의 책임/관심사에만 집중하고 있다.

변화가 일어날 때 해당 모듈에서 변하는 부분이 크다. 즉 어떤 변경사항으로 인해 변경이 발생하면 모듈의 여러 부분에서 변경이 된다는 뜻이다.

이게 왜 좋은 설계이냐면, 만약 변경이 일어났을 때 변하는 부분이 적다면, 어떤 부분이 변하는지 그 변경으로 다른 요소에 영향은 어느정도인지 파악하기가 매우 힘들어지기 때문이다.

위의 UserDao에서는 본래의 고유한 책임인 사용자의 데이터를 처리하는 기능이 DAO안에 모여있다. 그 자체로 응집도가 높다는 것이다.

### 낮은 결합도는 무엇인가??

느슨한 연결 관계를 유지해야하는 것이다. 이를 만족시킬 방법으로는, 최소한의 방법만 간접적인 형태로 제공하여

나머지는 알 필요도 없도록 만드는 것인데, 바로 이전에 봤던 인터페이스를 활용한 관계설정이 그 예시이다.

구체적인 구현체를 알 필요도 없이 제공받은 객체를 사용하기만 하면 되었던 것이 그 포인트이다.
</details>

<details>
<summary><h2>13. 전략 패턴은 무엇인가? p.87</h2></summary>

Main - UserDao - ConnectionMaker는 전략패턴에 해당한다.

전략 패턴은 자신의 기능 컨텍스트에서, 필요에 따라 변경이 필요한 로직을 인터페이스를 통해 외부로 분리시켜

이에 관한 구현체를 두어 필요에 따라 구현체를 바꿔 낄 수 있도록 하는 디자인 패턴이다.

UserDao는 컨텍스트, Main은 클라이언트, ConnectionMaker는 전략으로 보면되는데

컨텍스트는 자신의 기능을 수행하는데 필요한 기능 중

변경 가능한, DB연결 방식이라는 로직을 ConnectionMaker라는 인터페이스로 받고

클라이언트에서 이 구현체를 바꿔가며 사용할 수 있도록 하는 것이다.
</details>

<details>
<summary><h2>14. 오브젝트 팩토리 p.88 ~ p.92</h2></summary>

Main은 UserDao가 사용할 Connection을 넘겨주는 책임을 맡게 되었다.

하지만 Main의 본래 존재 목적은 이게 아니라, 테스트를 위한 용도였다. 그러므로 이를 분리해야한다.

분리될 내용은 객체간에 사용될 구현체를 만드는 것과 관계를 맺어주는 것이다.

이 역할을 수행할 클래스를 팩터리라고 부른다.

### 일반적인 팩터리
```java
package com.example.tobyspring.chapter01;

public class DaoFactory {

    public UserDao userDao() {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        return userDao;
    }

}
```
내용은 위와 같다. DaoFactory의 userDao()메서드를 실행하면, UserDao가 사용할 객체를 만들고, 그 객체와의 관계를 맺어준다.

### 조금 더 기능이 있는 팩터리
```java
package com.example.tobyspring.chapter01;

public class DaoFactory {
    
    public UserDao userDao() {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        return userDao;
    }

    public AccountDao accountDao() {
        return new AccountDao(new DConnectionMaker());
    }

    public MessageDao messageDao() {
        return new MessageDao(new DConnectionMaker());
    }
}
```

UserDao 말고도 다른 Dao를 다루기 위한 객체를 생성하고 관계를 셋팅하는 방법도 별 다른 건 없다.

그런데 이렇게 요구사항이 늘어나다보니 중복이 등장하게된다. 이를 개선해보면 다음과 같다.

### 조금 더 기능이 있는 팩터리 - 개선
```java
package com.example.tobyspring.chapter01;

public class DaoFactory {

     public UserDao userDao() {
        return new UserDao(connectionMaker());
    }

    public AccountDao accountDao() {
        return new AccountDao(connectionMaker());
    }

    public MessageDao messageDao() {
        return new MessageDao(connectionMaker());
    }

    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
```
</details>

<details>
<summary><h2>15. IoC p.92 ~ p.94</h2></summary>

## 제어의 역전이란 무엇일까?

프로그램의 제어 흐름 구조가 바뀐다는 것을 의미한다.

일반적인 프로그램 흐름은 main()메서드와 같이 프로그램이 시작되는 지점이 있고 차례대로 다음에 사용될 객체를 결정/생성/호출 하는 작업을 반복한다.

제어의 역전이 적용된 프로그램은 사용되는 객체가 자신이 사용할 객체를 스스로 선택하지 않는다.

이전의 코드에서 UserDao클래스의 생성자에서 new DConnectionMaker()를 직접 호출하던 부분을 외부로부터 DI받는 방식으로 변경한 것이 그 예시이다.

이는 모든 객체 제어 권한을 자신이 갖고 있는 것이 아니라 제 3자에게 위임하기 때문이다. 그리고 직전 예시로 살펴보면 DaoFactory가 이 모든 책임을 위임받고 수행하고 있었다.

일반적인 흐름과 제어의 역전이 적용된 흐름을 비교하다보면 라이브러리와 프레임워크의 차이점을 설명하는 듯하다.

## 라이브러리 vs 프레임워크

라이브러리는 애플리케이션 코드를 직접 흐름제어해야한다.

가령 예를 들면,

```java
Scanner scanner = new Scanner(new System.in);

scanner.nextLine();
```
와 같이 직접 라이브러리에 해당하는 객체도 만들고 사용하고자 하는 메서드도 직접 명시해야한다.

하지만 프레임워크는 애플리케이션 코드가 프레임워크에 의해 사용되는 것으로

프레임워크위에 애플리케이션 코드가 있고, 프레임워크가 이를 흐름제어하며 실행시켜 주는 것이다.

이를 위해선 프레임워크에는 반드시 IoC 개념이 적용되어 있어야한다.

IoC는 프레임워크나 컨테이너 같이 애플리케이션 전반적인 객체 생성/관계설정/사용/생명주기 등을 관리하는 존재가 필요하다.

그리고 스프링이 이 역할을 제공하고 있는 것이다.
</details>

<details>
<summary><h2>16. 스프링 IoC p.95 ~ p.101</h2></summary>

스프링이 제어권을 가지고 생성/관계부여 하는 객체를 Bean이라고 부른다.

스프링에서 Bean의 생성과 관계설정을 담당하는 IoC오브젝트를 Bean Factory라고 부른다.

일반적으로는 Bean Factory를 더 확장한 Application Context를 주로 사용한다.

Application Context는 별도의 설정 정보를 참고하여 Bean 생성/관계설정의 작업을 총괄하기 때문에

별도로 설정정보를 담고 있는 어떤 것을 가져와 이를 활용하는 IoC엔진으로 볼 수 있다.

이와 같이 별도의 설정정보를 만드는 방법은 다음과 같다.

### @Configuration, @Bean 을 활용하여 설정정보 만들기
```java
package com.example.tobyspring.chapter01;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoFactory {

  @Bean
  public UserDao userDao() {
    ApplicationContext applicationContext =
            new AnnotationConfigApplicationContext(DaoFactory.class);

    UserDao dao = applicationContext.getBean("userDao", UserDao.class);
    return new UserDao(connectionMaker());
  }

  @Bean
  public ConnectionMaker connectionMaker() {
    return new DConnectionMaker();
  }
}
```

@Configuration 애노테이션이 붙은 클래스를 설정정보로 사용하기 위해선

AnnotationConfigApplicationContext라는 객체를 활용하면 된다.

ApplicationContext는 DaoFactory 클래스를 설정정보로 등록하고 @Bean이 붙은 메서드의 이름을 가져와 Bean 목록을 만들어둔다.

### Object Factory vs ApplicationContext (개요)

- Object Factory는 클라이언트가 필요한 오브젝트를 가져오려면 어떤 팩터리 클래스를 사용해야하는지 알아야한다.

- Object Factory는 필요할 때 마다 팩토리 오브젝트를 생성해야한다.

- ApplicationContext는 이 두 과정이 필요없다. 애플리케이션에 전반적으로 적용되는 Context에서 등록하고 꺼내 쓰기만 하면 된다.

- ApplicationContext는 오브젝트가 생성되는 시점/방식/전략/후처리/인터셉팅 등 부가적인 기능을 제공한다.

- ApplicationContext는 getBean()메서드를 통해 특정 Bean을 찾는 기능도 지원한다.
</details>

<details>
<summary><h2>17. Object Factory vs ApplicationContext p.102 ~ p.105</h2></summary>

Object Factory에서 userDao를 여러 번 호출한다면?

각각 다른 값을 가진 오브젝트가 생성된다.

팩터리를 통해 userDao() 메서드를 호출하면 매번 새로운 인스턴스를 반환한다는 것이다.

ApplicationContext에서 getBean()메서드를 여러 번 호출한다면?

각각 같은 값을 가진 오브젝트가 생성된다.

ApplicaitonContext라는 특정 저장소에서 Bean 이름에 해당하는 객체를 꺼내다 만드는 것이기 때문이다.

정리하자면, Object Factory는 동등성을 만족하지만 동일성은 만족하지 않는 객체를 만들게 되고

ApplicationContext는 동등성/동일성을 모두 만족하는 객체를 가질 수 있게 도와준다.

이 결과로 알 수 있듯이 ApplicationContext는 싱글턴으로 객체를 관리한다.

### ApplicationContext가 객체를 싱글턴으로 관리하는 이유는 무엇인가?

Spring은 기본적으로 서버를 제작하기 위해 고안되었다.

그렇기 때문에 클라이언트를 대상으로 비즈니스 로직을 제공해야하는데, 이 때마다 매번 객체를 생성하기에는 그 부담이 커 싱글턴으로 객체를 관리하기로 했다.
</details>

<details>
<summary><h2>18. 싱글턴과 Bean 스코프 p.106 ~ p.110</h2></summary>

### 싱글턴 패턴의 문제점

- private 생성자를 갖기 때문에 상속이 불가능하다.

- 테스트하기 힘들다.
    - 테스트에서 사용될 때 Mock 객체로 대체하기가 힘들다.
    - 생성자 주입과 같이 동적인 의존성 주입이 쉽지 않다.

- 전역적으로 사용될 수 있고, 하나의 객체만 만들어진다는 것을 보장할 수 없다.

### 싱글턴 레지스트리 - 스프링에서 싱글턴의 단점을 보완해준다.

싱글턴 레지스트리는 위에서 언급한 기존 싱글턴 패턴의 문제점을 보완하여 단점들을 커버해주는 기능을 제공한다.


### Bean은 생명주기가 있다.

Bean은 생성/존재/적용 되는 범위가 있고 이것을 Bean의 스코프라고 한다.

일반적인 Bean의 스코프는 싱글턴으로 런타임 시점에 단 하나의 객체가 만들어지며 강제로 제거하지 않는 한 스프링이 실행되는 동안 계속 유지된다.

그리고 프로토타입 스코프가 있는데 이는 컨테이너에 Bean을 요청할 때마다 매번 새로운 Object를 만들어주는 스코프이며

HTTP 요청이 생길 때 마다 생성되는 요청 스코프, 이와 유사한 세션 스코프도 존재한다.
</details>

<details>
<summary><h2>20. 의존관계 주입 p.111 ~ p.128</h2></summary>

스프링의 IoC 기능을 동작하게 하는 핵심은 의존관계 주입에 있다.

DI는 객체를 외부로부터 제공(주입)받아 동적으로 의존관계를 맺을 수 있도록 하는 방법이다.

의존관계 주입은 다음 세 가지 조건을 충족해야한다.

- 런타임 시 의존관계가 드러나지 않는다. (인터페이스에만 의존해야한다.)

- 런타임 시점의 의존관계는 컨테이너/팩터리 와 같은 제 3자가 결정한다.

- 의존관계는 객체에 관한 참조를 외부에서 제공한다.


### 의존관계 주입의 장점

- 낮은 결합도/OCP

- 필요에 따른 동적인 객체 주입

- 부가기능 추가의 간편함

### 의존관계 주입 방법

- Setter 주입

```java
public class UserDao {
    private ConnectionMaker connectionMaker;

  public void setConnectionMaker(ConnectionMaker connectionMaker) {
      this.connectionMaker = connectionMaker;
  }
  
  @Bean
  public UserDao userDao() {
    UserDao userDao = new UserDao();
    userDao.setConnectionMaker(connectionMaker());
    return userDao;
  }
}
```

- 일반 메서드 주입

```java
public class UserDao {
    private ConnectionMaker connectionMaker;
    private AdditionalDependency1 additionalDependency1;
    private AdditionalDependency2 additionalDependency2;

  public void initUserDaoDependency(
      ConnectionMaker connectionMaker, 
          AdditionalDependency1 additionalDependency1,
          AdditionalDependency2 additionalDependency2
  ) {
      this.connectionMaker = connectionMaker;
      this.additionalDependency1 = additionalDependency1;
      this.additionalDependency2 = additionalDependency2;
  }
}
```

- 생성자 주입
```java
public class UserDao {
    private final ConnectionMaker connectionMaker;
    private final AdditionalDependency1 additionalDependency1;
    private final AdditionalDependency2 additionalDependency2;

  public void UserDao(
      ConnectionMaker connectionMaker, 
          AdditionalDependency1 additionalDependency1,
          AdditionalDependency2 additionalDependency2
  ) {
      this.connectionMaker = connectionMaker;
      this.additionalDependency1 = additionalDependency1;
      this.additionalDependency2 = additionalDependency2;
  }
}
```
</details>