package project.scrumboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class PostIt extends AppCompatActivity {
    private static DBHelper colDB;
    private static DBHelper rowDB;
    private static DBHelper memberDB;
    private static DBHelper postDB;
    private Spinner column,
                    row,
                    priority,
                    who;
    private static final String[]paths = {"state", "category", "requirements", "priority"};
    private static String[] priorityNames = {"High", "Medium", "Low"};
    private static ArrayList<String> columnNames;
    private static ArrayList<String> rowNames;
    private static ArrayList<String> memberNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_it);

        colDB = new DBHelper(this, "columns");
        rowDB = new DBHelper(this, "rows");
        memberDB = new DBHelper(this, "members");

        columnNames    = colDB.select();
        rowNames       = rowDB.select();
        memberNames    = memberDB.select();

        memberNames.add("Add new ^_^");


        column = (Spinner)findViewById(R.id.spinner1);
        row = (Spinner)findViewById(R.id.spinner2);
        priority = (Spinner)findViewById(R.id.spinner3);
        who = (Spinner)findViewById(R.id.spinner4);


        ArrayAdapter<String>colAdapter = new ArrayAdapter<String>(PostIt.this,
                android.R.layout.simple_spinner_item,columnNames);
        ArrayAdapter<String>rowAdapter = new ArrayAdapter<String>(PostIt.this,
                android.R.layout.simple_spinner_item,rowNames);
        ArrayAdapter<String>priorityAdapter = new ArrayAdapter<String>(PostIt.this,
                android.R.layout.simple_spinner_item,priorityNames);
        ArrayAdapter<String>memberAdapter = new ArrayAdapter<String>(PostIt.this,
                android.R.layout.simple_spinner_item,memberNames);


        colAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        column.setAdapter(colAdapter);
        row.setAdapter(rowAdapter);
        priority.setAdapter(priorityAdapter);
        who.setAdapter(memberAdapter);

    }

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        Spinner spinner = (Spinner)findViewById(R.id.spinner4);
        int pos = spinner.getAdapter().getCount();
        Log.d("pos :", ""+pos);
        Log.d("position :", ""+position);
        if(pos == position){
            Log.d("happy times", "yay");
        }
    }

}
