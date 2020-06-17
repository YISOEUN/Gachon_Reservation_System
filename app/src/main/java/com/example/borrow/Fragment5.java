package com.example.borrow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//사용자 반납 신청 화면

public class Fragment5 extends Fragment {
    private boolean running;
    ViewGroup v;
    private TextView rentalT;
    private TextView returnT;
    Button button;
    public String itemValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = (ViewGroup) inflater.inflate(R.layout.fragment5,container,false);

        final ImageView background = (ImageView) v.findViewById(R.id.change_return_imageview);
        rentalT = (TextView) v.findViewById(R.id.txt1);
        returnT = (TextView) v.findViewById(R.id.txt2);
        button=(Button) v.findViewById(R.id.returnB);

        String[] items = {"반납하실 물품을 선택해주세요.","마우스", "노트북충전기", "우산", "휴대폰충전기"};
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //spinner의 아이템이 클릭되었을 때
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {
                itemValue = (String) adapterView.getItemAtPosition(position);
                if (position==0){
                    background.setBackgroundResource(R.drawable.return_box);
                }
                if(position!=0){//힌트로 띄워놓은 항목 제외
                    if(position==1)
                    {
                        background.setBackgroundResource(R.drawable.mouse);
                    }else if(position==2){
                        background.setBackgroundResource(R.drawable.notebookcharger);
                    }else if(position==3){
                        background.setBackgroundResource(R.drawable.umbrella1);
                    }else if(position==4){
                        background.setBackgroundResource(R.drawable.phonecharger);
                    }
                    Toast.makeText(getActivity(), itemValue+"(이)가 선택되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                }
            }
            @Override
            public void onNothingSelected(AdapterView adapterView){ //아무것도 선택되지 않았을 때
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://qoxodnjs.cafe24.com/ItemReturn.php";
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("userID", ((LoginActivity) LoginActivity.context_main).userID);
                parameters.put("itemName", itemValue);
                if (itemValue!="반납하실 물품을 선택해주세요."){
                    Toast.makeText(getActivity(), itemValue + " 반납신청이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), " 반납하실 물품을 선택해주세요.", Toast.LENGTH_SHORT).show();
                }

                //휴대폰충전기
                //우산
                //마우스
                //노트북충전기

                final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(parameters),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {   //밑에서 Request가 보내지고 결과로 온 Reponse가 JsonResponse를 통해 다뤄진다.
                                    JSONObject jsonResponse = response;
                                    //result = [{"userID":"bae","userStudentID":""}]
                                } catch (Exception e)//예외처리
                                {
                                    e.printStackTrace();//간단하게 예외처리함
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(((LoginActivity) MainActivity.context_main), "DB 연동 에러", Toast.LENGTH_LONG).show();
                            }
                        }) {

                };
                //실제로 로그인을 보낼 수 있는 Request : userID, password를 받아 리스너를 보냄
                RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
                queue.add(jsObjRequest);
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;
        Thread thread1 = new Fragment5.BackgroundThread();
        thread1.start();
    }
    @Override
    public void onPause() {
        super.onPause();
        running = false;
    }
    class BackgroundThread extends Thread {
        public void run(){
            while (running){
                try{
                    Thread.sleep(1000);

                    long now = System.currentTimeMillis();
                    Date date1 = new Date(now);
                    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy년 MM월 dd일 hh:mm:ss");
                    String getTime = simpleDate.format(date1);
                    rentalT.setText(getTime); // 현재 날짜, 시각을 사용자에게 보여줌
/*
                    Calendar cal = Calendar.getInstance();
                    Date date =simpleDate.parse(getTime);
                    cal.setTime(date);
                    cal.add(Calendar.DATE, 2); //대여가능 기간이 2일이라고 가정
                    Date date2=new Date(cal.getTimeInMillis());
                    String getTime2=simpleDate.format(date2);
                    returnT.setText(getTime2); // 반납 날짜, 시각
 */
                    returnT.setText("~대여했던 물품의 마감 일시");
                    ////////////////////////////////////////////////////////////////////////////////
                    //데이터베이스에서 대여한 물품의 마감 일시를 가져와서 띄워줘야함
                    ////////////////////////////////////////////////////////////////////////////////

                }catch(Exception ex){}
            }
        }
    }
}
