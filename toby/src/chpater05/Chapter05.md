# 5.1 사용자 레벨 관리 기능 추가

### 사용자의 활동내역으로 레벨을 조정해주는 기능 추가

- 레벨은 BASIC, SILVER, GOLD
 
- 첫 가임 시 BASIC, 활동에 따라서 업그레이드 된다.

- 50회 이상 로그인 시 SILVER가 된다.

- SILVER 레벨이면서 30번 이상 추천을 받으면 GOLD가 된다.

- 사용자 레벨의 변경 작업은 일정한 주기를 가지고 일괄적으로 진행된다.

## 상수 값으로 해결 vs Enum으로 해결

### 상수

```java
class User {
    private static final int BASIC = 1;
    private static final int SILVER = 2;
    private static final int GOLD = 3;
    
    int level;

    public void setLevel(int level) {
        this.level = level;
    }
}
```

### Enum

```java
public enum Level {
    BASIC(1), SILVER(2), GOLD(3);
    
    private final int value;
    
    Level(int value) {
        this.value = value;
    }
    
    public static Level valueOf(int value) {
        switch (value) {
            case 1: return BASIC;
            case 2: return SILVER;
            case 3: return GOLD;
        }
    }
}

public class User {
    String id;
    String name;
    String password;
    Level level;
    int login;
    int recommend;


    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    } 
    
    // getter/setter
}

public class UserDaoTest {
    
    User user1;
    User user2;
    User user3;
    
    // 새롭게 추가된 필드를 넣어준다.
    @Before
    public void setUp() {
        this.user1 = new User("userA", "유저A", "springno1", Level.BASIC, 1, 0);
        this.user2 = new User("userB", "유저B", "springno2", Level.SILVER, 55, 10);
        this.user3 = new User("userC", "유저C", "springno3", Level.GOLD, 101, 40);
    }
    
    @Test
    public void addAndGet() {
        User userGet1 = dao.get(user1.getId());
        checkSameUser(userGet1, user1);

        User userGet2 = dao.get(user2.getId());
        checkSameUser(userget2, user2);
    }
}
```

여기서 Enum, 테스트코드에 추가된 내용을 반영하기 위해 UserDaoJdbc를 수정하면 다음과 같다.

```java
import java.sql.SQLException;
import javax.swing.tree.RowMapper;

public class UserDaoJdbc implements UserDao {

    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt(recommend));
            return user;
        }
    };

    public void add(User user) {
        this.jdbcTemplate.update(
            "insert into users(id, name, password, level, login, recommend) " +
                "values(?,?,?,?,?,?)", user.getId(), user.getName(),
            user.getPassword(), user.getLevel().intValue(),
            user.getLogin(), user.getRecommend());
    }
}
```

여기서, Level은 위에서 구현한 Enum이므로 DB에 저장될 수 없는 이유로 Level에 미리 만들어둔 intValue()메서드를 사용해야한다.

반대로 조회 시에는 ResultSet에서 DB에 저장된 타입은 int로 level 정보를 가져와야 한다.

또한, 조회 시 User의 setLevel()을 바로 사용하면 타입이 일치하지 않기 때문에

Level에 미리 만들어둔 valueOf() 메서드를 사용하여 Enum으로 변경해줘야한다.

위 방법대로 테스트를 돌려보면 결과는 성공이다.

# 5.1.2 사용자 수정 기능 추가

### 테스트코드로 로직 스케치하기

```java
@Test
public void update() {
    dao.deleteAll();
    dao.add(user1);
    
    user1.setName("UserName1");
    user1.setPassword("P@ssw0rd")
    user.setLevel(Level.Gold);
    user.setLogin(1000);
    user.setRecommend(999);
    dao.update(user1);
    
    User user1update = dao.get(user1.getId());
    checkSameUser(user1, user1update);
}
```

id를 제외한 모든 필드를 새롭게 바꾸며 user1과 업데이트가 적용된 user1update와 비교하는 것이 위 테스트 로직이다.

```java
public void update(User user) {
    this.jdbcTemplate.update(
        "update users set name = ?, password = ?, level = ?, login = ?, " +
        "recommend = ? where id = ? ", user.getName(), user.getPassword(), 
        user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
    )
}
```

테스트코드 내용을 바탕으로 다음과 같은 실제 Dao 메서드를 추가한다.

Ap