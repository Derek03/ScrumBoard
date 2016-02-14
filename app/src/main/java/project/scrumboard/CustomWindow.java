package project.scrumboard;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Spencer on 2/13/2016.
 */
public class CustomWindow extends Activity {
    protected TextView title;
    protected ImageView icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        //setContentView(R.layout.activity_main);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

        title = (TextView) findViewById(R.id.title);
        icon  = (ImageView) findViewById(R.id.icon);
    }
}
