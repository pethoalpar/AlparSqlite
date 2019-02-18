package sqlite.pethoalpar.com.sqlite;


import sqlite.pethoalpar.com.alqlite.annotation.Column;
import sqlite.pethoalpar.com.alqlite.annotation.Id;
import sqlite.pethoalpar.com.alqlite.annotation.Table;

/**
 * Created by pethoalpar on 16/08/2017.
 */

@Table(name = "PERSON")
public class Person {

    @Id
    @Column(columnName = "id")
    protected Integer id;

    @Column(columnName = "name")
    protected String name;

    @Column(columnName = "age")
    protected int age;

    @Column(columnName = "userName")
    private String userName;

    public String getUserName () {
        return userName;
    }

    public void setUserName ( String userName ) {
        this.userName = userName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}