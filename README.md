# spring-test

mainブランチは単体テスト用、deployブランチは統合テスト用。

EmployeeControllerTestsで統合テスト時は以下コマンドでmysqlを起動する。
```shell
# 起動
docker-compose up -d
# コマンド実行 抜けるにはexit
docker-compose exec db bash
# 終了
docker-compose down
```

EmployeeControllerITで統合テストをする時は事前にDockerを起動しておけば自動でコンテナを作成してくれる。

## テストメソッドテンプレート

```java
    @DisplayName("")
    @Test
    public void given_when_then(){
        // given - precondition or setup

        // when - action or the behaviour that we are going test

        // then - verify the output

    }
```
これをテンプレートとして使用する。

## Hamcrest vs AssertJ

HamcrestはJUnit4から標準採用のAssertion。  
AssertJは直感的に使えるAssertion。

```java
// Hamcrest
assertThat("str", is("str"));
assertThat(list, is(not(empty())));
assertThat(list, is(contains("1", "2", "3")));

// AssertJ
assertThat("str").isEqualTo("str");
assertThat(list).isNotEmpty()
                .contains("1", "2", "3");
```

## @DataJpaTest

テストを行うための設定を行ってくれるアノテーション。  
(デフォルトでは)メソッドごとにトランザクションがロールバックする。  
(デフォルトでは)@Entity,@Repositoryのみをスキャンする。
(デフォルトでは)データソースをインメモリのものに置換する。  
`@AutoConfigureTestDatabase(replace=Replace.NONE)`とすることで通常のデータソースを使用できる。

## @Mock, @InjectMock, @MockBean

Mockitoの`mock()`を使用することでモックオブジェクトを作成することができる。  
また、`@Mock`を使用することで何度も`mock()`を宣言することを回避できる。  
`@ExtendWith(MockitoExtension.class)`を宣言したクラスのプロパティに`@Mock`と`@InjectMocks`を記載することで、  
`@InjectMocks`のオブジェクトのプロパティに`@Mock`指定したオブジェクトを挿入したモックオブジェクトを作成できる。  
Injectされるプロパティは、Constructor,Setter,Field Injectionが可能な場合Injectされる。

`@MockBean`はApplicationContextにモックオブジェクトを登録する。  
コントローラーのテストなどApplicationContextが必要な場合は使用する。  
ただし、@MockBeanがあるクラスごとにApplicationContextは再生成される(遅い)。

## JsonPath

JSON文字列とJavaオブジェクトを双方向に変換できるライブラリ。

```java
$.firstName; // -> {firstName: "ここを取得"}
$.users[0:2]; // -> {users: [0: "ここから", 1: "ここまで取得"]}
```

## @WebMvcTest

すべてのApplicationContextを読まず、コントローラーに必要な依存関係だけをロードするアノテーション。  

## MockMvc

Spring MVCの動作をコードで再現するためのクラス。  
ユーザが使っているようにテストができる。

## @SpringBootTest

すべてのApplicationContextを読むアノテーション。
統合テスト用でMockは行わない。

```java
// ApplicationContextはロードするが、Web環境等は提供しない
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
// デフォルト。サーバーを起動させず模擬Web環境が提供される
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
// サーバーをランダムなポートで起動させる
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// 実際のWeb環境が提供される。
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
```

## @AutoConfigureMockMvc

`@SpringBootTest`において、`@Autowired`で`MockMvc`をインジェクトするためのアノテーション。

## Testcontainers

https://www.testcontainers.org/  
軽量のテスト用の環境を提供するJavaライブラリ。  
データベースやSelenium Webブラウザーなどを提供する。  
Dockerコンテナを使用できる。  

```java
// staticはクラス内で共有
@Container
private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer();

// テストメソッドごとに作成
@Container
private PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer()
        .withDatabaseName("foo")
        .withUsername("foo")
        .withPassword("secret");    
```
