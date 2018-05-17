package com.techease.registryblocks.Activities.Fragments;

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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.techease.registryblocks.Activities.Utils.AlertsUtils;
import com.techease.registryblocks.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ProductDetailsFragment extends Fragment {

    ImageView ivImage;
    TextView tvTitle,tvModel,tvSerial,tvRegistration,tvLast_taransfered,insurance,tvLabel10,tvLabel1,tvLabel2,
    tvLabel3,tvLabel4,tvLabel5,tvLabel6,tvLabel7,tvLabel8,tvLabel9,tvLabel1Title,tvLabel2Title,tvLabel3Title,
            tvLabel4Title,tvLabel5Title,tvLabel6Title,tvLabel7Title,tvLabel8Title,tvLabel9Title,tvLabel10Title;
    String Id,strLabel10,strLabel1,strLabel2,strLabel3,strLabel4,strLabel5,strLabel6,strLabel7,strLabel8,strLabel9,
    strLabel1Title,strLabel2Title,strLabel3Title,strLabel4Title,strLabel5Title,strLabel6Title,strLabel7Title,
            strLabel8Title,strLabel9Title,strLabel10Title;
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
        tvLabel1Title=(TextView)view.findViewById(R.id.tvLabel1Title);
        tvLabel2Title=(TextView)view.findViewById(R.id.tvLabel2Title);
        tvLabel3Title=(TextView)view.findViewById(R.id.tvLabel3Title);
        tvLabel4Title=(TextView)view.findViewById(R.id.tvLabel4Title);
        tvLabel5Title=(TextView)view.findViewById(R.id.tvLabel5Title);
        tvLabel6Title=(TextView)view.findViewById(R.id.tvLabel6Title);
        tvLabel7Title=(TextView)view.findViewById(R.id.tvLabel7Title);
        tvLabel8Title=(TextView)view.findViewById(R.id.tvLabel8Title);
        tvLabel9Title=(TextView)view.findViewById(R.id.tvLabel9Title);
        tvLabel10Title=(TextView)view.findViewById(R.id.tvLabel10Title);
        tvRegistration=(TextView)view.findViewById(R.id.tvRegDate);
        tvLast_taransfered=(TextView)view.findViewById(R.id.tvLastTrans);
        insurance=(TextView)view.findViewById(R.id.tvInsurance);

        if (alertDialog==null)
        {
            alertDialog= AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();
        }
        apiCall();


        Id=getArguments().getString("id");

        return view;
    }

    private void apiCall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rogervaneijk.com/registeryblocks/rest/productDetails", new Response.Listener<String>() {
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
                    JSONObject label1JSONObj=object.getJSONObject("label1");
                    Iterator<String> iterator = label1JSONObj.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        Log.i("TAG","key:"+key +"--Value::"+label1JSONObj.optString(key));
                        strLabel1Title=key;
                        strLabel1=label1JSONObj.optString(key);
                    }
                    JSONObject label2JSONObj=object.getJSONObject("label2");
                    Iterator<String> iterator2 = label2JSONObj.keys();
                    while (iterator2.hasNext()) {
                        String key = iterator2.next();
                        strLabel2Title=key;
                        strLabel2=label2JSONObj.optString(key);
                    }
                    JSONObject label3JSONObj=object.getJSONObject("label3");
                    Iterator<String> iterator3 = label3JSONObj.keys();
                    while (iterator3.hasNext()) {
                        String key = iterator3.next();
                        strLabel3Title=key;
                        strLabel3=label3JSONObj.optString(key);
                    }
                    JSONObject label4JSONObj=object.getJSONObject("label4");
                    Iterator<String> iterator4 = label4JSONObj.keys();
                    while (iterator4.hasNext()) {
                        String key = iterator4.next();
                        strLabel4Title=key;
                        strLabel4=label4JSONObj.optString(key);
                    }
                    JSONObject label5JSONObj=object.getJSONObject("label5");
                    Iterator<String> iterato5 = label5JSONObj.keys();
                    while (iterato5.hasNext()) {
                        String key = iterato5.next();
                        strLabel5Title=key;
                        strLabel5=label5JSONObj.optString(key);
                    }
                    JSONObject label6JSONObj=object.getJSONObject("label5");
                    Iterator<String> iterato6 = label6JSONObj.keys();
                    while (iterato6.hasNext()) {
                        String key = iterato6.next();
                        strLabel5Title=key;
                        strLabel5=label6JSONObj.optString(key);
                    }
                    JSONObject label7JSONObj=object.getJSONObject("label5");
                    Iterator<String> iterato7 = label7JSONObj.keys();
                    while (iterato7.hasNext()) {
                        String key = iterato7.next();
                        strLabel5Title=key;
                        strLabel5=label7JSONObj.optString(key);
                    }
                    JSONObject label8JSONObj=object.getJSONObject("label5");
                    Iterator<String> iterato8 = label8JSONObj.keys();
                    while (iterato8.hasNext()) {
                        String key = iterato8.next();
                        strLabel5Title=key;
                        strLabel5=label8JSONObj.optString(key);
                    }
                    JSONObject label9JSONObj=object.getJSONObject("label5");
                    Iterator<String> iterato9 = label9JSONObj.keys();
                    while (iterato9.hasNext()) {
                        String key = iterato9.next();
                        strLabel5Title=key;
                        strLabel5=label9JSONObj.optString(key);
                    }
                    JSONObject label10JSONObj=object.getJSONObject("label5");
                    Iterator<String> iterato10 = label10JSONObj.keys();
                    while (iterato10.hasNext()) {
                        String key = iterato10.next();
                        strLabel5Title=key;
                        strLabel5=label10JSONObj.optString(key);
                    }
                    if ( strLabel1!=null)
                    {
                        tvLabel1.setVisibility(View.VISIBLE);
                        tvLabel1Title.setVisibility(View.VISIBLE);
                        tvLabel1Title.setText(strLabel1Title);
                        tvLabel1.setText(strLabel1);
                    }
                    if ( strLabel2!=null)
                    {
                        tvLabel2.setVisibility(View.VISIBLE);
                        tvLabel2Title.setVisibility(View.VISIBLE);
                        tvLabel2Title.setText(strLabel2Title);
                        tvLabel2.setText(strLabel2);
                    }
                    if ( strLabel3!=null)
                    {
                        tvLabel3.setVisibility(View.VISIBLE);
                        tvLabel3Title.setVisibility(View.VISIBLE);
                        tvLabel3Title.setText(strLabel3Title);
                        tvLabel3.setText(strLabel3);
                    }
                    if ( strLabel4!=null)
                    {
                        tvLabel4.setVisibility(View.VISIBLE);
                        tvLabel4Title.setVisibility(View.VISIBLE);
                        tvLabel4Title.setText(strLabel4Title);
                        tvLabel4.setText(strLabel4);
                    }
                    if (strLabel5!=null)
                    {
                        tvLabel5.setVisibility(View.VISIBLE);
                        tvLabel5Title.setVisibility(View.VISIBLE);
                        tvLabel5Title.setText(strLabel5Title);
                        tvLabel5.setText(strLabel5);
                    }
                    if (strLabel6!=null)
                    {
                        tvLabel6.setVisibility(View.VISIBLE);
                        tvLabel6Title.setVisibility(View.VISIBLE);
                        tvLabel6Title.setText(strLabel6Title);
                        tvLabel6.setText(strLabel6);
                    }
                    if ( strLabel7!=null)
                    {
                        tvLabel7.setVisibility(View.VISIBLE);
                        tvLabel7Title.setVisibility(View.VISIBLE);
                        tvLabel7Title.setText(strLabel7Title);
                        tvLabel7.setText(strLabel7);
                    }
                    if ( strLabel8!=null)
                    {
                        tvLabel8.setVisibility(View.VISIBLE);
                        tvLabel8Title.setVisibility(View.VISIBLE);
                        tvLabel8Title.setText(strLabel8Title);
                        tvLabel8.setText(strLabel8);
                    }
                    if (strLabel9!=null)
                    {
                        tvLabel9.setVisibility(View.VISIBLE);
                        tvLabel9Title.setVisibility(View.VISIBLE);
                        tvLabel9Title.setText(strLabel9Title);
                        tvLabel9.setText(strLabel9);
                    }
                    if ( strLabel10!=null)
                    {
                        tvLabel10.setVisibility(View.VISIBLE);
                        tvLabel10.setVisibility(View.VISIBLE);
                        tvLabel10Title.setText(strLabel10Title);
                        tvLabel10.setText(strLabel10);
                    }

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
                Fragment fragment=new MyItemsFragment();
                getFragmentManager().beginTransaction().replace(R.id.container,fragment).commit();
            }
        });
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }
}
