# 5.1 사용자 레벨 관리 기능 추가

### 사용자의 활동내역으로 레벨을 조정해주는 기능 추가

- 레벨은 BASIC, SILVER, GOLD

- 첫 가입 시 BASIC, 활동에 따라서 업그레이드 된다.

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
            case 1:
                return BASIC;
            case 2:
                return SILVER;
            case 3:
                return GOLD;
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
public void update(){
    dao.deleteAll();
    dao.add(user1);

    user1.setName("UserName1");
    user1.setPassword("P@ssw0rd")
    user.setLevel(Level.Gold);
    user.setLogin(1000);
    user.setRecommend(999);
    dao.update(user1);

    User user1update=dao.get(user1.getId());
    checkSameUser(user1,user1update);
    }
```

id를 제외한 모든 필드를 새롭게 바꾸며 user1과 업데이트가 적용된 user1update와 비교하는 것이 위 테스트 로직이다.

```java
public void update(User user){
    this.jdbcTemplate.update(
    "update users set name = ?, password = ?, level = ?, login = ?, "+
    "recommend = ? where id = ? ",user.getName(),user.getPassword(),
    user.getLevel().intValue(),user.getLogin(),user.getRecommend(),user.getId());
    )
    }
```

테스트코드 내용을 바탕으로 다음과 같은 실제 Dao 메서드를 추가한다.

또한 테스트를 돌려보면 성공적으로 돌아가게 된다.

사용자 수정 요구사항을 해결할 DAO를 바탕으로 서비스를 구현하면 아래와 같다.

# 5.1.3. UserService.upgradeLevels()

구현하기 전에, 짚고 넘어가야할 점은 **사용자 관리 로직을 어디다가 두는 것이 좋을까?**에 대한 내용이다.

DAO는 데이터를 어떻게 가져오고 조작할지를 다루는 곳이기 때문에 적합하지 않다.

따라서 사용자 관리 **비즈니스 로직을 담을 클래스**를 추가하는 것이 적절하다.

이 클래스의 이름은 **UserService**로 하는데, **UserService**는 **UserDao의 구현체**가 바뀌어도 영향받지 않아야한다.

```java
public class UserService {

    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    // 사용자 레벨 업그레이드 메서드
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            } else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            } else if (user.getLevel() == Level.GOLD) {
                changed = false;
            } else {
                changed = false;
            }
            if (changed) {
                userDao.update(user);
            }
        }
    }
}
```

- upgradeLevels() 메서드는 모든 사용자 정보를 DAO를 통해 받아온 후 한 명씩 레벨 변경 작업을 수행한다.

- 현재 사용자의 레벨이 변경되었는지 확인한다.

- BASIC 레벨이면서 로그인 조건을 만족한다면, 레벨을 SILVER로 변경하고 레벨 변경 플래그를 true로 바꾼다.

- SILVER 레벨이면서 로그인 조건을 만족한다면, 레벨을 GOLD로 변경하고 레벨 변경 플래그를 true로 바꾼다.

- GOLD 등급은 레벨 변경이 이루어지지 않는다. 어떤 조건이라도 충족하지 못한 경우도 마찬가지이다.

```java

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

    @Autowired
    UserService userService;

    List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
            new User("uA", "현", "p1", Level.BASIC, 49, 0),
            new User("uB", "윤", "p2", Level.BASIC, 50, 0),
            new User("uC", "우", "p3", Level.SILVER, 60, 29),
            new User("uD", "현", "p4", Level.SILVER, 60, 30),
            new User("uE", "원", "p5", Level.GOLD, 100, 100)
        );
    }

    @Test
    public void upgradeLevels() {
        userDao.deleteAll();
        for (User user : users)
            userDao.add(user);

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(userl.getId());
        assertThat(userUpdate.getLevel(), is(expectedLevel));
    }
}
```

테스트 코드는 위와 같다. List<User> 구문으로 테스트 픽스쳐를 셋팅한 후, userService를 호출하여 변경사항이 적용 되었는지 확인한다.

아직 **한 가지 더 남은 기능**이 있는데, 처음 가입하는 사용자는 기본적으로 BASIC 레벨이어야 한다는 부분이다.

# 5.1.4 UserService.add()

구현하기 전에 이 로직도 어디에 있으면 좋을 지 생각해 본다면 당연히 DAO는 아니다.

그렇다면 User가 직접 처리하도록 하면 나쁘지 않아 보이지만, 처음 가입하는 상황을 제외한다면

무의미한 정보이기 때문에 이 로직을 담기 위해 클래스에서 직접 초기화 하는 것은 문제가 될 수 있다. (왜 문제가 될 수 있는걸까? 왜 무의미한 정보이지?)

따라서 사용자 관리에 대한 비즈니스 로직을 담당하는 UserService에 넣는 것이 적절해보인다.

UserService에 담겨야 할 내용은 다음과 같다.

- UserService의 add() 메서드를 호출하면 레벨이 BASIC으로 설정되는 것

- add()를 호출할 때 입력으로 들어온 내용에 level 필드가 있다면 입력에 맞게 설정해준다.

```java
class UserServiceTest {

    @Test
    public void add() {
        userDao.deleteAll();

        // 레벨 입력값이 있는 경우
        User userWithLevel = users.get(4);

        // 레벨 입력값이 없는 경우
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWtihLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
    }
}

public class UserService {

    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    // 사용자 레벨 업그레이드 메서드
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            } else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            } else if (user.getLevel() == Level.GOLD) {
                changed = false;
            } else {
                changed = false;
            }
            if (changed) {
                userDao.update(user);
            }
        }
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
}
```

위 테스트와 비즈니스 로직은 성공적으로 만들어졌지만, 테스트 과정에서 DAO와 DB까지 모두 사용해야한다는 점이 아쉬운 점으로 남아있게 된다.

# 5.1.5 코드 개선

비즈니스 로직 구현을 모두 마쳤기 때문에 리팩터링을 수행한다. 이 때 어떤 내용을 점검하고 리팩터링 할 것인지는 다음과 같다.

- 코드에 중복된 부분은 없는가?

- 코드가 무엇을 하는 것인지 이해하기 불편하지 않은지?

- 코드가 자신이 있어야 할 자리에 있는지?

- 앞으로 변경이 일어난다면 어떤 것이 있을 수 있고, 그 변화에 쉽게 대응할 수 있게 작성되어 있는지?

UserService의 upgradeLevels() 메서드에는 이 점검사항에 해당하는 몇 가지 문제가 존재한다.

```java
public class UserService {

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {

            // 1번 If 블럭
            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            }
            else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            }
            else if (user.getLevel() == Level.GOLD) {
                changed = false;
            } else {
                changed = false;
            }
            if (changed) {
                userDao.update(user);
            }
        }
    }
}
```

이 메서드에서 여러 개로 등장하는 if 구문은 조건 블럭이 레벨 갯수만큼 반복된다.

따라서 새로운 레벨이 추가되면 Level enum을 수정해야할 뿐만 아니라 upgradeLevels()의 실제 비즈니스 로직에 새로운 로직을 추가해줘야한다.

만약 더 추가적인 요구사항이 들어온다면 메서드 크기는 커지고 앞으로 유지보수하기 힘든 코드가 되어 버린다.

## upgradeLevels() 리팩터링

