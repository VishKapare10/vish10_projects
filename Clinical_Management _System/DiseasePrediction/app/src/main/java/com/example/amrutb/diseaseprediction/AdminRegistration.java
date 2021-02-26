package com.example.amrutb.diseaseprediction;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amrutb.Mail.GMailSender;
import com.example.amrutb.SharedPreferences.SharedPreferenceManager;
import com.example.amrutb.UserObject.UserObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.key;

public class AdminRegistration extends AppCompatActivity {
    EditText username,password,conPassword,mobile,hospitalName,address,specialist,mail;
    TextView others;
    Button submit;
    //String url="http://192.168.0.130/disease_prediction/adminregistration.php";
    String url="http://sourcecodetechnology.com/disease_prediction/adminregistration.php";
    ProgressDialog progressDialog;
    ListView listView;
    String areaName,subAreaName;
    Spinner area,subArea;
    StringBuilder choicesString;
    String[] listContent = {"Flu","Cold","Cough","Stomach Pain","Head ache", "Back Pain","Knee Pain","Anxiety","sleeping sickness"
            ,"joint pain"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration);
        username= (EditText) findViewById(R.id.editTextAdminUser);
        password= (EditText) findViewById(R.id.editTextAdminPassword);
        conPassword= (EditText) findViewById(R.id.editTextAdminConfirmPassword);
        mobile= (EditText) findViewById(R.id.editTextAdminMobileNo);
        hospitalName= (EditText) findViewById(R.id.editTextAdminShopName);
        address= (EditText) findViewById(R.id.editTextAdminAddress);
        specialist= (EditText) findViewById(R.id.editTextSpecialist);
        others= (TextView) findViewById(R.id.editTextOthers);
        submit= (Button) findViewById(R.id.buttonAdminSubmit);
        area= (Spinner) findViewById(R.id.spinnerArea);
        subArea= (Spinner) findViewById(R.id.spinnerSubArea);
        submit= (Button) findViewById(R.id.buttonAdminSubmit);
        mail= (EditText) findViewById(R.id.editTextAdminMailId);

        final String[] areaname={"Select Area","Pune","Mumbai"};
        ArrayAdapter adapter=new ArrayAdapter(AdminRegistration.this,android.R.layout.simple_spinner_item,areaname);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        area.setAdapter(adapter);

        String[] subareaname={"Select Sub-Area","LONAVALA","TALEGON","DEHUROAD","AKURDI","CHINCHWAD","PIMPRI","DAPODI","SHIVAJINAGAR","NIGDI","BHOSARI","CHAKAN","BANER","HINJEWADI"
                , "HADAPSAR","AAREY MILK COLONY","ANDHERI","ANUSHAKTI NAGAR","BANDRA","BANDRA KURLA COMPLEX","BORIVALI"};
        ArrayAdapter adapter1=new ArrayAdapter(AdminRegistration.this,android.R.layout.simple_spinner_item,subareaname);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        subArea.setAdapter(adapter1);

