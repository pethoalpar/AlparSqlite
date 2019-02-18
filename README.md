# How to use alpsqlite - library

### In your build.gradle file you must add

```gradle
maven { url "https://jitpack.io"}
```

### In your app's build.gradle file you must add 

```gradle
compile 'com.github.pethoalpar:AlparSqlite:v1.0'
```

### Declare entity

```java
@Table(name = "PERSON")
public class Person {

    @Id
    @Column(columnName = "id")
    protected Integer id;

    @Column(columnName = "name")
    protected String name;

    @Column(columnName = "age")
    protected int age;
}
```

### Init database table

```java
DatabaseService<Person> dbDelegate = new DatabaseService<>(Person.class, this);
```

### Insert entity to database

```java
Long id = dbDelegate.insert(person);
```

### Update entity

```java
dbDelegate.update(person);
```

### Find entity by id

```java
dbDelegate.findById((int) id);
```
