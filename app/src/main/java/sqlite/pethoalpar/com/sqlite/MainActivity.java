package sqlite.pethoalpar.com.sqlite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import sqlite.pethoalpar.com.alqlite.SQLBusinessDelegate;
import sqlite.pethoalpar.com.alqlite.exception.NoResultException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Person person = new Person();
        person.setName("Alpar");
        person.setAge(42);

        SQLBusinessDelegate<Person> dbDelegate = new SQLBusinessDelegate<Person>(this,Person.class,"database name", null,1);
        try {
            Long id = dbDelegate.save(person);
            List<Person> list = dbDelegate.findAll();
            //((TextView)this.findViewById(R.id.textView)).setText(list.size());
        } catch (NoResultException e) {
            e.printStackTrace();
        }
    }
}
