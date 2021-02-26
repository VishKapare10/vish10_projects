package com.example.amrutb.diseaseprediction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amrutb.SharedPreferences.SharedPreferenceManager;
import com.example.amrutb.UserObject.UserObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AdminLogin extends AppCompatActivity {

    EditText username,password;
    Button login,register;
    ProgressDialog progressDialog;
    //String url="http://192.168.0.130/disease_prediction/adminlogin.php";
    String url="http://sourcecodetechnology.com/disease_prediction/adminlogin.php";
    SharedPreferenceManager sharedPreferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        username= (EditText) findViewById(R.id.editTextAdminUser);
        password= (EditText) findViewById(R.id.editTextAdminPass);
        login= (Button) findViewById(R.id.buttonAdminLogin);
        register= (Button) findViewById(R.id.buttonAdminRegister);
        sharedPreferenceManager=new SharedPreferenceManager(AdminLogin.this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminLogin.this,AdminRegistration.class);
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
                    Toast.makeText(AdminLogin.this,"Vacant Fields",Toast.LENGTH_SHORT).show();
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
        progressDialog=new ProgressDialog(AdminLogin.this);
        progressDialog.setMessage("Verifying Details...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        asyncHttpClient.get(url, params, new AsyncHttpResponseHandler() {
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
                        sharedPreferenceManager.setString("hospitalname",object.getString("hospitalname"));
                        sharedPreferenceManager.closeDB();
                        Intent intent=new Intent(AdminLogin.this,AdminMainPage.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(AdminLogin.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AdminLogin.this,"JSON Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(AdminLogin.this,"Error Occurred",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
