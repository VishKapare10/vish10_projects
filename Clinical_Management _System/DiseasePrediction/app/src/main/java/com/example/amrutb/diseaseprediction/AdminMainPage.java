package com.example.amrutb.diseaseprediction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AdminMainPage extends AppCompatActivity {
    ListView listView;
    EditText name_mobile;
    Button search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main_page);
        listView= (ListView) findViewById(R.id.listView);
        name_mobile= (EditText) findViewById(R.id.enterNameMob);
        search= (Button) findViewById(R.id.search);

        String[] requests={"Pending Requests","Accepted Request","History","Rejected Requests"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(AdminMainPage.this,android.R.layout.simple_dropdown_item_1line,requests);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(AdminMainPage.this, ""+adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
                switch (i){
                    case(0):Intent intent=new Intent(AdminMainPage.this,PendingRequest.class);
                        startActivity(intent);
                        break;
                    case(1):Intent intent1=new Intent(AdminMainPage.this,AcceptedRejecteRequest.class);
                        intent1.putExtra("request","Accepted");
                        startActivity(intent1);
                        break;
                    case(2):Intent intent3=new Intent(AdminMainPage.this,History.class);
                        startActivity(intent3);
                        break;
                    case(3):Intent intent2=new Intent(AdminMainPage.this,AcceptedRejecteRequest.class);
                        intent2.putExtra("request","Rejected");
                        startActivity(intent2);
                        break;
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name_mobile.getText().toString().isEmpty()){
                    Toast.makeText(AdminMainPage.this, "Enter Patient Name", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(AdminMainPage.this,Search.class);
                    intent.putExtra("name",name_mobile.getText().toString().trim());
                    startActivity(intent);
                }

            }
        });


    }
}
