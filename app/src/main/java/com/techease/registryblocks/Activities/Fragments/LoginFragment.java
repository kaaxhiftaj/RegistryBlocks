package com.techease.registryblocks.Activities.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.techease.registryblocks.Activities.Activities.BottomNavigationActivity;
import com.techease.registryblocks.Activities.Utils.AlertsUtils;
import com.techease.registryblocks.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends Fragment implements View.OnClickListener {

    TextView tvNoAccount,tvForgot;
    EditText etEmail,etPass;
    Button btnLogin;
    String strEmail,strPass,strUserId,strMessage;
    android.support.v7.app.AlertDialog alertDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_login, container, false);

        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        tvNoAccount=(TextView)view.findViewById(R.id.tvNoAccount);
        tvForgot=(TextView)view.findViewById(R.id.tvForgot);
        etEmail=(EditText)view.findViewById(R.id.etEmailIn);
        etPass=(EditText)view.findViewById(R.id.etPassIn);
        btnLogin=(Button)view.findViewById(R.id.btnLogin);
        
        tvNoAccount.setOnClickListener(this);
        tvForgot.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id)
        {
            case R.id.tvForgot:
                Fragment fragment=new ForgotPassFragment();
                getFragmentManager().beginTransaction().replace(R.id.mainContainer,fragment).addToBackStack("LoginFragment").commit();
                break;
            case R.id.tvNoAccount:
                Fragment fragment2=new RegistrationFragment();
                getFragmentManager().beginTransaction().replace(R.id.mainContainer,fragment2).addToBackStack("LoginFragment").commit();
            case R.id.btnLogin:
                check();
        }
    }

    private void check() {
        strEmail=etEmail.getText().toString();
        strPass=etPass.getText().toString();
        if (strEmail.equals("")&& !strEmail.contains("@"))
        {
            etEmail.setError("Required");
        }
        else
        if (strPass.equals(""))
        {
            etPass.setError("Required");
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rogervaneijk.com/registeryblocks/rest/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("true"))
                {
                    if (alertDialog!=null)
                        alertDialog.dismiss();
                    Log.d("zmaReg",response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        JSONObject object=jsonObject.getJSONObject("user");
                        strMessage=jsonObject.getString("message");
                        strUserId=object.getString("id");
                        editor.putString("userId",strUserId).commit();
                        editor.putString("token","login").commit();
                        startActivity(new Intent(getActivity(), BottomNavigationActivity.class));
                        getActivity().finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {

                    if (alertDialog!=null)
                        alertDialog.dismiss();
                    try {
                        JSONObject object=new JSONObject(response);
                        String message=object.getString("message");
                        AlertsUtils.showErrorDialog(getActivity(),message);
                        etEmail.setText("");
                        etPass.setText("");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                params.put("email",strEmail);
                params.put("password",strPass);
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
