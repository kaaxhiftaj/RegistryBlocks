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

import java.util.HashMap;
import java.util.Map;


public class EnterCodeFragment extends Fragment {

    EditText etCode;
    Button btnSend;
    String strCode;
    android.support.v7.app.AlertDialog alertDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_enter_code, container, false);

        etCode=(EditText)view.findViewById(R.id.etCode);
        btnSend=(Button)view.findViewById(R.id.btnSendCode);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCode=etCode.getText().toString();
                if (strCode.equals(""))
                {
                    etCode.setError("Required");
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
        });
        return view;
    }

    private void apicall() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://rogervaneijk.com/registeryblocks/rest/verifycode", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                Log.d("zmaReg",response);
                Bundle bundle=new Bundle();
                bundle.putString("code",strCode);
                Fragment fragment=new ResetPassFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.mainContainer,fragment).commit();

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
                params.put("email",strCode);
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
