package gr.greekbusses.misc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSwipeListener implements View.OnTouchListener
{
    private final GestureDetector gestureDetector;

    public OnSwipeListener (Context context)
    {
        gestureDetector = new GestureDetector(context, new GestureListener());
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        return gestureDetector.onTouchEvent(motionEvent);
    }
    private final class GestureListener extends GestureDetector.SimpleOnGestureListener
    {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent motionEvent)
        {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float velocityX, float velocityY)
        {
            boolean result = false;

            try
            {
                float diffY = motionEvent2.getY() - motionEvent1.getY();
                float diffX = motionEvent2.getX() - motionEvent1.getX();

                if (Math.abs(diffX) > Math.abs(diffY))
                {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                    {
                        if (diffX > 0)
                        {
                            onSwipeRight();
                        }
                        else
                        {
                            onSwipeLeft();
                        }
                        result = true;
                    }
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD)
                {
                    if (diffY > 0)
                    {
                        onSwipeBottom();
                    }
                    else
                    {
                        onSwipeTop();
                    }
                    result = true;
                }
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {}
    public void onSwipeLeft() {}
    public void onSwipeTop() {}
    public void onSwipeBottom() {}
}
