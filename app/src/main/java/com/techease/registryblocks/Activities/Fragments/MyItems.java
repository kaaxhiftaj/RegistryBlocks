package com.techease.registryblocks.Activities.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.adamnoor.registryblocks.R;
import com.techease.registryblocks.Activities.Activities.BottomNavigationActivity;
import com.techease.registryblocks.Activities.Adapters.AllProductsAdapter;
import com.techease.registryblocks.Activities.Controller.AllProductsModel;
import com.techease.registryblocks.Activities.Utils.AlertsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyItems extends Fragment {

    GridView gridView;
    ArrayList<AllProductsModel> allProductsModelArrayList;
    AllProductsAdapter allProductsAdapter;
    RequestQueue requestQueue;
    android.support.v7.app.AlertDialog alertDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_items, container, false);

        setCustomActionBar();
        gridView=(GridView) view.findViewById(R.id.gridViewMyItems);
        requestQueue = Volley.newRequestQueue(getActivity());
        allProductsModelArrayList=new ArrayList<>();
        if (alertDialog==null)
        {
            alertDialog= AlertsUtils.createProgressDialog(getActivity());
            alertDialog.show();
        }
        apicall();

        return view;
    }

    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://registryblocks.com/app/rest/allproducts", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                Log.d("zmaALlProducts",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject object=jsonArray.getJSONObject(i);
                        AllProductsModel model=new AllProductsModel();
                        model.setProductId(object.getString("product_id"));
                        model.setProductName(object.getString("title"));
                        model.setProductModel(object.getString("model"));
                        allProductsModelArrayList.add(model);

                    }
                    allProductsAdapter=new AllProductsAdapter(getActivity(),allProductsModelArrayList);
                    gridView.setAdapter(allProductsAdapter);

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
            LayoutInflater mInflater = LayoutInflater.from(getActivity());
            View mCustomView = mInflater.inflate(R.layout.myitems_actionbar, null);
            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
    }
}
