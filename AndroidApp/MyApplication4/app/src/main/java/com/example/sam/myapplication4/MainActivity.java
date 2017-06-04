package com.example.sam.myapplication4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class MainActivity extends AppCompatActivity{
    String result = "";
    DataOutputStream os = null;
    BufferedReader in = null;
    int lastX, lastY;
    int[] wall = new int[60];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        final Background bg = new Background(this);
        for (int i = 0; i < 60; i++) wall[i] = 1;
        bg.wall = wall;
        //bg.setBackgroundColor(Color.BLUE);
        bg.invalidate();
        setContentView(bg);
        bg.setClickable(true);

        setTitle("Crowd Channeling");
        bg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (bg.refresh.contains(x, y)) {
                            JSONObject t = new JSONObject();
                            try {
                                String temp = "";
                                for (int i = 0; i < 25; i++) temp += String.valueOf(bg.info[i]);
                                t.put("grid", temp);
                                temp = "";
                                for (int i = 0; i < 60; i++) temp += String.valueOf(bg.wall[i]);
                                t.put("wall", temp);
                                t.put("sensor1", 20);
                                t.put("sensor2", 21);

                            } catch (Exception e) {
                                Log.d("errorjson", e.toString());
                            }

                            String content = String.valueOf(t);
                            Log.d("content", content);
                            try {
                                result = "";
                                String tempgrid = "";
                                for (int i = 0; i < 25; i++) tempgrid += String.valueOf(bg.info[i]);
                                t.put("grid", tempgrid);
                                String tempwall = "";
                                for (int i = 0; i < 60; i++) tempwall += String.valueOf(bg.wall[i]);
                                t.put("wall", tempwall);

                                URL url = new URL("http://139.219.229.71/android?id=-1&grid=" + tempgrid + "&wall=" + tempwall + "&sensor1=" + String.valueOf(bg.sensorpos[0]) + "&sensor2=" + String.valueOf(bg.sensorpos[1]));
                                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                conn.setConnectTimeout(5000);

                                //conn.setRequestProperty("MyProperty", "hi");
                                conn.setRequestMethod("GET");
                                conn.setRequestProperty("Charset", "UTF-8");
                                result = "";
                                if (conn.getResponseCode() == 200) {
                                    in = new BufferedReader(
                                            new InputStreamReader(conn.getInputStream()));
                                    String line;
                                    if (conn.getResponseCode() == 200) {
                                        while ((line = in.readLine()) != null) {
                                            result += line;
                                        }
                                    }
                                    Log.d("g", result);
                                }

                                /*conn.setDoOutput(true);
                                conn.setDoInput(true);
                                conn.setRequestMethod("POST");
                                conn.setRequestProperty("Content-Type", "application/json");
                                content = "{'a':'b'}";
                                os = new DataOutputStream(conn.getOutputStream());
                                byte[] contentb = content.getBytes("utf-8");

                                os.write(contentb, 0, contentb.length);
                                os.flush();
                                os.close();
                                in = new BufferedReader(
                                        new InputStreamReader(conn.getInputStream()));
                                String line;
                                if (conn.getResponseCode() == 200) {
                                    while ((line = in.readLine()) != null) {
                                        result += line;
                                    }
                                }*/
                                t = new JSONObject(result);
                                String temp = t.getString("path");
                                int[] direct = new int[25];
                                for (int i = 0; i < 25; i++) {
                                    switch (temp.charAt(i)) {
                                        case '0': {
                                            direct[i] = 0;
                                            break;
                                        }
                                        case '1': {
                                            direct[i] = 1;
                                            break;
                                        }
                                        case '2': {
                                            direct[i] = 2;
                                            break;
                                        }
                                        case '3': {
                                            direct[i] = 3;
                                            break;
                                        }
                                        case '4': {
                                            direct[i] = 4;
                                            break;
                                        }
                                    }
                                }
                                temp = t.getString("color");
                                int[] danger = new int[25];
                                for (int i = 0; i < 25; i++) {
                                    switch (temp.charAt(i)) {
                                        case '0': {
                                            danger[i] = 0;
                                            break;
                                        }
                                        case '1': {
                                            danger[i] = 1;
                                            break;
                                        }
                                        case '2': {
                                            danger[i] = 2;
                                            break;
                                        }
                                    }
                                }
                                bg.direct = direct;
                                bg.danger = danger;
                                bg.invalidate();
                            } catch (Exception e) {
                                Log.d("error", e.toString());
                            }
                            Log.d("result", result);

                        } else
                        if (bg.modeChange.contains(x, y)) {
                            for (int i = 0; i < 25; i ++) {
                                bg.danger[i] = 0;
                            }
                            bg.mode = (bg.mode + 1) % 4;
                            Log.d("test", String.valueOf(bg.mode));
                            bg.invalidate();
                        } else
                        if (bg.mode == 0) {
                            if (bg.sensor[0].contains(x, y)) bg.drawing = 1;
                            else if (bg.sensor[1].contains(x, y)) bg.drawing = 2;
                            else {
                                bg.drawing = 0;
                                return false;
                            }
                        } else
                        if (bg.mode == 1) {
                            x = (int) ((int) x - bg.width * 0.1);
                            x = (int) ((int) x / (bg.width * bg.len));
                            if (x < 0) x = 0;
                            if (x >= bg.n) x = bg.n - 1;
                            y = (int) ((int) y - bg.width * 0.26);
                            y = (int) ((int) y / (bg.width * bg.len));
                            if (y < 0) y = 0;
                            if (y >= bg.n) y = bg.n - 1;
                            bg.info[y * 5 + x] = 1 - bg.info[y * 5 + x];
                            bg.invalidate();
                        } else
                        if (bg.mode == 2) {
                            lastX = x;
                            lastY = y;
                        }
                        Log.d("drawing", String.valueOf(bg.drawing));
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //Log.d("move", String.valueOf(bg.drawing));
                        if (bg.drawing == 0) return false;

                        bg.sensor[bg.drawing - 1].left = (int) ((int) -bg.width * bg.len / 2 + 20 + event.getX());
                        bg.sensor[bg.drawing - 1].top = (int) ((int) -bg.width * bg.len / 2 + 20 + event.getY());
                        bg.sensor[bg.drawing - 1].right = (int) ((int) bg.width * bg.len / 2 - 20 + event.getX());
                        bg.sensor[bg.drawing - 1].bottom = (int) ((int) bg.width * bg.len / 2 - 20 + event.getY());
                        bg.invalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (bg.mode == 0) {
                            if (bg.drawing != 0) {
                                x = (int) ((int) x - bg.width * 0.1);
                                x = (int) ((int) x / (bg.width * bg.len));
                                if (x < 0) x = 0;
                                if (x >= bg.n) x = bg.n - 1;
                                y = (int) ((int) y - bg.width * 0.26);
                                y = (int) ((int) y / (bg.width * bg.len));
                                if (y < 0) y = 0;
                                if (y >= bg.n) y = bg.n - 1;
                                if (bg.sensorpos[bg.drawing - 1] != y * 5 + x) {
                                    bg.sensor[bg.drawing - 1].left = (int) ((int) bg.width * 0.1 + bg.width * bg.len * x + 20);
                                    bg.sensor[bg.drawing - 1].right = (int) ((int) bg.width * 0.1 + bg.width * bg.len * (x + 1) - 20);
                                    bg.sensor[bg.drawing - 1].top = (int) ((int) bg.width * 0.26 + bg.width * bg.len * y + 20);
                                    bg.sensor[bg.drawing - 1].bottom = (int) ((int) bg.width * 0.26 + bg.width * bg.len * (y + 1) - 20);
                                    bg.sensorpos[bg.drawing - 1] = y * 5 + x;
                                    bg.invalidate();
                                } else {
                                    try {
                                        URL url = new URL("http://139.219.229.71/android?id=" + String.valueOf(bg.drawing - 1));
                                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                        conn.setConnectTimeout(5000);

                                        //conn.setRequestProperty("MyProperty", "hi");
                                        conn.setRequestMethod("GET");
                                        conn.setRequestProperty("Charset", "UTF-8");
                                        result = "";
                                        if (conn.getResponseCode() == 200) {
                                            in = new BufferedReader(
                                                    new InputStreamReader(conn.getInputStream()));
                                            String line;
                                            if (conn.getResponseCode() == 200) {
                                                while ((line = in.readLine()) != null) {
                                                    result += line;
                                                }
                                            }
                                            Log.d("g", result);
                                        }
                                        JSONObject t = new JSONObject(result);
                                        Log.d("1", t.getString("numPeople"));
                                        Log.d("2", t.getString("noiselevel"));
                                        Log.d("3", t.getString("temperature"));
                                        Dialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                                                .setTitle("Sensor No." + String.valueOf(bg.drawing))
                                                .setMessage("Temperature: " + t.getString("temperature") + " DEG\n" +
                                                        "Noise Level: "+ t.getString("noiselevel").charAt(0) + " mW\n" +
                                                        "Number People: " + t.getString("numPeople") + "\n" +
                                                        "Acc (x,y,z): " + t.getString("xAccelData") + "," + t.getString("yAccelData") + "," + t.getString("zAccelData")).
                                                create();
                                        alertDialog.show();
                                    } catch (Exception e) {
                                        Log.d("error", e.toString());
                                    }
                                    bg.sensor[bg.drawing - 1].left = (int) ((int) bg.width * 0.1 + bg.width * bg.len * x + 20);
                                    bg.sensor[bg.drawing - 1].right = (int) ((int) bg.width * 0.1 + bg.width * bg.len * (x + 1) - 20);
                                    bg.sensor[bg.drawing - 1].top = (int) ((int) bg.width * 0.26 + bg.width * bg.len * y + 20);
                                    bg.sensor[bg.drawing - 1].bottom = (int) ((int) bg.width * 0.26 + bg.width * bg.len * (y + 1) - 20);
                                    bg.invalidate();
                                }
                                //Log.d("end-position", String.valueOf(x) + "," + String.valueOf(y));
                            }
                            bg.drawing = 0;
                        } else
                        if (bg.mode == 2) {
                            if (Math.abs(event.getX() - lastX) > Math.abs(event.getY() - lastY)) {
                                int dir = -1;
                                if (event.getX() - lastX > 0) dir = 1;

                                lastX = (int) ((int) lastX - bg.width * 0.1);
                                lastX = (int) ((int) lastX / (bg.width * bg.len));
                                if (lastX < 0 || lastX >= bg.n) return false;
                                lastY = (int) ((int) lastY - bg.width * 0.26);
                                lastY = (int) ((int) lastY / (bg.width * bg.len));
                                if (lastY < 0 || lastY >= bg.n) return false;
                                Log.d("dir",String.valueOf(dir));
                                if (dir == 1) {
                                    if (lastX == 4) {
                                        bg.wall[55 + lastY] = 1 - bg.wall[55 + lastY];
                                    } else {
                                        bg.wall[lastY * 10 + lastX * 2 + 3] = 1 - bg.wall[lastY * 10 + lastX * 2 + 3];
                                    }
                                } else {
                                    bg.wall[lastY * 10 + lastX * 2 + 1] = 1 - bg.wall[lastY * 10 + lastX * 2 + 1];
                                }

                            } else {
                                int dir = -1;
                                if (event.getY() - lastY > 0) dir = 1;
                                lastX = (int) ((int) lastX - bg.width * 0.1);
                                lastX = (int) ((int) lastX / (bg.width * bg.len));
                                if (lastX < 0 || lastX >= bg.n) return false;
                                lastY = (int) ((int) lastY - bg.width * 0.26);
                                lastY = (int) ((int) lastY / (bg.width * bg.len));
                                if (lastY < 0 || lastY >= bg.n) return false;

                                if (dir == 1) {
                                    if (lastY == 4) {
                                        bg.wall[50 + lastX] = 1 - bg.wall[50 + lastX];
                                    } else {
                                        bg.wall[lastY * 10 + lastX * 2 + 10] = 1 - bg.wall[lastY * 10 + lastX * 2 + 10];
                                    }
                                } else {
                                    bg.wall[lastY * 10 + lastX * 2] = 1 - bg.wall[lastY * 10 + lastX * 2];
                                }
                            }
                            bg.invalidate();
                        }

                        break;
                }
                return false;
            }
        });

    }
}
