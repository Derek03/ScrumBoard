package project.scrumboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class EditPost extends AppCompatActivity {
    private static DBHelper colDB;
    private static DBHelper rowDB;
    private static DBHelper memberDB;
    private static DBHelper postDB;
    private Spinner column,
            row,
            priority,
            who;
    private static String[] priorityNames = {"High", "Medium", "Low"};
    private static ArrayList<String> columnNames;
    private static ArrayList<String> rowNames;
    private static ArrayList<String> memberNames;
    private String oldTitle;
    private String oldCol;
    private String oldRow;
    private String[] postValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        colDB = new DBHelper(this, "columns");
        rowDB = new DBHelper(this, "rows");
        memberDB = new DBHelper(this, "members");
        postDB = new DBHelper(this, "posts");

        Intent intent = getIntent();
        oldTitle = intent.getStringExtra("title");
        oldCol = intent.getStringExtra("col");
        oldRow = intent.getStringExtra("row");

        postValues = postDB.getValues(oldTitle, oldCol, oldRow);

        EditText title = (EditText) findViewById(R.id.postTitle);
        title.setText(postValues[0]);

        EditText description = (EditText) findViewById(R.id.postDescription);
        description.setText(postValues[1]);


        columnNames = colDB.select();
        rowNames = rowDB.select();
        memberNames = memberDB.select();

        //if this is the first time, give a blank entry
        if (memberNames.size() == 0) {
            memberNames.add(" ");
        }
        memberNames.add("Add new ( ಠ ಠ )");

        //setting up the create button to insert the values into the postDB
        Button updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //log to check when it goes in here
                Log.d("i have been pressed: ", "yeah..?");
                //text values to be plugged into the db obtained from the spinners and stuff
                EditText title = (EditText) findViewById(R.id.postTitle);
                EditText description = (EditText) findViewById(R.id.postDescription);
                String member = who.getSelectedItem().toString();
                String columnValue = column.getSelectedItem().toString();
                String rowValue = row.getSelectedItem().toString();
                String priorityVal = priority.getSelectedItem().toString();
                int priority = 1;
                //setting the priority int value based off of the string text
                if (priorityVal.equals("High")) {
                    priority = 3;
                } else if (priorityVal.equals("Medium")) {
                    priority = 2;
                }
                //insert all this stuff into the db
                postDB.updatePost(oldTitle, oldCol, oldRow,
                        title.getText().toString(),
                        columnValue,
                        rowValue,
                        description.getText().toString(),
                        member,
                        priority);
                Intent intent = new Intent(EditPost.this, Table.class);
                startActivity(intent);
            }
        });
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                postDB.deleteSinglePost(oldTitle,oldCol,oldRow);
                Intent intent = new Intent(EditPost.this, Table.class);
                startActivity(intent);
            }
        });

        column = (Spinner) findViewById(R.id.spinner1);
        row = (Spinner) findViewById(R.id.spinner2);
        priority = (Spinner) findViewById(R.id.spinner3);
        who = (Spinner) findViewById(R.id.spinner4);


        ArrayAdapter<String> colAdapter = new ArrayAdapter<String>(EditPost.this,
                android.R.layout.simple_spinner_item, columnNames);
        ArrayAdapter<String> rowAdapter = new ArrayAdapter<String>(EditPost.this,
                android.R.layout.simple_spinner_item, rowNames);
        ArrayAdapter<String> priorityAdapter = new ArrayAdapter<String>(EditPost.this,
                android.R.layout.simple_spinner_item, priorityNames);
        ArrayAdapter<String> memberAdapter = new ArrayAdapter<String>(EditPost.this,
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
        int holder = -1;
        holder = rowNames.indexOf(postValues[5]);
        if(holder!=-1){
            row.setSelection(holder);
        }
        holder = columnNames.indexOf(postValues[4]);
        if(holder!=-1){
            column.setSelection(holder);
        }
        holder = memberNames.indexOf(postValues[2]);
        if(holder!=-1){
            who.setSelection(holder);
        }
        //Log.d("WHAT BE YOU","'"+postValues[3]+"'");
        if(postValues[3].equals("3")){
            priority.setSelection(0);
        }
        else if(postValues[3].equals("2")){
            priority.setSelection(1);
        }
        else if(postValues[3].equals("1")){
            priority.setSelection(2);
        }

    }

    private void addMember(View view) {
        //edit box for alert dialog
        final EditText textboxString = new EditText(this);

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Enter the name of your new member:")
                .setView(textboxString)
                .setTitle("Add Member")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = textboxString.getText().toString();
                        //basically if this is the first value being added, remove the blank
                        if (memberNames.contains(" ")) {
                            memberNames.remove(" ");
                        }
                        //add the value to the array and save to db
                        memberNames.add(0, value);
                        memberDB.insert(value);
                        //set spinner to select the new value
                        who.setSelection(0);
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