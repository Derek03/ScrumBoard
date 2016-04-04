package project.scrumboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
    private static final String[] paths = {"state", "category", "requirements", "priority"};
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

        columnNames = colDB.select();
        rowNames = rowDB.select();
        memberNames = memberDB.select();

        memberNames.add(" ");
        memberNames.add("Add new ^_^");


        column = (Spinner) findViewById(R.id.spinner1);
        row = (Spinner) findViewById(R.id.spinner2);
        priority = (Spinner) findViewById(R.id.spinner3);
        who = (Spinner) findViewById(R.id.spinner4);


        ArrayAdapter<String> colAdapter = new ArrayAdapter<String>(PostIt.this,
                android.R.layout.simple_spinner_item, columnNames);
        ArrayAdapter<String> rowAdapter = new ArrayAdapter<String>(PostIt.this,
                android.R.layout.simple_spinner_item, rowNames);
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(PostIt.this,
                android.R.layout.simple_spinner_item, priorityNames);
        ArrayAdapter<String> memberAdapter = new ArrayAdapter<String>(PostIt.this,
                android.R.layout.simple_spinner_item, memberNames);


        colAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        memberAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        column.setAdapter(colAdapter);
        row.setAdapter(rowAdapter);
        priority.setAdapter(priorityAdapter);
        who.setAdapter(memberAdapter);

        who.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int pos, long arg3) {

                Spinner spinner = (Spinner) findViewById(R.id.spinner4);
                int position = spinner.getAdapter().getCount();

                //if its the Add new button!
                if (pos == position - 1) {
                    addMember(arg0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //
            }
        });
    }

    private void addMember(View view){
        //edit box for alert dialog
        final EditText textboxString = new EditText(this);

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Enter the name of your new member:")
                .setView(textboxString)
                .setTitle("Add Member")
                .setPositiveButton("Add",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = textboxString.getText().toString();
                        memberNames.add(1,value);
                        //colDB.insert(value);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
