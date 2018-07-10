package saadandaakash.latexreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class PaintScreen extends View {

    public static final int BRUSH_SIZE = 20;
    public static final int BRUSH_COLOR = Color.BLACK;
    public static final int BACKGROUND_COLOR = Color.WHITE;

    private Paint style;
    private Paint backgroundStyle;
    private Canvas canvas;
    private Bitmap bitmap;
    private Path currentPath;
    private ArrayList<Path> totalPaths;
    private float pastX;
    private float pastY;

    public PaintScreen(Context context) {
        this(context, null);
    }

    public PaintScreen(Context context, AttributeSet attributeSet){
        super(context, attributeSet);

        //Paint will store information about the brush style
        //TODO: Correct the style of brush we want to use
        style = new Paint();
        style.setAntiAlias(true);
        style.setColor(BRUSH_COLOR);
        style.setStyle(Paint.Style.STROKE);
        style.setStrokeJoin(Paint.Join.ROUND);
        style.setStrokeCap(Paint.Cap.ROUND);
        style.setStrokeWidth(BRUSH_SIZE);

        currentPath = new Path();
        totalPaths = new ArrayList<>();
    }

    public void newInstance(DisplayMetrics dm){
            bitmap = Bitmap.createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, 0, 0, style);
        canvas.drawPath(currentPath, style);
        for(Path p: totalPaths){
            canvas.drawPath(p, style);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        if(action == MotionEvent.ACTION_DOWN){
            touchDown(x, y);
            invalidate();
        } else if(action == MotionEvent.ACTION_UP) {
            touchUp(x, y);
            invalidate();
        } else if(action == MotionEvent.ACTION_MOVE){
            touchMove(x, y);
            invalidate();
        }
        return true;
    }

    private void touchDown(float x, float y){

        //Resets the original path that was on the screen, so we can only add the new path to totalPath
        currentPath = new Path();
        currentPath.moveTo(x, y);

        pastX = x;
        pastY = y;
    }

    private void touchUp(float x, float y){
        totalPaths.add(currentPath);
        currentPath = new Path();
    }

    private void touchMove(float x, float y){

        System.out.println(x + " " + y);
        float dX = pastX - x;
        float dY = pastY - y;

        if(Math.abs(dX) > 15 || Math.abs(dY) > 15){
            currentPath.quadTo(x, y, x, y);
            pastX = x;
            pastY = y;
        }
    }

    private void clear(){
        currentPath.reset();
        invalidate();
    }
}
