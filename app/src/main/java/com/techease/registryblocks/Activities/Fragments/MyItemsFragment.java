package com.techease.registryblocks.Activities.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techease.registryblocks.R;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.techease.registryblocks.Activities.Activities.BottomNavigationActivity;
import com.techease.registryblocks.Activities.Activities.FullScreenActivity;
import com.techease.registryblocks.Activities.Activities.ScannerActivity;
import com.techease.registryblocks.Activities.Adapters.AllProductsAdapter;
import com.techease.registryblocks.Activities.Controller.AllProductsModel;
import com.techease.registryblocks.Activities.Utils.AlertsUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyItemsFragment extends Fragment {

    private EditText searchView;
    GridView gridView;
    ArrayList<AllProductsModel> allProductsModelArrayList;
    AllProductsAdapter allProductsAdapter;
    RequestQueue requestQueue;
    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_my_items, container, false);

        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setCustomActionBar();
        userId=sharedPreferences.getString("userId","");
        gridView=(GridView) view.findViewById(R.id.gridViewMyItems);
        searchView=(EditText) view.findViewById(R.id.sv);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        searchEducationList();

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


    public void searchEducationList() {


        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence query, int i, int i1, int i2) {
                Log.d("LOG_TAG", getClass().getSimpleName() + " text changed " + searchView.getText());

                query = query.toString().toLowerCase();
                ArrayList<AllProductsModel> newData = new ArrayList<>();
                for (int j = 0; j < allProductsModelArrayList.size(); j++) {
                    final String test2 = allProductsModelArrayList.get(j).getProductName().toLowerCase();
                    if (test2.startsWith(String.valueOf(query))) {
                        newData.add(allProductsModelArrayList.get(j));
                    }
                }
                allProductsAdapter = new AllProductsAdapter(getActivity(), newData);
                gridView.setAdapter(allProductsAdapter);
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }
    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rogervaneijk.com/registeryblocks/rest/allproducts", new Response.Listener<String>() {
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
                        model.setProductName(object.getString("category"));
                        model.setProductModel(object.getString("model"));
                        model.setProductImage(object.getString("image"));
                        model.setProductDate(object.getString("date"));
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
                params.put("userid",userId);
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
            ImageView imageView=(ImageView)mCustomView.findViewById(R.id.ivAdd);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   startActivity(new Intent(getActivity(), ScannerActivity.class));
                }
            });
            ImageView ivProfile=(ImageView)mCustomView.findViewById(R.id.ivProfile);
            ivProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editor.putString("token","").commit();
                    startActivity(new Intent(getActivity(), FullScreenActivity.class));
                    getActivity().finish();
                }
            });
            mActionBar.setCustomView(mCustomView);
            mActionBar.setDisplayShowCustomEnabled(true);
    }
}
