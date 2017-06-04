package com.example.sam.myapplication4;


        import android.content.Context;
        import android.content.res.Resources;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Rect;
        import android.util.Log;
        import android.view.View;
        import android.view.WindowManager;

        import java.io.FileInputStream;

/**
 * Created by sam on 17-6-3.
 */

public class Background extends View {
    WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    int width = wm.getDefaultDisplay().getWidth();
    public int[] danger = new int[25];
    public double len = 0.16;
    public int n = 5;
    public int[] wall = new int[60];
    public int[] info = {0, 0, 0, 0, 0,
            1, 1, 0, 1, 1,
            1, 1, 0, 1, 0,
            0, 0, 0, 0, 0,
            1, 1, 1, 1, 1};
    public int[] direct = {-1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1};
    public Rect[] r = new Rect[n * n];
    public Rect[] sensor = new Rect[2];
    public int[] sensorpos = {6, 8};
    public int drawing = 0;
    public int mode = 0;
    public Rect refresh = new Rect((int) ((int) width * 0.1 + width * len * 2), (int) ((int) width * 0.13 + width * len * 6),
            (int) ((int) width * 0.1 + width * len * 3), (int) ((int) width * 0.13 + width * len * 7));
    Bitmap[] bitmaps;
    Bitmap upimg = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow_1_up);
    Bitmap rightimg = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow_1_right);
    Bitmap downimg = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow_1_down);
    Bitmap leftimg = BitmapFactory.decodeResource(getResources(), R.mipmap.arrow_1_left);
    Bitmap solution = BitmapFactory.decodeResource(getResources(), R.mipmap.refresh);
    Bitmap outk = BitmapFactory.decodeResource(getResources(), R.mipmap.out);
    Bitmap mode3 = BitmapFactory.decodeResource(getResources(), R.mipmap.exit);
    Bitmap mode2 = BitmapFactory.decodeResource(getResources(), R.mipmap.wall);
    Bitmap mode1 = BitmapFactory.decodeResource(getResources(), R.mipmap.block);
    Bitmap mode0 = BitmapFactory.decodeResource(getResources(), R.mipmap.camera_1);
    Bitmap newBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.block_3);
    Bitmap camera = BitmapFactory.decodeResource(getResources(), R.mipmap.camera_4);

    Rect modeChange = new Rect();
    public Background(Context context) {
        super(context);
        sensor[0] = new Rect(0, 0, 0, 0);
        sensor[0].left = (int) ((int) width * 0.1 + width * len * 1 + 20);
        sensor[0].right = (int) ((int) width * 0.1 + width * len * (1 + 1) - 20);
        sensor[0].top = (int) ((int) width * 0.1 + width * len * 2 + 20);
        sensor[0].bottom = (int) ((int) width * 0.1 + width * len * 3 - 20);
        sensor[1] = new Rect(0, 0, 0, 0);
        sensor[1].left = (int) ((int) width * 0.1 + width * len * 3 + 20);
        sensor[1].right = (int) ((int) width * 0.1 + width * len * (3 + 1) - 20);
        sensor[1].top = (int) ((int) width * 0.1 + width * len * (n - 3) + 20);
        sensor[1].bottom = (int) ((int) width * 0.1 + width * len * (n - 3 + 1) - 20);
        modeChange.left = (int) ((int) width * 0.1 + width * len * 0 + 20);
        modeChange.right = (int) ((int) width * 0.1 + width * len * 1 - 20);
        modeChange.top = (int) ((int) width * 0.07 + width * len * 0 + 20);
        modeChange.bottom = (int) ((int) width * 0.07 + width * len * 1 - 20);
        Resources resources = getResources();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint line = new Paint();
        line.setColor(0xee295e6a);
        line.setStrokeWidth((float) 15.0);
        Paint modeC = new Paint();
        modeC.setColor(Color.WHITE);
        modeC.setTextSize((float) 60.0);
        canvas.drawRect(modeChange, modeC);
        refresh.left = 0;
        refresh.right = 0;
        switch (mode) {

            case 0: {
                canvas.drawBitmap(mode0, null, modeChange, modeC);
                modeC.setColor(Color.BLACK);
                canvas.drawText("Setting Camera", modeChange.right + 10, modeChange.bottom - 30, modeC);
                break;
            }
            case 1: {
                canvas.drawBitmap(mode1, null, modeChange, modeC);
                modeC.setColor(Color.BLACK);
                canvas.drawText("Setting Block", modeChange.right + 10, modeChange.bottom - 30, modeC);
                break;
            }
            case 2: {
                canvas.drawBitmap(mode2, null, modeChange, modeC);
                modeC.setColor(Color.BLACK);
                canvas.drawText("Setting Wall", modeChange.right + 10, modeChange.bottom - 30, modeC);
                break;
            }
            case 3: {
                canvas.drawBitmap(mode3, null, modeChange, modeC);
                refresh.left = (int) ((int) width * 0.1 + width * len * 2);
                refresh.right = (int) ((int) width * 0.1 + width * len * 3);
                modeC.setColor(Color.BLACK);
                canvas.drawText("Solution Generation", modeChange.right + 10, modeChange.bottom - 30, modeC);
                break;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                r[i * n + j] = new Rect();
                r[i * n + j].left = (int) ((int) width * 0.1 + width * len * j) + 10;
                r[i * n + j].right = (int) ((int) width * 0.1 + width * len * (j + 1)) - 10;
                r[i * n + j].top = (int) ((int) width * 0.1 + width * len * (i + 1)) + 10;
                r[i * n + j].bottom = (int) ((int) width * 0.1 + width * len * (i + 2)) - 10;
                Paint paint = new Paint();
                //Log.d("wall", String.valueOf(wall[i*10 + j*2]));
                if (wall[i * 10 + j * 2] == 1) canvas.drawLine(r[i * n + j].left - 10, r[i * n + j].top - 10, r[i * n + j].right + 10, r[i * n + j].top - 10, line);
                if (wall[i * 10 + j * 2 + 1] == 1) canvas.drawLine(r[i * n + j].left - 10, r[i * n + j].top - 10, r[i * n + j].left - 10, r[i * n + j].bottom + 10, line);
                if (info[i * n + j] == 0) {
                    paint.setColor(0xeef6f6f6);
                }
                else if (danger[i * n + j] == 0) paint.setColor(0xeec2faf1);
                else if (danger[i * n + j] == 2) paint.setColor(0xeeff0000);
                else paint.setColor(0xeeffa500);
                canvas.drawRect(r[i * n + j], paint);
                if (mode == 1 && info[i * n + j] == 0) canvas.drawBitmap(newBitmap, null, r[i * n + j], paint); else
                if (mode == 0 && info[i * n + j] == 0 && !(r[i * n + j].contains(sensor[0].left, sensor[0].top)) && !(r[i * n + j].contains(sensor[1].left, sensor[1].top))) canvas.drawBitmap(newBitmap, null, r[i * n + j], paint);
            }
        }
        for (int i = 0; i < n; i++) {
            if (wall[50 + i] == 1) {
                canvas.drawLine(r[20 + i].left - 10, r[20 + i].bottom + 10, r[20 + i].right + 10, r[20 + i].bottom + 10, line);
            }
            if (wall[55 + i] == 1) {
                canvas.drawLine(r[4 + i * 5].right + 10, r[4 + i * 5].top - 10, r[4 + i * 5].right + 10, r[4 + i * 5].bottom + 10, line);
            }
        }
        if (mode == 0) {
            Paint test = new Paint();
            test.setColor(0xee295e6a);
            canvas.drawRect(sensor[0], test);
            canvas.drawBitmap(camera, null, sensor[0], test);
            test.setColor(0xee295e6a);
            canvas.drawRect(sensor[1], test);
            canvas.drawBitmap(camera, null, sensor[1], test);
        } else
        if (mode == 3) {
            Paint test = new Paint();
            test.setColor(Color.WHITE);
            canvas.drawBitmap(solution, null, refresh, test);
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if (info[i * 5 + j] == 1 && direct[i * 5 + j] != -1) {
                        //Log.d("123",String.valueOf(direct[i * 5 + j]));
                        switch (direct[i * 5 + j]) {
                            case 0: {
                                canvas.drawBitmap(upimg, null, r[i * 5 + j], test);
                                break;
                            }
                            case 1: {
                                canvas.drawBitmap(rightimg, null, r[i * 5 + j], test);
                                break;
                            }
                            case 2: {
                                canvas.drawBitmap(downimg, null, r[i * 5 + j], test);
                                break;
                            }
                            case 3: {
                                canvas.drawBitmap(leftimg, null, r[i * 5 + j], test);
                                break;
                            }
                            case 4: {
                                canvas.drawBitmap(outk, null, r[i * 5 + j], test);
                            }
                        }
                    }
                }
            }
        }
        super.onDraw(canvas);
    }
}
