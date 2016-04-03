package project.scrumboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class NewBoard extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView lst1;
    private ListView lst2;
    private ArrayList<String> columnValues = new ArrayList<>();
    private ArrayList<String> rowValues = new ArrayList<>();
    private ArrayAdapter<String> rowAdapter;
    private ArrayAdapter<String> colAdapter;
    private DBHelper colDB = new DBHelper(this, "columns");
    private DBHelper rowDB = new DBHelper(this, "rows");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_board);

        //Column Listview
        lst1 = (ListView) findViewById(R.id.listCol);
        columnValues = colDB.select();
        colAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, columnValues);
        lst1.setAdapter(colAdapter);
        lst1.setOnItemClickListener(this);


        //Row Listview
        lst2 = (ListView) findViewById(R.id.listRow);
        rowValues = rowDB.select();
        rowAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rowValues);
        lst2.setAdapter(rowAdapter);
        lst2.setOnItemClickListener(this);




    }




    //clicking list item
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //do stuff here when clicked
    }





    //clicking the add column button
    public void newCol(View view){

        //edit box for alert dialog
        final EditText textboxString = new EditText(this);



        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Enter the name of your new column:")
                .setView(textboxString)
                .setTitle("Add column")
                .setPositiveButton("Add",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = textboxString.getText().toString();
                        columnValues.add(value);
                        colDB.insert(value);
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






    //clicking the add column button
    public void newRow(View view) {
        final EditText textboxString = new EditText(this);

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Enter the name of your new Row:")
                .setView(textboxString)
                .setTitle("Add column")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = textboxString.getText().toString();
                        rowValues.add(value);
                        rowDB.insert(value);
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

    public void goToPost(View view){

        //button to post it
        Intent intent = new Intent(NewBoard.this, PostIt.class);
        startActivity(intent);
    }
}
