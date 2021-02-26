package com.example.amrutb.diseaseprediction;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.amrutb.SharedPreferences.SharedPreferenceManager;
import com.example.amrutb.UserObject.UserObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class SetTimeDate extends AppCompatActivity {

    TextView setDate,setTime;
    Button send;
    //String url="http://192.168.0.130/disease_prediction/user_request.php";
    String url="http://sourcecodetechnology.com/disease_prediction/user_request.php";
    ProgressDialog progressDialog;
    SharedPreferenceManager sharedPreferenceManager;
    Calendar myCalendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_time_date);
        setDate = (TextView) findViewById(R.id.date);
        setTime = (TextView) findViewById(R.id.time);
        send = (Button) findViewById(R.id.send);

        final String doctorMobile=getIntent().getExtras().getString("doctorMobileNumber");

        sharedPreferenceManager=new SharedPreferenceManager(SetTimeDate.this);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        setDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SetTimeDate.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        setTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SetTimeDate.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        setTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setTime.getText().toString().equals("click here to set time")||setDate.getText().toString().equals("click here to set date")){
                    Toast.makeText(SetTimeDate.this, "Select Valid Date/Time", Toast.LENGTH_SHORT).show();
                }else{
                    UserObject userObject=new UserObject();
                    sharedPreferenceManager.connectDB();
                    userObject.username=sharedPreferenceManager.getString("name");
                    userObject.mobile=sharedPreferenceManager.getString("mobile");
                    userObject.choice=sharedPreferenceManager.getString("symtoms");
                    userObject.appointmentDate=setDate.getText().toString().trim();
                    userObject.appointmentTime=setTime.getText().toString().trim();
                    userObject.adminMobile=doctorMobile;
                    userRequest(userObject);
                }
            }
        });

    }

    private void userRequest(UserObject userObject) {
        RequestParams params=new RequestParams();
        params.put("username",userObject.username);
        params.put("usermobile",userObject.mobile);
        params.put("adminmobile",userObject.adminMobile);
        params.put("date",userObject.appointmentDate);
        params.put("time",userObject.appointmentTime);
        params.put("symptoms",userObject.choice);
        progressDialog=new ProgressDialog(SetTimeDate.this);
        progressDialog.setMessage("Sending request...");
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
                        Toast.makeText(SetTimeDate.this, "Request sent", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SetTimeDate.this, "JSON Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressDialog.dismiss();
                Toast.makeText(SetTimeDate.this, "Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLabel() {

            String myFormat = "yy/MM/dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        setDate.setPaintFlags(setDate.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            setDate.setText(sdf.format(myCalendar.getTime()));
        }



    }
