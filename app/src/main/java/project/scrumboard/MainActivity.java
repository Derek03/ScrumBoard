package project.scrumboard;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button viewAllPoject;
    private Button newPButton;
    DBHelper mydb;
    DBHelper mydb2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new DBHelper(this, "columns");
        mydb2 = new DBHelper(this, "rows");
//        mydb.setup(); //this needs to be run once to set up db
//        mydb2.setup(); //this needs to be run once to set up db, then can be commented, deletes/creates
//        mydb.delete()
//        mydb2.delete();
        //mydb.insert("this is a test");

        //String test[] = mydb.select();
        ArrayList<String> test = mydb.select();
        for(String value : test){
            Log.d("********", value);
        }

        ArrayList<String> test2 = mydb2.select();

        for(String value : test2){
            Log.d("********2", value);
        }

        viewAllPoject = (Button) findViewById(R.id.previous);
        viewAllPoject.setOnClickListener(Listener);
        newPButton = (Button) findViewById(R.id.add);
        newPButton.setOnClickListener(Listener);
    }

    View.OnClickListener Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch(view.getTag().toString()) {
                case "Previous":
                    intent = new Intent(MainActivity.this, BoardMainActivity.class);
                    break;
                case "Other":
                    //intent = new Intent(MainActivity.this, PostIt.class);
                    break;
                case "Add":
                    intent = new Intent(MainActivity.this, NewBoard.class);
                default:
                    break;
            }

            startActivity(intent);

            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //       .setAction("Action", null).show();
        }
    };
}
