package com.example.amrutb.diseaseprediction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.amrutb.SharedPreferences.SharedPreferenceManager;
import com.example.amrutb.UserObject.UserObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UserMainPage extends AppCompatActivity {

    String subAreaName;
    Spinner subArea;
    ListView listView;
    Button submit,history,feedback;
    ProgressDialog progressDialog;
    SharedPreferenceManager sharedPreferenceManager;
    String[] listContent = {"Flu","Cold","Cough","Stomach Pain","Head ache", "Back Pain","Knee Pain","Anxiety","sleeping sickness"
    ,"joint pain"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_page);
        subArea= (Spinner) findViewById(R.id.spinnerArea);
        listView= (ListView) findViewById(R.id.listView);
        submit= (Button) findViewById(R.id.submit);
        history= (Button) findViewById(R.id.history);
        feedback= (Button) findViewById(R.id.feedback);

        String[] subareaname={"Select Place","LONAVALA","TALEGON","DEHUROAD","AKURDI","CHINCHWAD","PIMPRI","DAPODI","SHIVAJINAGAR","NIGDI","BHOSARI","CHAKAN","BANER","HINJEWADI"
                , "HADAPSAR","AAREY MILK COLONY","ANDHERI","ANUSHAKTI NAGAR","BANDRA","BANDRA KURLA COMPLEX","BORIVALI"};
        ArrayAdapter adapter1=new ArrayAdapter(UserMainPage.this,android.R.layout.simple_spinner_item,subareaname);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_item);
        subArea.setAdapter(adapter1);

        subArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subAreaName=adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter arrayAdapter=new ArrayAdapter(UserMainPage.this,android.R.layout.simple_list_item_multiple_choice,listContent);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setItemChecked(2, true);
        listView.setAdapter(arrayAdapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {                
                SparseBooleanArray choices = listView.getCheckedItemPositions();
                StringBuilder choicesString = new StringBuilder();
                for (int i = 0; i < choices.size(); i++)
                {
                    if(choices.valueAt(i) == true)
                        choicesString.append(listContent[choices.keyAt(i)]).append(",");
                }
                if(choicesString.toString().isEmpty()||subAreaName.equals("Select Place")){
                    Toast.makeText(UserMainPage.this, "Invalid selection", Toast.LENGTH_SHORT).show();
                }else{
                    UserObject userObject=new UserObject();
                    userObject.subArea=subAreaName;
                    StringBuilder stringBuilder=new StringBuilder();

                    sharedPreferenceManager=new SharedPreferenceManager(UserMainPage.this);
                    sharedPreferenceManager.connectDB();
                    sharedPreferenceManager.setString("symtoms",choicesString.toString());
                    sharedPreferenceManager.closeDB();

                    userObject.choice=stringBuilder.append(",").append(choicesString).toString();
                    userObject.choice=userObject.choice.replace(",","%");

                    Intent intent=new Intent(UserMainPage.this,DoctorList.class);
                    intent.putExtra("subarea",userObject.subArea);
                    intent.putExtra("others",userObject.choice);
                    startActivity(intent);
//                    searchDoctor(userObject);
//                    Toast.makeText(UserMainPage.this, "subarea"+subAreaName+" "+userObject.choice, Toast.LENGTH_SHORT).show();
                }
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserMainPage.this,UserHistory.class);
                startActivity(intent);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserMainPage.this,FeedBack.class);
                startActivity(intent);
            }
        });
    }
}
