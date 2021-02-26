package com.example.amrutb.diseaseprediction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amrutb.SharedPreferences.SharedPreferenceManager;
import com.example.amrutb.UserObject.UserObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Welcome extends AppCompatActivity {

    EditText username,password;
    Button login,register;
    TextView admin;
    ProgressDialog progressDialog;
    //String url="http://192.168.0.130/disease_prediction/login.php";
    String url="http://sourcecodetechnology.com/disease_prediction/login.php";
    SharedPreferenceManager sharedPreferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        username= (EditText) findViewById(R.id.editTextLogUser);
        password= (EditText) findViewById(R.id.editTextLogPass);
        login= (Button) findViewById(R.id.buttonLogin);
        register= (Button) findViewById(R.id.buttonRegister);
        sharedPreferenceManager=new SharedPreferenceManager(Welcome.this);
        admin= (TextView) findViewById(R.id.textViewAdmin);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Welcome.this,Register.class);
                startActivity(intent);
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Welcome.this,AdminLogin.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserObject userObject=new UserObject();
                userObject.username=username.getText().toString().trim();
                userObject.password=password.getText().toString().trim();
                if(userObject.username.isEmpty()||userObject.password.isEmpty()){
                    Toast.makeText(Welcome.this,"Vacant Fields",Toast.LENGTH_SHORT).show();
                }else{
                    login(userObject);
                }
            }
        });
    }

    private void login(UserObject userObject) {
        RequestParams params=new RequestParams();
        params.put("name",userObject.username);
        params.put("password",userObject.password);
        progressDialog=new ProgressDialog(Welcome.this);
        progressDialog.setMessage("Verifying Details...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        AsyncHttpClient client=new AsyncHttpClient();
        client.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressDialog.dismiss();
                String result=new String(responseBody);
                try {
                    JSONObject object=new JSONObject(result);
                    if(object.getString("success").equals("200")){
                        sharedPreferenceManager.connectDB();
                        sharedPreferenceManager.setString("name",object.getString("name"));
                        sharedPreferenceManager.setString("id",object.getString("id"));
                        sharedPreferenceManager.setString("mobile",object.getString("mobile"));
                        sharedPreferenceManager.setString("mail",object.getString("mail"));
                        sharedPreferenceManager.closeDB();
                        Intent intent=new Intent(Welcome.this,UserMainPage.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(Welcome.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Welcome.this,"JSON Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(Welcome.this,"Error Occurred",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
