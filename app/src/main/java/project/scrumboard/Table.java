package project.scrumboard;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Table extends AppCompatActivity {

    private static DBHelper colDB;
    private static DBHelper rowDB;
    private static DBHelper postDB;
    private static ArrayList<String> columnNames;
    private static ArrayList<String> rowNames;
    private static ArrayList<String> postNames;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);


        colDB = new DBHelper(this, "columns");
        rowDB = new DBHelper(this, "rows");
        postDB = new DBHelper(this, "posts");

        columnNames = colDB.select();
        rowNames = rowDB.select();
        postNames = postDB.select();

        //Create Table
        TableLayout table = (TableLayout)findViewById(R.id.table);
        table.setStretchAllColumns(true);
        table.setShrinkAllColumns(false);

        //Create Rows based on Vals
        for(int i = 0; i<= rowNames.size(); i++){
                //Create First Row
                TableRow rowTitle = new TableRow(this);
                //rowTitle.setGravity(Gravity.CENTER_HORIZONTAL);
            //if in the first row,first column, display nothing
            if(i == 0) {
                TextView title = new TextView(this);
                title.setText("");
                title.setWidth(30);

                title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(Typeface.SERIF, Typeface.BOLD);

                //MAKE IT WIDER THAN ONE COLUMN
                //TableRow.LayoutParams params = new TableRow.LayoutParams();
                //params.span = 6;

                rowTitle.addView(title);
            //else display the names of the rows
            }else {
                TextView title = new TextView(this);
                title.setText(rowNames.get(i-1));
                title.setWidth(30);

                title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                title.setTypeface(Typeface.SERIF, Typeface.BOLD);

                rowTitle.addView(title);
            }


            //next, if in the first row, display the column names
            if(i == 0){
                for( int j = 0; j < columnNames.size(); j++){
                        TextView colLabel = new TextView(this);
                        colLabel.setWidth(100);
                        colLabel.setText(columnNames.get(j));
                        colLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
                        rowTitle.addView (colLabel);

                }
            //else display the post-its
            }else{
                for( int j = 0; j < columnNames.size(); j++) {

                   /* TextView colLabel = new TextView(this);
                    colLabel.setWidth(500);
                    colLabel.setHeight(500);
                    colLabel.setBackgroundColor(Color.parseColor("#1111A5"));
                    colLabel.setText(" h");
                    colLabel.setTypeface(Typeface.SERIF, Typeface.BOLD);
                     rowTitle.addView (colLabel);
                    */

                    /*add test to img here*/
                    Bitmap bm = null;
                    Drawable d = null;
                    String title = postDB.selectPostTitle(columnNames.get(j), rowNames.get(i-1));

                    if(!title.equals("FAILED")){
                    try {

                        bm = BitmapFactory.decodeResource(getResources(), R.drawable.postit);
                        Bitmap.Config bitmapConfig = bm.getConfig();
                        if (bitmapConfig == null) {
                            bitmapConfig = Bitmap.Config.ARGB_8888;
                        }
                        Bitmap bmn = bm.copy(bitmapConfig, true);

                        Canvas canvas = new Canvas(bmn);

                        // new antialised Paint
                        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        // text color - #3D3D3D
                        paint.setColor(Color.BLACK);
                        // text size in pixels
                        paint.setTextSize(50);
                        canvas.drawBitmap(bmn, 0, 0, paint);
                        canvas.drawText(title, 20, 100, paint);

                        d = new BitmapDrawable(getResources(), bmn);

                        ImageButton but = new ImageButton(this);
                        but.setBackground(d);
                        but.setMaxWidth(100);
                        but.setMaxHeight(100);
                        but.setTag(String.valueOf(i) + "," + String.valueOf(j));
                        setButtonListener(but);
                        rowTitle.addView(but);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                    ImageButton but = new ImageButton(this);
                    but.setBackground(null);
                    but.setMaxWidth(100);
                    but.setMaxHeight(100);
                    rowTitle.addView(but);
                }
            }
            table.addView(rowTitle);
        }
    }

    private void setButtonListener(ImageButton but){
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = v.getTag().toString();
                String values[] = tag.split(",");
                int i = Integer.parseInt(values[0]);
                int j = Integer.parseInt(values[1]);
                String title = postDB.selectPostTitle(columnNames.get(j), rowNames.get(i - 1));
                Intent intent = new Intent(Table.this, EditPost.class);
                intent.putExtra("title", title);
                intent.putExtra("col", columnNames.get(j));
                intent.putExtra("row", rowNames.get(i-1));

                startActivity(intent);
            }
        });
    }
}
