package com.techease.registryblocks.Activities.Fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techease.registryblocks.Activities.Utils.AlertsUtils;
import com.techease.registryblocks.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class ResetPassFragment extends Fragment {

    EditText etEmail,etCEmail;
    Button btnReset;
    String strEmail,strConfirm,code;
    android.support.v7.app.AlertDialog alertDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_reset_pass, container, false);

        code=getArguments().getString("code");
        etEmail=(EditText)view.findViewById(R.id.etNewPass);
        etCEmail=(EditText)view.findViewById(R.id.etConfirm);
        btnReset=(Button)view.findViewById(R.id.btnReset);
        
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
        return view;
    }

    private void check() {
        strEmail=etEmail.getText().toString();
        strConfirm=etCEmail.getText().toString();
        if (strEmail.equals(""))
        {
            etEmail.setError("Required");
        }
        else 
            if (strConfirm.equals(" ") && !strEmail.equals(strConfirm))
            {
                etCEmail.setError("Password does not match");
            }
            else 
            {
                if (alertDialog==null)
                {
                    alertDialog= AlertsUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                }
                apicall();
            }
    }

    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rogervaneijk.com/registeryblocks/rest/resetpassword", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                Log.d("zmaReg",response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String abc=jsonObject.getString("message");
                    AlertsUtils.showErrorDialog(getActivity(),abc);
                    Fragment fragment=new LoginFragment();
                    getFragmentManager().beginTransaction().replace(R.id.mainContainer,fragment).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                AlertsUtils.showErrorDialog(getActivity(),error.getMessage().toString());
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
                params.put("password",strConfirm);
                params.put("code",code);
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
}
