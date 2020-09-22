package com.example.howmany;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mliveCount = findViewById(R.id.livecount);
        mliveCount.setOnClickListener(this);

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
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(this);





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


    @Override
    public void onClick(View v) {
        if( v == buttonScan) { //qr코드 버튼 클릭시
            //scan option
            qrScan = new IntentIntegrator(this);
            qrScan.setPrompt("Scanning...");
            qrScan.setOrientationLocked(false);
            qrScan.initiateScan();
        }

        else if( v == mliveCount){

        }
    }

    //Getting the scan results

    //qr코드 승인 , qr코드 없을시
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {

           //qrcode 가 없으면
           /*String address = "www.naver.com"; //obj.getString("address");// 주소 받아오기
            Intent intent_1 = new Intent(MainActivity.this, pWebView.class);
            intent_1.putExtra("webview_addr",address);
            startActivity(intent_1);
            */
            if (result.getContents() == null) {
                Log.d(TAG,"테스트0");
                String address = "http://emoclew.pythonanywhere.com/"; //obj.getString("address");// 주소 받아오기
                Intent intent_1 = new Intent(MainActivity.this, pWebView.class);
                intent_1.putExtra("webview_addr",address);
                startActivity(intent_1);
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

            } else {
                //qrcode 결과가 있으면
                Log.d(TAG,"테스트1");
                Toast.makeText(MainActivity.this, "Scan Perfect", Toast.LENGTH_SHORT).show();
                try {
                    //data를 json으로 변환
                    Log.d(TAG,"테스트2");
                    JSONObject obj = new JSONObject(result.getContents());
                    textViewName.setText(obj.getString("name"));
                    textViewAddress.setText(obj.getString("address"));
                    String address_1 = "m.naver.com"; //obj.getString("address");// 주소 받아오기
                    Intent intent = new Intent(MainActivity.this, pWebView.class);
                    intent.putExtra("webview_addr",address_1);

                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                    textViewResult.setText(result.getContents());
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
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