# Chapter 2. JPA 시작

## 객체 매핑 시작

```sql
create table member {
    ID varchar(255) NOT NULL,
    NAME varchar(255),
    AGE integer,
    PRIMARY KEY (ID)
}
```

```java
@Setter
@Getter
@Entity
@Table(name = "MEMBER")
public class Member {
    @Id
    @Column(name = "ID")
    private String id;
    @Column(name = "NAME")
    private String username;

    private Integer age;

}
```

## 애플리케이션 개발

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://xmlns.jcp.org/xml/ns/persistence" version="2.1">

    <persistence-unit name="jpabook">

        <properties>

            <!-- 필수 속성 -->
            <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="javax.persistence.jdbc.user" value="sa"/>
            <property name="javax.persistence.jdbc.password" value=""/>
            <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/test"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect" />

            <!-- 옵션 -->
            <property name="hibernate.show_sql" value="true" />
            <property name="hibernate.format_sql" value="true" />
            <property name="hibernate.use_sql_comments" value="true" />
            <property name="hibernate.id.new_generator_mappings" value="true" />

            <!--<property name="hibernate.hbm2ddl.auto" value="create" />-->
        </properties>
    </persistence-unit>

</persistence>
```

```java
import com.book.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaApplication {

    public static void main(String[] args) {

        // [엔티티 매니저 팩토리] - 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpabook");
        // [엔티티 매니저] - 생성
        EntityManager em = emf.createEntityManager();
        // [트랜잭션] - 획득
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin(); // [트랜잭션] - 시작
            logic(em); // 비즈니스 로직 실행
            tx.commit();// [트랜잭션] - 커밋
        } catch (Exception e) {
            tx.rollback(); // [트랜잭션] - 롤백
        } finally {
            em.close(); // [엔티티 매니저] - 종료
        }
        emf.close(); // [엔티티 매니저 팩토리] - 종료
    }

    // [비즈니스 로직]
    private static void logic(EntityManager em) {
        String id = "id1";
        Member member = new Member();
        member.setId(id);
        member.setUsername("지한");
        member.setAge(2);

        // 등록
        em.persist(member);

        // 수정
        member.setAge(20);

        // 한 건 조회
        Member findMember = em.find(Member.class, id);
        System.out.println("findMember=" + findMember.getUsername() + ", age =" + findMember.getAge());

        //목록 조회
        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        System.out.println("members.size=" + members.size());

        //삭제
        em.remove(member);
    }

}

```

코드 구성
- 엔티티 매니저 설정
- 트랜잭션 관리
- 비지니스 로직

![엔티티 매니저 생성과정](https://user-images.githubusercontent.com/43127088/109125852-e91db900-778f-11eb-80be-15fcd1b0f195.PNG)

### 엔티티 매니저 팩토리 생성

- persistence.xml의 설정 정보를 읽어서 엔티티 매니저 팩토리를 생성
  - persistence.xml은 META-INF/persistence.xml에 위치해야 한다.
- 비용이 많이 드는 작업이므로 애플리케이션 전체에서 딱 한 번만 생성하고 공유해서 사용해야 한다.

### 엔티티 매니저 생성

- 엔티티 매니저 팩토리에서 엔티티 매니저를 생성
- 엔티티 매니저를 사용해서 엔티티를 데이터베이스에 **등록/수정/삭제/조회**
- 엔티티 매니저는 데이터베이스 커넥션과 밀접한 관계가 있으므로 **스레드간에 공유하거나 재사용하면 안된다.**

### 트랜잭션 관리

JPA를 사용하면 항상 트랜잭션 안에서 데이터를 변경

트랜잭션 없이 데이터를 변경하면 예외 발생

트랜잭션을 시작하려면 엔티티 매니저에서 트랜잭션 API를 받아야 한다.

### 비즈니스 로직

비즈니스 로직에서 수정부분을 보면 em.update() 같은 메소드를 호출하는 것이 아닌, 단순히 엔티티의 값만 변경한다.
JPA는 어떤 엔티티가 변경되었는지 추적하는 기능을 갖추고 있어서 트랜잭션을 커밋할 때 변경된 엔티티를 찾아서 update 쿼리를 생성해서 데이터베이스에 반영한다.

### JPQL

위 비즈니스 로직 코드에서 목록 조회 부분을 보면 JPQL을 사용해서 조회하는 것을 볼 수 있다.

```java
TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
List<Member> resultList = query.getResultList();
```

JPA는 엔티티 객체를 중심으로 개발하므로 검색을 할 때도 테이블이 아닌 엔티티 객체를 대상으로 검색해야 한다.
이를 위해 JPA는 JPQL이라는 쿼리 언어를 제공한다.

- JPQL : 엔티티 객체를 대상으로 하는 객체 지향 쿼리
- SQL : 데이터베이스 테이블을 대상으로 하는 쿼리