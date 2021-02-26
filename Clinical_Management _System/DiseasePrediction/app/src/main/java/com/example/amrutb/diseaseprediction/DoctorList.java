package com.example.amrutb.diseaseprediction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.amrutb.SharedPreferences.SharedPreferenceManager;
import com.example.amrutb.UserObject.UserObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class DoctorList extends AppCompatActivity {

    RecyclerView recList;
    ProgressDialog progressDialog;
    SharedPreferenceManager sharedPreferenceManager;
    //String url="http://192.168.0.130/disease_prediction/doctorsearch.php";
    String url="http://sourcecodetechnology.com/disease_prediction/doctorsearch.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        UserObject userObject=new UserObject();
        userObject.subArea=getIntent().getExtras().getString("subarea");
        userObject.choice=getIntent().getExtras().getString("others");

        createList(userObject);
    }
    private List<UserObject> createList(UserObject userObject) {
        final List<UserObject> list = new ArrayList<UserObject>();
        RequestParams params=new RequestParams();
        params.put("subarea",userObject.subArea);
        params.put("symptoms",userObject.choice);
        progressDialog=new ProgressDialog(DoctorList.this);
        progressDialog.setMessage("Fetching User Request...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        asyncHttpClient.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressDialog.dismiss();
                String result=new String(responseBody);
                System.out.println(result);
                try {
                    JSONObject object=new JSONObject(result);
                    if(object.getString("success").equals("200")){
                        JSONArray jsonArray=object.getJSONArray("result");
                        System.out.println(jsonArray);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject object1=jsonArray.getJSONObject(i);
                            UserObject ci = new UserObject();
                            ci.username = object1.getString("name");
                            ci.hospitalName=object1.getString("hospitalname");
                            ci.address= object1.getString("address");
                            ci.specialist=object1.getString("speciality");
                            ci.choice=object1.getString("others");
                            ci.mobile=object1.getString("mobileno");
                            ci.area=object1.getString("area");
                            ci.subArea=object1.getString("subarea");
                            ci.rating= Float.parseFloat(object1.getString("rating"));
                            list.add(ci);
                        }
                        ContactAdapter ca = new ContactAdapter(list);
                        recList.setAdapter(ca);


                    }else{
                        Toast.makeText(DoctorList.this,object.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DoctorList.this,"JSON ERROR", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(DoctorList.this,"Connection ERROR", Toast.LENGTH_SHORT).show();
            }
        });
        return list;
    }

}
