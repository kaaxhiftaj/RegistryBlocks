package com.techease.registryblocks.Activities.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.techease.registryblocks.Activities.Adapters.AllProductsAdapter;
import com.techease.registryblocks.Activities.Controller.AllProductsModel;
import com.techease.registryblocks.Activities.Utils.AlertsUtils;
import com.techease.registryblocks.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ProductDetails extends Fragment {

    ImageView ivImage;
    TextView tvTitle,tvModel,tvSerial,tvRegistration,tvLast_taransfered,insurance,tvLabel10,tvLabel1,tvLabel2,
    tvLabel3,tvLabel4,tvLabel5,tvLabel6,tvLabel7,tvLabel8,tvLabel9;
    String Id,strLabel10,strLabel1,strLabel2,strLabel3,strLabel4,strLabel5,strLabel6,strLabel7,strLabel8,strLabel9;
    android.support.v7.app.AlertDialog alertDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        setCustomActionBar();
        getActivity().setTitle("Item Specificatoin");
        ivImage=(ImageView)view.findViewById(R.id.ivImageProductDetail);
        tvTitle=(TextView)view.findViewById(R.id.tvProductName);
        tvModel=(TextView)view.findViewById(R.id.tvModelNo);
        tvSerial=(TextView)view.findViewById(R.id.tvSerialNo);
        tvLabel1=(TextView)view.findViewById(R.id.tvLabel1);
        tvLabel2=(TextView)view.findViewById(R.id.tvLabel2);
        tvLabel3=(TextView)view.findViewById(R.id.tvLabel3);
        tvLabel4=(TextView)view.findViewById(R.id.tvLabel4);
        tvLabel5=(TextView)view.findViewById(R.id.tvLabel5);
        tvLabel6=(TextView)view.findViewById(R.id.tvLabel6);
        tvLabel7=(TextView)view.findViewById(R.id.tvLabel7);
        tvLabel8=(TextView)view.findViewById(R.id.tvLabel8);
        tvLabel9=(TextView)view.findViewById(R.id.tvLabel9);
        tvLabel10=(TextView)view.findViewById(R.id.tvLabel10);
        tvRegistration=(TextView)view.findViewById(R.id.tvRegDate);
        tvLast_taransfered=(TextView)view.findViewById(R.id.tvLastTrans);
        insurance=(TextView)view.findViewById(R.id.tvInsurance);

        if (alertDialog==null)
        {
            alertDialog= AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();
        }
        apiCall();

        if (strLabel1!=null)
        {
            tvLabel1.setText(strLabel1);
        }
        else
            if (strLabel2!=null)
            {
                tvLabel2.setText(strLabel2);
            }
            else
            if (strLabel3!=null)
            {
                tvLabel3.setText(strLabel3);
            }
            else
            if (strLabel4!=null)
            {
                tvLabel4.setText(strLabel4);
            }
            else
            if (strLabel5!=null)
            {
                tvLabel5.setText(strLabel5);
            }
            else
            if (strLabel6!=null)
            {
                tvLabel6.setText(strLabel6);
            }
            else
            if (strLabel7!=null)
            {
                tvLabel7.setText(strLabel7);
            }
            else
            if (strLabel8!=null)
            {
                tvLabel8.setText(strLabel8);
            }
            else
            if (strLabel9!=null)
            {
                tvLabel9.setText(strLabel9);
            }
            else
            if (strLabel10!=null)
            {
                tvLabel2.setText(strLabel10);
            }
        Id=getArguments().getString("id");

        return view;
    }

    private void apiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://registryblocks.com/app/rest/productDetails", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                Log.d("zmaALlProducts",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject object=jsonObject.getJSONObject("data");
                    Glide.with(getActivity()).load(object.getString("image")).into(ivImage);
                    tvTitle.setText(object.getString("category"));
                    tvModel.setText(object.getString("model"));
                    tvSerial.setText(object.getString("serial"));
                    JSONObject getJsonObject=object.getJSONObject("data");
                    Iterator iterator = getJsonObject.keys();
                    while(iterator.hasNext()){
                        String key = (String)iterator.next();
                        JSONObject issue = getJsonObject.getJSONObject(key);

                        //  get id from  issue
                        String _pubKey = issue.optString("label1");

                        Toast.makeText(getActivity(), _pubKey, Toast.LENGTH_SHORT).show();
                    }
                    strLabel1=(object.getString("label1"));
                    strLabel2=(object.getString("label2"));
                    strLabel3=(object.getString("label3"));
                    strLabel4=(object.getString("label4"));
                    strLabel5=(object.getString("label5"));
                    strLabel6=(object.getString("label6"));
                    strLabel7=(object.getString("label7"));
                    strLabel8=(object.getString("label8"));
                    strLabel9=(object.getString("label9"));
                    strLabel10=(object.getString("label10"));


                    JSONObject objectHistory=object.getJSONObject("history");
                    tvLast_taransfered.setText(objectHistory.getString("last_transferd"));
                    tvRegistration.setText(objectHistory.getString("registration"));
                    insurance.setText(objectHistory.getString("insurance"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                Log.d("zma error", String.valueOf(error.getCause()));
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",Id);
                return params;
            }
        };

        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    private void setCustomActionBar() {

        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);

        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.itemspec_actionbar, null);
        LinearLayout linearLayout=(LinearLayout)mCustomView.findViewById(R.id.llBack);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment=new MyItems();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            }
        });
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }
}
