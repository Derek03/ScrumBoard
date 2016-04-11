package project.scrumboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Spencer on 2/13/2016.
 */
public class BoardMainActivity extends Activity {

    private GridView gv;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private int j = 0;
    private GestureDetector detector;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ImageView expandedImageView;

    private Integer[] mThumbIds = {
            R.drawable.postit,R.drawable.postit
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_activity_main);

        detector = new GestureDetector(this, new SwipeGestureDetector());

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int pos, long id) {
                j = pos;
                zoomImageFromThumb(v, mThumbIds[pos]);
            }
        });

         mShortAnimationDuration = getResources().getInteger(android.R.integer.config_mediumAnimTime);
    }
    class ImageAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater layoutInflater;

        public ImageAdapter(BoardMainActivity activity){
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return mThumbIds.length;
        }
        public Object getItem(int position) {
            return position;
        }
        public long getItemId(int position) {
            return position;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {

            View listItem = convertView;

            if (listItem == null) {
                listItem = layoutInflater.inflate(R.layout.single_grid_item, null);
            }
            Log.d("uhmm",""+position);
            new DrawTitle().execute(mThumbIds[position]);

            //iv.setBackgroundResource(mThumbIds[p]);

            return listItem;
        }

    }
    private void zoomImageFromThumb(final View thumbView, int imageResId) {

        if(mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        expandedImageView = (ImageView) findViewById(R.id.expanded_image);

        expandedImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(detector.onTouchEvent(event)){
                    return true;
                } else {
                    return false;
                }
            }
        });

        expandedImageView.setImageResource(imageResId);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container).getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;

        if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds
                .width() / startBounds.height()) {

            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight = startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f));

        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));

                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    thumbView.setAlpha(1f);
                    expandedImageView.setVisibility(View.GONE);
                    mCurrentAnimator = null;
                }
                @Override
                public void onAnimationCancel(Animator animation) {
                    thumbView.setAlpha(1f);
                    expandedImageView.setVisibility(View.GONE);
                    mCurrentAnimator = null;
                }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }

    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1,
                               MotionEvent e2,
                               float velocityX,
                               float velocityY) {

            try {
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY){
                    if(mThumbIds.length > j) {
                        j++;
                        if(j < mThumbIds.length) {
                            expandedImageView.setImageResource(mThumbIds[j]);
                            return true;
                        } else {
                            j = 0;
                            expandedImageView.setImageResource(mThumbIds[j]);
                            return true;
                        }
                    }
                } else if(e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    if(j > 0) {
                        j--;
                        expandedImageView.setImageResource(mThumbIds[j]);
                        return true;
                    } else {
                        j = mThumbIds.length - 1;
                        expandedImageView.setImageResource(mThumbIds[j]);
                        return true;
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    class DrawTitle
            extends AsyncTask<Integer, Void, String> {
        protected String doInBackground(final Integer... img) {

            try {

                Bitmap bm = BitmapFactory.decodeResource(getResources(), img[0]);
                Bitmap.Config bitmapConfig = bm.getConfig();
                if(bitmapConfig == null) {
                    bitmapConfig = Bitmap.Config.ARGB_8888;
                }
                Bitmap bmn = bm.copy(bitmapConfig, true);

                Canvas canvas = new Canvas(bmn);

                // new antialised Paint
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                // text color - #3D3D3D
                paint.setColor(Color.BLACK);
                // text size in pixels
                paint.setTextSize(500);
                canvas.drawBitmap(bmn, 0, 0, paint);
                canvas.drawText("title", 300, 600, paint);

                final Drawable d = new BitmapDrawable(getResources(), bmn);

                runOnUiThread(new Runnable() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void run() {
                        //update ui here
                        ImageView iv = (ImageView) findViewById(R.id.thumb);
                        iv.setBackground(d);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return(null);
        }

        protected void onPostExecute(final String data) {}
    }
}

