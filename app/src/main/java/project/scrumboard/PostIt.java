package project.scrumboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class PostIt extends AppCompatActivity {
    private static DBHelper colDB;
    private static DBHelper rowDB;
    private Spinner column,
                    row,
                    priority,
                    who;
    private static final String[]paths = {"state", "category", "requirements", "priority"};
    private static ArrayList<String> columnNames;
    private static ArrayList<String> rowNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_it);

        colDB = new DBHelper(this, "columns");
        rowDB = new DBHelper(this, "rows");

        columnNames    = colDB.select();
        rowNames       = rowDB.select();
        Log.d("colDB number: ", ""+colDB.number);
        Log.d("rowDB number: ", ""+rowDB.number);
        Log.d("colDB length: ", ""+columnNames.size());
        Log.d("rowDB length: ", ""+rowNames.size());
        for(String value : columnNames){
            Log.d("*******", value);
        }
        for(String value : rowNames){
            Log.d("*******2", value);
        }
        column = (Spinner)findViewById(R.id.spinner1);
        row = (Spinner)findViewById(R.id.spinner2);
        priority = (Spinner)findViewById(R.id.spinner3);
        who = (Spinner)findViewById(R.id.spinner4);
        ArrayAdapter<String>colAdapter = new ArrayAdapter<String>(PostIt.this,
                android.R.layout.simple_spinner_item,columnNames);

        ArrayAdapter<String>rowAdapter = new ArrayAdapter<String>(PostIt.this,
                android.R.layout.simple_spinner_item,rowNames);

        colAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        column.setAdapter(colAdapter);
        row.setAdapter(rowAdapter);

        priority.setAdapter(colAdapter);
        who.setAdapter(colAdapter);
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
