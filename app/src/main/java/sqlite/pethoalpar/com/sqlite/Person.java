package sqlite.pethoalpar.com.sqlite;


import sqlite.pethoalpar.com.alqlite.Types;
import sqlite.pethoalpar.com.alqlite.annotation.Column;
import sqlite.pethoalpar.com.alqlite.annotation.Id;
import sqlite.pethoalpar.com.alqlite.annotation.Table;

/**
 * Created by pethoalpar on 16/08/2017.
 */

@Table(name = "PERSON")
public class Person {

    @Id
    @Column(columnName = "id", type = Types.INTEGER)
    protected Integer id;

    @Column(columnName = "name", type = Types.TEXT)
    protected String name;

    @Column(columnName = "age", type = Types.INTEGER)
    protected int age;

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