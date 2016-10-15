package cn.edu.cdut.lm.mymuiscplayer.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.HashMap;

import cn.edu.cdut.lm.mymuiscplayer.R;

/**
 * Created by LimiaoMaster on 2016/10/14 19:17
 */

public class QuickScrollBar extends ImageButton {

    private String TAG = "QuickScrollBar";
    private String[] strings = new String[] { "*", "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z" };
    private Paint paint = new Paint();
    private RecyclerView mRecyclerView;
    private HashMap<String,Integer> mHashMap;
    private float singleHeight;
    private TextView tv_alpha;
    private LinearLayoutManager mlinearLayoutManager;

    private Handler handler = new Handler() ;
    private float xPosition;

    public QuickScrollBar(Context context) {
        super(context);
    }

    public QuickScrollBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QuickScrollBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initBar(LinearLayoutManager linearLayoutManager, View view){
        //mRecyclerView = recyclerView;
        mlinearLayoutManager = linearLayoutManager;
        tv_alpha = (TextView) view.findViewById(R.id.tv_alpha);
        tv_alpha.setVisibility(INVISIBLE);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1){
                    QuickScrollBar.this.setVisibility(View.INVISIBLE);
                }
            }
        };
    }

    public void getHashMap(HashMap<String , Integer> hashMap){
        mHashMap = hashMap;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int barWidth = getWidth();
        int barHeight = getHeight();
        singleHeight = barHeight/strings.length;
        singleHeight = (barHeight - singleHeight)/strings.length;
        float xPosition;
        float yPosition;
        for (int i = 0 ; i< strings.length ; i++){
            paint.setColor(Color.WHITE);
            paint.setTextSize(40);

            xPosition =  (barWidth - paint.measureText(strings[i]))/2;
            if (i>=1){
                yPosition = singleHeight *(i+1)-5;
            }else
                yPosition = singleHeight *(i+1)+5;

            canvas.drawText(strings[i],xPosition,yPosition,paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y = event.getY();
        int touchLetterIndex = (int) (y/singleHeight);
        if(touchLetterIndex >= 0 && touchLetterIndex < strings.length){
            String touchLetter = strings[touchLetterIndex];
            if (mHashMap.containsKey(touchLetter)){
                int scrollPosition = mHashMap.get(touchLetter);
                mlinearLayoutManager.scrollToPositionWithOffset(scrollPosition + 1,0);//加一是因为首行有个一播放全部，所以整体往后错一行，加一。
            }
            tv_alpha.setText(touchLetter);
        }

        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG,"触摸类型为ACTION_DOWN..........");
                handler.removeMessages(1);
                tv_alpha.setVisibility(VISIBLE);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG,"触摸类型为ACTION_MOVE..........");
                tv_alpha.setVisibility(VISIBLE);
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG,"触摸类型为ACTION_UP..........");
                tv_alpha.setVisibility(INVISIBLE);
                handler.sendEmptyMessageDelayed(1,1000);
                break;
        }


        return super.onTouchEvent(event);

    }
}
