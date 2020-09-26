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
    //server의 url을 적어준다.
    private final String BASE_URL = "http://emoclew.pythonanywhere.com";
    private MyAPI mMyAPI;

    private TextView mListTv;
    // 웹서버 관련 코드
    /*-------------------------------------------------*/


    //view Objects
    private Button buttonScan;
    private Button mliveCount;
    private TextView textViewName, textViewAddress, textViewResult;
    //qr code scanner object
    private IntentIntegrator qrScan;
    private WebView mWebView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        buttonScan = (Button) findViewById(R.id.buttonScan);
        qrScan = new IntentIntegrator(this);

        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qrScan.setPrompt("Scanning...");
                qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
            }
        });

        mliveCount = findViewById(R.id.livecount);

        initMyAPI(BASE_URL);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final CustomAdapter customAdapter = new CustomAdapter();
        recyclerView.setAdapter(customAdapter);


            Log.d(TAG, "Test01");
            Call<List<PostItem>> getCall = mMyAPI.get_posts();
            Log.d(TAG, "Test02");
            getCall.enqueue(new Callback<List<PostItem>>() {
                @Override
                public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Test03");
                        List<PostItem> mList = response.body();
                        Log.d(TAG, "Test04");

                        arrayList.clear();
                        for (PostItem item : mList) {
                            Log.d(TAG, "Test05");
                            PeopleList peopleList = new PeopleList();
                            Log.d(TAG, "Test06");
                            peopleList.setName(item.getName());
                            Log.d(TAG, "Test07");
                            peopleList.setMajor(item.getMajor());
                            Log.d(TAG, "Test08");
                            peopleList.setPhone_num(item.getPhone_num());
                            Log.d(TAG, "Test09");
                            arrayList.add(peopleList);
                            Log.d(TAG, "Test10");

                            Log.d(TAG, "Fxxking");

                        }
                        customAdapter.notifyDataSetChanged();


                    } else {
                        Log.d(TAG, "Status Code : " + response.code());
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


                    Log.d(TAG, "Test01");
                    Call<List<PostItem>> getCall = mMyAPI.get_posts();
                    Log.d(TAG, "Test02");
                    getCall.enqueue(new Callback<List<PostItem>>() {
                        @Override
                        public void onResponse(Call<List<PostItem>> call, Response<List<PostItem>> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, "Test03");
                                List<PostItem> mList = response.body();
                                Log.d(TAG, "Test04");

                                arrayList.clear();
                                for (PostItem item : mList) {
                                    Log.d(TAG, "Test05");
                                    PeopleList peopleList = new PeopleList();
                                    Log.d(TAG, "Test06");
                                    peopleList.setName(item.getName());
                                    Log.d(TAG, "Test07");
                                    peopleList.setMajor(item.getMajor());
                                    Log.d(TAG, "Test08");
                                    peopleList.setPhone_num(item.getPhone_num());
                                    Log.d(TAG, "Test09");
                                    arrayList.add(peopleList);
                                    Log.d(TAG, "Test10");

                                    Log.d(TAG, "Fxxking");

                                }
                                customAdapter.notifyDataSetChanged();


                            } else {
                                Log.d(TAG, "Status Code : " + response.code());
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




        //구분선
        //recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;

        //View Objects





        //intializing scan object

        //button onClick

        /*
        lineView = (LineView) findViewById(R.id.line_view);

        //list data
        List<AirQualityData> data = db.todayAirQualityData();

        //lable
        ArrayList<String> hour = new ArrayList<String>();
        //3 data sets

        ArrayList<Integer> dataList_10 = new ArrayList<>();
        ArrayList<Integer> dataList_2_5 = new ArrayList<>();
        ArrayList<Integer> dataList_1_0 = new ArrayList<>();

        //put db data into arrays
        for(AirQualityData datum : data) {
            hour.add(String.valueOf(datum.getHour()));
            dataList_10.add(datum.getPm10());
            dataList_2_5.add(datum.getPm2_5());
            dataList_1_0.add(datum.getPm1_0());
        }


        // put data sets into datalist
        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();
        dataLists.add(dataList_10);
        dataLists.add(dataList_2_5);
        dataLists.add(dataList_1_0);


        //put data sets into datalist
        ArrayList<ArrayList<Integer>> dataLists = new ArrayList<>();

        //draw line graph
        lineView.setDrawDotLine(true);
        lineView.setShowPopup(LineView.ShOW_POPUPS_NONE);
        lineView.setColorArray(new int[]{
                Color.parseColor("e74c3c") , Color.parseColor("#2980b9"), Color.parseColor(("1abc9c"))
        });

        lineView.setBottomTextList(hour);
        lineView.setDataList(dataLists);
        */



//        @Override
//        public void onClick(View v) {
//            if( v == buttonScan) { //qr코드 버튼 클릭시
//                //scan option
//                qrScan = new IntentIntegrator(this);
//                qrScan.setPrompt("Scanning...");
//                qrScan.setOrientationLocked(false);
//                qrScan.initiateScan();
//            }
//
//            else if( v == mliveCount){
//
//            }
//        }

    }


    /*-------------------------------------------------*/
    private void initMyAPI(String baseUrl){

        Log.d(TAG,"initMyAPI : " + baseUrl);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mMyAPI = retrofit.create(MyAPI.class);
    }
    //Retrofit 객체를 생성하고 이 객체를 이용해서, API service를 create 해준다.


    /*-------------------------------------------------*/




    //Getting the scan results

    //qr코드 승인 , qr코드 없을시
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //qrcode 가 없으면
            if (result.getContents() == null) {

                Log.d(TAG, "example_test_01");
                Toast.makeText(MainActivity.this, "취소!", Toast.LENGTH_SHORT).show();
            } else {

                Log.d(TAG, "example_test_02");
                //qrcode 결과가 있으면
                Toast.makeText(MainActivity.this, "스캔완료!", Toast.LENGTH_SHORT).show();
                try {
                    //data를 json으로 변환


                    Log.d(TAG, "example_test_03");
                    JSONObject obj = new JSONObject(result.getContents());
                } catch (JSONException e) {

                    Intent intent = getIntent();
                    String myUrl = result.getContents();//intent.getStringExtra("webview_addr");     // 접속 URL (내장HTML의 경우 왼쪽과 같이 쓰고 아니면 걍 URL)

                    setContentView(R.layout.hotcheck_view);
                    // 웹뷰 셋팅
                    mWebView = (WebView) findViewById(webView);//xml 자바코드 연결
                    mWebView.getSettings().setJavaScriptEnabled(true);//자바스크립트 허용
                    Log.d(myUrl,"테스트2");
                    mWebView.loadUrl(myUrl);//웹뷰 실행
                    Log.d(myUrl,"테스트3");
                    mWebView.setWebChromeClient(new WebChromeClient());//웹뷰에 크롬 사용 허용//이 부분이 없으면 크롬에서 alert가 뜨지 않음
                    mWebView.setWebViewClient(new WebViewClientClass());//새창열기 없이 웹뷰 내에서 다시 열기//페이지 이동 원활히 하기위해 사용






                    Log.d(TAG, "example_test_04");
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            Log.d(TAG, "example_test_05");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {//뒤로가기 버튼 이벤트
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {//웹뷰에서 뒤로가기 버튼을 누르면 뒤로가짐
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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