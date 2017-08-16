<h1>How to use alpsqlite - library</h1>

<h3>In your build.gradle file you must add</h3>

```gradle
maven { url "https://jitpack.io"}
```

<h3>In your app's build.gradle file you must add</h3>

```gradle
compile 'com.github.pethoalpar:alpsqlite:v1'
```

<h3>Declare entity</h3>

```java
@Table(name = "PERSON")
public class Person {

    @Id
    @Column(columnName = "id", type = Types.INTEGER)
    protected Integer id;

    @Column(columnName = "name", type = Types.TEXT)
    protected String name;

    @Column(columnName = "age", type = Types.INTEGER)
    protected int age;
}
```

<h3>Insert entity to database</h3>

```java
SQLBusinessDelegate<Person> dbDelegate = new SQLBusinessDelegate<Person>(context,Person.class,"database name", null,1);
int id = dbDelegate.save(person);
```

<h3>Update entity</h3>

```java
dbDelegate.update(person);
```

<h3>Find entity by id</h3>

```java
dbDelegate.findById((int) id);
```
