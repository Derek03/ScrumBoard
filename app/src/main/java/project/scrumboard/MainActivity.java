package project.scrumboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button viewAllProject;
    private Button newPButton;
    DBHelper colDB;
    DBHelper rowDB;
    DBHelper memberDB;
    DBHelper postDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.deleteDatabase("Data.db");

        //creates and sets up the dbs
        colDB = new DBHelper(this, "columns");
        rowDB = new DBHelper(this, "rows");
        memberDB = new DBHelper(this, "members");
        postDB = new DBHelper(this, "posts");
        colDB.setup();
        rowDB.setup();
        memberDB.setup();
        postDB.setup();

        //Arraylist for values
        ArrayList<String> test = postDB.select();

        //this is for showing that things are getting saved to the post db
        for(String values : test ){
            Log.d("values from posts: ", values);
        }


        viewAllProject = (Button) findViewById(R.id.previous);
        viewAllProject.setOnClickListener(Listener);
        newPButton = (Button) findViewById(R.id.add);
        newPButton.setOnClickListener(Listener);
    }

    View.OnClickListener Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch(view.getTag().toString()) {
                case "Previous":
                    intent = new Intent(MainActivity.this, Table.class);
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
        }
    };
}
