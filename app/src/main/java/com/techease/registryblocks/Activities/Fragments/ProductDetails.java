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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.adamnoor.registryblocks.R;
import com.techease.registryblocks.Activities.Adapters.AllProductsAdapter;
import com.techease.registryblocks.Activities.Controller.AllProductsModel;
import com.techease.registryblocks.Activities.Utils.AlertsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ProductDetails extends Fragment {

    ImageView ivImage;
    TextView tvTitle,tvModel,tvSerial,tvColor,tvBrand,tvWatts,tvBattery,tvSize,tvRegistration,tvLast_taransfered,insurance;
    String Id;
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
        tvColor=(TextView)view.findViewById(R.id.tvColor);
        tvBrand=(TextView)view.findViewById(R.id.tvProductBrand);
        tvWatts=(TextView)view.findViewById(R.id.tvWatts);
        tvBattery=(TextView)view.findViewById(R.id.tvBattery);
        tvSize=(TextView)view.findViewById(R.id.tvSize);
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
                    tvTitle.setText(object.getString("title"));
                    tvBattery.setText(object.getString("battery"));
                    tvBrand.setText(object.getString("brand"));
                    tvColor.setText(object.getString("color"));
                    tvModel.setText(object.getString("model"));
                    tvSize.setText(object.getString("size"));
                    tvSerial.setText(object.getString("serial"));
                    tvWatts.setText(object.getString("watts"));
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
