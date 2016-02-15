package project.scrumboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button viewAllPoject;
    private Button newPButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewAllPoject = (Button) findViewById(R.id.button);
        viewAllPoject.setOnClickListener(Listener);
        newPButton = (Button) findViewById(R.id.button2);
        newPButton.setOnClickListener(Listener);
    }

    View.OnClickListener Listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch(view.getTag().toString()) {
                case "One":
                    intent = new Intent(MainActivity.this, BoardMainActivity.class);
                    break;
                case "Two":
                    intent = new Intent(MainActivity.this, PostIt.class);
                    break;
                default:
                    break;
            }

            startActivity(intent);

            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //       .setAction("Action", null).show();
        }
    };
}
