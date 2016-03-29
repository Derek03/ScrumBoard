package project.scrumboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class PostIt extends AppCompatActivity {

    private Spinner column,
                    row,
                    priority,
                    who;
    private static final String[]paths = {"state", "category", "requirements", "priority"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_it);

        column = (Spinner)findViewById(R.id.spinner1);
        row = (Spinner)findViewById(R.id.spinner2);
        priority = (Spinner)findViewById(R.id.spinner3);
        who = (Spinner)findViewById(R.id.spinner4);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(PostIt.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        column.setAdapter(adapter);
        row.setAdapter(adapter);
        priority.setAdapter(adapter);
        who.setAdapter(adapter);
    }

    /*public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }*/

}
