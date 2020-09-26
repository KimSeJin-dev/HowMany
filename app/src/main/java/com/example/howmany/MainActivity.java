package com.example.howmany;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.howmany.R.id.webView;

public class MainActivity extends AppCompatActivity {
    /*-------------------------------------------------*/
    private final String TAG = getClass().getSimpleName();
    ArrayList<PeopleList> arrayList = new ArrayList<>();

    private final String BASE_URL = "http://emoclew.pythonanywhere.com";
    private MyAPI mMyAPI;

    private TextView mListTv;
    private Button buttonScan;
    private Button mliveCount;
    private IntentIntegrator qrScan;
    private WebView mWebView;
    private final long FINISH_INTERVAL_TIME = 2000;
    private long lastTimeBackPressed = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qrScan = new IntentIntegrator(this);

        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.setOrientationLocked(false);
                qrScan.setPrompt("QR코드를 정중앙에 위치시켜 스캔해주세요");
                qrScan.initiateScan();
            }
        });

        mliveCount = findViewById(R.id.livecount);

        initMyAPI(BASE_URL);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final CustomAdapter customAdapter = new CustomAdapter();
        recyclerView.setAdapter(customAdapter);



        Call<List<PostItem>> getCall = mMyAPI.get_posts();
            getCall.enqueue(new Callback<List<PostItem>>() {
                @Override
                public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                    if (response.isSuccessful()) {
                        List<PostItem> mList = response.body();

                        arrayList.clear();
                        for (PostItem item : mList) {
                            PeopleList peopleList = new PeopleList();
                            peopleList.setName(item.getName());
                            peopleList.setMajor(item.getMajor());
                            peopleList.setPhone_num(item.getPhone_num());
                            arrayList.add(peopleList);

                        }
                        customAdapter.notifyDataSetChanged();


                    } else {
                        Log.d(TAG, "Error Code");
                    }
                }

                @Override
                public void onFailure(Call<List<PostItem>> call, Throwable t) {

                }
            });
            final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {


                    Call<List<PostItem>> getCall = mMyAPI.get_posts();
                    getCall.enqueue(new Callback<List<PostItem>>() {
                        @Override
                        public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                            if (response.isSuccessful()) {
                                List<PostItem> mList = response.body();
                                arrayList.clear();
                                for (PostItem item : mList) {
                                    PeopleList peopleList = new PeopleList();
                                    peopleList.setName(item.getName());
                                    peopleList.setMajor(item.getMajor());
                                    peopleList.setPhone_num(item.getPhone_num());
                                    arrayList.add(peopleList);


                                }
                                customAdapter.notifyDataSetChanged();


                            } else {
                                Log.d(TAG, "Error Code : ");
                            }
                        }

                        @Override
                        public void onFailure(Call<List<PostItem>> call, Throwable t) {

                        }
                    });

                    customAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });


    }


    private void initMyAPI(String baseUrl){

        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {
             Toast.makeText(MainActivity.this, "취소!", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(MainActivity.this, "스캔완료!", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                } catch (JSONException e) {
                   String myUrl = result.getContents();//intent.getStringExtra("webview_addr");     // 접속 URL (내장HTML의 경우 왼쪽과 같이 쓰고 아니면 걍 URL)

                    setContentView(R.layout.hotcheck_view);
                    // 웹뷰 셋팅
                    mWebView = (WebView) findViewById(webView);//xml 자바코드 연결
                    mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용
                    mWebView.loadUrl(myUrl);//웹뷰 실행
                    mWebView.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
                    mWebView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {//뒤로가기 버튼 이벤트
//
//        switch(keyCode)
//            mWebView.canGoBack()
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {//웹뷰에서 뒤로가기 버튼을 누르면 뒤로가짐
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            //mWebView.goBack();
//            return true;
//        }
//        return false;
//    }

    @Override
    public void onBackPressed() {
        if ((mWebView instanceof  WebView) && mWebView.canGoBack()) {
            String Url = mWebView.getUrl();
            if(mWebView.getUrl().equals(Url))
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                super.onBackPressed();
            }
                mWebView.goBack();

        }
        else if((mWebView instanceof  WebView) && mWebView.getUrl().equals("http://emoclew.pythonanywhere.com/")){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

            super.onBackPressed();
        }
        else {
            System.exit(0);
        }
    }



    private class WebViewClientClass extends WebViewClient {//페이지 이동
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL",url);
            view.loadUrl(url);
            return true;
        }
    }




    public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> { // 리사이클러뷰


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.information_recyclerview_item, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

            ((CustomViewHolder) holder).person_name.setText(arrayList.get(position).getName());
            ((CustomViewHolder) holder).person_major.setText(arrayList.get(position).getMajor());
            ((CustomViewHolder) holder).person_phone_num.setText(arrayList.get(position).getPhone_num());



        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        private class CustomViewHolder extends RecyclerView.ViewHolder {

            TextView person_name;
            TextView person_major;
            TextView person_phone_num;

            public CustomViewHolder(View view) {
                super(view);
                person_name = (TextView) view.findViewById(R.id.person_name);
                person_major = (TextView) view.findViewById(R.id.person_major);
                person_phone_num = (TextView) view.findViewById(R.id.person_phone_num);


            }
        }
    }

}