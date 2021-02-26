package com.example.amrutb.diseaseprediction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amrutb.SharedPreferences.SharedPreferenceManager;
import com.example.amrutb.UserObject.UserObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Amrut B on 12/23/2016.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

private List<UserObject> contactList;

public ContactAdapter(List<UserObject> contactList) {
        this.contactList = contactList;
        }

@Override
public int getItemCount() {
        return contactList.size();
        }

@Override
public void onBindViewHolder(final ContactViewHolder contactViewHolder, int i) {
        final UserObject ci = contactList.get(i);
        contactViewHolder.hospitalName.setText(ci.hospitalName);
        contactViewHolder.doctorName.setText(ci.username);
        contactViewHolder.speciality.setText(ci.specialist);
        contactViewHolder.others.setText(ci.choice);
        contactViewHolder.mobile.setText(ci.mobile);
        contactViewHolder.address.setText(ci.address);
        contactViewHolder.subarea.setText(ci.subArea);
        contactViewHolder.area.setText(ci.area);
        contactViewHolder.rating.setRating(ci.rating);

    contactViewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent=new Intent(view.getContext(),SetTimeDate.class);
                intent.putExtra("doctorMobileNumber",ci.mobile);
                view.getContext().startActivity(intent);
            }
        });

        }

@Override
public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
        from(viewGroup.getContext()).
        inflate(R.layout.card_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
        }

public static class ContactViewHolder extends RecyclerView.ViewHolder {

    protected TextView hospitalName;
    protected TextView doctorName;
    protected TextView speciality;
    protected TextView others;
    protected TextView mobile;
    protected TextView address;
    protected TextView subarea;
    protected TextView area;
    protected RatingBar rating;
    protected Button accept;

    public ContactViewHolder(View v) {
        super(v);
        hospitalName =  (TextView) v.findViewById(R.id.hospitalName);
        doctorName = (TextView)  v.findViewById(R.id.doctorName);
        speciality = (TextView)  v.findViewById(R.id.txtSpeciality);
        others = (TextView)  v.findViewById(R.id.txtOthers);
        mobile = (TextView)  v.findViewById(R.id.txtMobile);
        address= (TextView) v.findViewById(R.id.txtAddress);
        subarea= (TextView) v.findViewById(R.id.txtSubArea);
        area= (TextView) v.findViewById(R.id.txtArea);
        rating= (RatingBar) v.findViewById(R.id.ratingBar1);
        accept= (Button) v.findViewById(R.id.buttonAccept);
    }
}
}