        area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                areaName=adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        subArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subAreaName=adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final UserObject userObject=new UserObject();
                userObject.username=username.getText().toString().trim();
                userObject.password=password.getText().toString().trim();
                userObject.mobile=mobile.getText().toString().trim();
                SharedPreferences prefs = getApplicationContext().getSharedPreferences("userdetails",0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("username",userObject.username);
                editor.putString("mobile",userObject.mobile);
                editor.commit();
                userObject.hospitalName=hospitalName.getText().toString().trim();
                userObject.address=address.getText().toString().trim();
                userObject.specialist=specialist.getText().toString();
                userObject.area=areaName;
                userObject.mail=mail.getText().toString().trim();
                userObject.subArea=subAreaName;
                if(userObject.username.isEmpty()||userObject.password.isEmpty()||userObject.mobile.isEmpty()||userObject.hospitalName.isEmpty()
                        || userObject.address.isEmpty()||conPassword.getText().toString().isEmpty()||userObject.area.isEmpty()
                        ||userObject.subArea.isEmpty()||userObject.specialist.isEmpty()||userObject.mail.isEmpty()){
                    Toast.makeText(AdminRegistration.this,"Vacant Fields",Toast.LENGTH_SHORT).show();
                }else if(userObject.area.equals("Select Area")|| userObject.subArea.equals("Select Sub-Area")){
                    Toast.makeText(AdminRegistration.this,"Select Valid Area/Sub-Area",Toast.LENGTH_SHORT).show();
                }else if(!(userObject.mobile.length()==10)){
                    Toast.makeText(AdminRegistration.this,"Invalid Mobile Number",Toast.LENGTH_SHORT).show();
                }else if(!(conPassword.getText().toString().trim().equals(userObject.password))){
                    Toast.makeText(AdminRegistration.this,"Password Not Matching",Toast.LENGTH_SHORT).show();
                }else if(others.getText().toString().isEmpty()){
                    userObject.choice=" ";
                    adminRegister(userObject);
                }else if(isValidEmail(userObject.mail)){
                    CreateAndSendOtp(userObject.mail);

                    AlertDialog.Builder builder=new AlertDialog.Builder(AdminRegistration.this);
                    builder.setTitle("OTP");
                    final EditText input = new EditText(AdminRegistration.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    builder.setView(input);
                    builder.setMessage("Enter OTP sent on your mail")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SharedPreferenceManager sharedPreferenceManager=new SharedPreferenceManager(AdminRegistration.this);
                                    sharedPreferenceManager.connectDB();
                                    String key=sharedPreferenceManager.getString("key");
                                    sharedPreferenceManager.closeDB();
                                    if(key.equals(input.getText().toString().trim())){
                                        userObject.choice=others.getText().toString();
                                        adminRegister(userObject);
                                    }else{
                                        Toast.makeText(AdminRegistration.this, "OTP not matching", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                    Toast.makeText(AdminRegistration.this,"Session Over",Toast.LENGTH_SHORT).show();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else {
                    Toast.makeText(AdminRegistration.this,"Something went wrong...",Toast.LENGTH_SHORT).show();
                }
            }
        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog=new Dialog(AdminRegistration.this);
                dialog.setContentView(R.layout.listview);
                dialog.setTitle("Select");
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                listView= (ListView) dialog.findViewById(R.id.listView);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listView.setItemChecked(2, true);
                Button button= (Button) dialog.findViewById(R.id.listViewSubmit);
                ArrayAdapter<String> adapter= new ArrayAdapter<String>(AdminRegistration.this,android.R.layout.simple_list_item_multiple_choice, listContent);
                listView.setAdapter(adapter);
                dialog.show();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        SparseBooleanArray choices = listView.getCheckedItemPositions();
                        StringBuilder choicesString = new StringBuilder();
                        for (int i = 0; i < choices.size(); i++)
                        {
                            if(choices.valueAt(i) == true)
                                choicesString.append(listContent[choices.keyAt(i)]).append(",");
                        }
                        Toast.makeText(AdminRegistration.this, choicesString, Toast.LENGTH_SHORT).show();
                        others.setText(choicesString);
                    }
                });
            }
        });
    }

    private void adminRegister(UserObject userObject) {
        RequestParams params=new RequestParams();
        params.put("name",userObject.username);
        params.put("password",userObject.password);
        params.put("mobile",userObject.mobile);
        params.put("hospitalname",userObject.hospitalName);
        params.put("address",userObject.address);
        params.put("specialist",userObject.specialist);
        params.put("others",userObject.choice);
        params.put("area",userObject.area);
        params.put("subarea",userObject.subArea);
        progressDialog=new ProgressDialog(AdminRegistration.this);
        progressDialog.setMessage("Registering...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
        AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
        asyncHttpClient.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressDialog.dismiss();
                String result=new String(responseBody);
                try {
                    JSONObject object=new JSONObject(result);
                    if(object.getString("success").equals("200")){
                        Toast.makeText(AdminRegistration.this,"Successfully Registered",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(AdminRegistration.this,AdminLogin.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(AdminRegistration.this,object.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AdminRegistration.this,"JSON Error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(AdminRegistration.this,"Connection Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void CreateAndSendOtp(final String mailId)
    {
//        final String key = UUID.randomUUID().toString();
        int randomPIN = (int)(Math.random()*9000)+1000;
        String key = ""+randomPIN;

        SharedPreferenceManager sharedPreferenceManager=new SharedPreferenceManager(AdminRegistration.this);
        sharedPreferenceManager.connectDB();
        sharedPreferenceManager.setString("key",key);
        sharedPreferenceManager.closeDB();
        sendEmail(key, mailId);
    }

    public void sendEmail(final String key, final String mailId)
    {
        new Thread(new Runnable(){
            @Override
            public void run() {
                try
                {
                    GMailSender sender = new GMailSender("srcdocs.ad@gmail.com", "adsrc190$");
                    sender.sendMail("Verification Key",
                            "Verification Key: " + key,
                            "srcdocs.ad@gmail.com",
                            mailId
                    );
                    System.out.println("Mail Sent" + mailId + " " + "Verification Key: " + key);
                }
                catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
        }).start();
    }
}
