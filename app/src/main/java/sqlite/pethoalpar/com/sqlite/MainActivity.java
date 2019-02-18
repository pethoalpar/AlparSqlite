package sqlite.pethoalpar.com.sqlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import sqlite.pethoalpar.com.alqlite.DatabaseService;
import sqlite.pethoalpar.com.alqlite.exception.NoResultException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = this.findViewById(R.id.textView);

        Person person = new Person();
        person.setName("Alpar");
        person.setAge(42);
        person.setUserName("pethoalpar");

        DatabaseService<Person> dbDelegate = new DatabaseService<>(Person.class, this);
        try {
            Long id = dbDelegate.insert(person);
            Person user = dbDelegate.findById(id.intValue());
            tv.setText(buildUserString(user));
            user.setAge(45);
            dbDelegate.update(user);
            tv.setText(tv.getText() + buildUserString(user));
            List<Person> list = dbDelegate.findAll();
            tv.setText(tv.getText() + String.valueOf(list.size()));
        } catch (NoResultException e) {
            e.printStackTrace();
        }
    }

    private String buildUserString ( Person person ) {
        StringBuilder sb = new StringBuilder("User: ");
        sb.append("name:").append(person.getName());
        sb.append(" user name:").append(person.getUserName());
        sb.append(" age:").append(person.getAge());
        sb.append(" id:").append(person.getId());
        sb.append("\n");
        return sb.toString();
    }
}
