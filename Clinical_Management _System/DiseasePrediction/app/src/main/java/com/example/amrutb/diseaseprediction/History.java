package com.example.amrutb.diseaseprediction;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

import cz.msebera.android.httpclient.Header;

public class History extends AppCompatActivity {

    ListView listView;
    ProgressDialog progressDialog;
    TextView textView;
    SharedPreferenceManager sharedPreferenceManager;
    //String url="http://192.168.0.130/disease_prediction/history.php";
    String url="http://sourcecodetechnology.com/disease_prediction/history.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        listView= (ListView) findViewById(R.id.listView);
        textView= (TextView) findViewById(R.id.request);

        sharedPreferenceManager=new SharedPreferenceManager(History.this);
        sharedPreferenceManager.connectDB();
        String mobile=sharedPreferenceManager.getString("mobile");
        sharedPreferenceManager.closeDB();

        final ArrayList arrayList=new ArrayList();
        RequestParams params=new RequestParams();
        params.put("mobile",mobile);
        progressDialog=new ProgressDialog(History.this);
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
                            ci.mobile = object1.getString("mobile");
                            ci.appointmentDate=object1.getString("date");
                            ci.appointmentTime= object1.getString("time");
                            ci.choice=object1.getString("symptoms");
                            ci.id=object1.getString("id");
                            arrayList.add(ci.username+"\n"+"Mobile Number: "+ci.mobile+"\n"+"Symptoms: "+ci.choice+"\n"+"Date: "
                                    +ci.appointmentDate+"\n"+"Time: "+ci.appointmentTime+"\n"+"Request status: "+object1.getString("request_status"));
                        }
                        ArrayAdapter arrayAdapter=new ArrayAdapter(History.this,android.R.layout.simple_expandable_list_item_1,arrayList){
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent){
                                // Get the current item from ListView
                                View view = super.getView(position,convertView,parent);

                                // Get the Layout Parameters for ListView Current Item View
                                ViewGroup.LayoutParams params = view.getLayoutParams();

                                // Set the height of the Item View
                                params.height = view.getHeight();
                                view.setLayoutParams(params);

                                return view;
                            }
                        };
                        listView.setAdapter(arrayAdapter);
                    }else{
                        Toast.makeText(History.this,object.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(History.this,"JSON ERROR", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(History.this,"Connection ERROR", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}

