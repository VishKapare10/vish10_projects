package com.example.amrutb.diseaseprediction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.amrutb.UserObject.UserObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Register extends AppCompatActivity {

    EditText username,password,conPassword,mobile,mail;
    Button register;
    //String url="http://192.168.0.130/disease_prediction/registeration.php";
    String url="http://sourcecodetechnology.com/disease_prediction/registeration.php";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username= (EditText) findViewById(R.id.editTextRegUser);
        password= (EditText) findViewById(R.id.editTextRegPassword);
        conPassword= (EditText) findViewById(R.id.editTextRegConfirmPassword);
        mobile= (EditText) findViewById(R.id.editTextRegMobileNo);
        mail= (EditText) findViewById(R.id.editTextRegMailId);
        register= (Button) findViewById(R.id.buttonSubmit);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserObject userObject=new UserObject();
                userObject.username=username.getText().toString().trim();
                userObject.password=password.getText().toString().trim();
                userObject.mobile = mobile.getText().toString().trim();
                userObject.mail=mail.getText().toString().trim();
                if(userObject.username.isEmpty()||userObject.password.isEmpty()
                        || userObject.mobile.isEmpty() || userObject.mail.isEmpty() || conPassword.getText().toString().isEmpty()){
                    Toast.makeText(Register.this,"Vacant Fields",Toast.LENGTH_SHORT).show();
                }else if(!(userObject.mobile.length()==10)){
                    Toast.makeText(Register.this,"Enter Valid Number"+userObject.mobile.length(), Toast.LENGTH_SHORT).show();
                }else if(!(userObject.password.equals(conPassword.getText().toString()))){
                    Toast.makeText(Register.this,"Password Not Matching",Toast.LENGTH_SHORT).show();
                }else{
                    register(userObject);
                }
            }
        });
    }

    private void register(UserObject userObject) {
        RequestParams params=new RequestParams();
        params.put("name",userObject.username);
        params.put("password",userObject.password);
        params.put("mobile",userObject.mobile);
        params.put("mail",userObject.mail);
        progressDialog=new ProgressDialog(Register.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering...");
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
                        Toast.makeText(Register.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Register.this,Welcome.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(Register.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Register.this,"JSON Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
