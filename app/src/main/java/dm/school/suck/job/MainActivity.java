package dm.school.suck.job;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    JWebSocketClient client;
    OkHttpClient client_ok;
    ListView listView;
    DBOpenHelper dbOpenHelper;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity=this;
        dbOpenHelper=new DBOpenHelper(getApplicationContext());
        client_ok = new OkHttpClient();
        listView = findViewById(R.id.list_s);
        listView.setAdapter(new Adapter(this));
        initSocketClient();
        try {
            client.connectBlocking();
        } catch (InterruptedException e) { e.printStackTrace(); }
        if (client != null && client.isOpen()) {
//            client.send("你好");
        }
        Button Start=findViewById(R.id.btn_start);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            String a=http_re();
                            JSONArray obj_start = new JSONArray(a);

                            ArrayList<DB_df> s=new ArrayList<>();
                          for(int i=0;i<obj_start.length();i++){
                              JSONObject obj=obj_start.getJSONObject(i);
                              String id=obj.getString("a");
                              String price=obj.getString("p");
                              String qty=obj.getString("q");
                              String time=obj.getString("T");
                              String status="n";
                              long dv = Long.valueOf(time)*1000;// its need to be in milisecond
                              Date df = new java.util.Date(dv);
                              String times = new SimpleDateFormat("hh:mm:ss").format(df);
                              s.add(new DB_df(id,times,price,qty,status));

                          }
                            dbOpenHelper.insertDummyDf(s);
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
    private static final long HEART_BEAT_RATE = 10 * 1000;
    //每隔10秒進行一次對長連接的心跳檢測
    private Handler mHandler = new Handler();
    private Runnable heartBeatRunnable = new Runnable() {
        @Override public void run() {
            if (client != null) {
                if (client.isClosed()) {
                    reconnectWs(); }
            } else {
                //如果client已為空，重新初始化websocket
                initSocketClient();
            }
            //定時對長連接進行心跳檢測
            mHandler.postDelayed(this, HEART_BEAT_RATE); }



    };

    private void initSocketClient() {


        URI uri = URI.create("wss://stream.yshyqxx.com/ws/btcusdt@aggTrade");
        client = new JWebSocketClient(uri) {
            @Override public void onMessage(String message) {
                //message就是接收到的消息
                Log.e("JWebSClientService", message);

                try {
                JSONObject obj=new JSONObject(message);
                String status;
                ArrayList<DB_df> df=dbOpenHelper.searchDB(obj.getString("a"));
                DB_df db_df =df.get(0);
                if( Double.parseDouble(db_df.getPrice())> Double.parseDouble(obj.getString("p"))){
                    status="r";
                }else {
                    status="g";
                }

                     df=new ArrayList<DB_df>();
                     df.add(new DB_df(obj.getString("a"),obj.getString("T"),obj.getString("p"),obj.getString("q"),status));
                    dbOpenHelper.updateDummyData(df);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listView.setAdapter(new Adapter(activity));
            }

        };
    }

    /** * 開啟重連 */ private void reconnectWs() {
        mHandler.removeCallbacks(heartBeatRunnable);
        new Thread() { @Override public void run() {
            try { //重連
                client.reconnectBlocking();
            } catch (InterruptedException e) { e.printStackTrace(); } } }.start(); }



    public  String  http_re()
            throws IOException {


        Request request = new Request.Builder()
                .url("https://api.yshyqxx.com/api/v1/aggTrades?symbol=BTCUSDT&limit=1000")
                .get()
                .build();

        Call call = client_ok.newCall(request);
        Response response = call.execute();
        return response.body().string();
    }



            }



