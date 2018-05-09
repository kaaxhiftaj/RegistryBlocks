package com.techease.registryblocks.Activities.Activities;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.zxing.Result;
import com.techease.registryblocks.Activities.Fragments.ItemImages;
import com.techease.registryblocks.Activities.Fragments.MyItems;
import com.techease.registryblocks.Activities.Utils.AlertsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;
import static android.content.Intent.ACTION_VIEW;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final int Request_Camera=1;
    EditText etSerialNo,etModelNo;
    private ZXingScannerView scannerView;
    Button btnScan,btnRegister;
    String strSerialNo,strModelNo;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    android.support.v7.app.AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView=new ZXingScannerView(this);
        setContentView(R.layout.activity_scanner);

        sharedPreferences = ScannerActivity.this.getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        setCustomActionBar();
        btnScan=(Button)findViewById(R.id.btnScan);

        etModelNo=(EditText)findViewById(R.id.etModelNo);
        etSerialNo=(EditText)findViewById(R.id.etSerialNo);
        btnRegister=(Button)findViewById(R.id.btnRegister);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setContentView(scannerView);
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if (checkPermission())
                    {
                        //  Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        requestPermissions();
                    }
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });


    }

    private void check() {
        strSerialNo=etSerialNo.getText().toString();
        strModelNo=etModelNo.getText().toString();
        if (strSerialNo.equals(""))
        {
            etSerialNo.setError("Please fill the field");
        }
        else
            if (strModelNo.equals(""))
            {
                etModelNo.setError("Please fill the field");
            }
        else
        {
            if (alertDialog==null)
            {
                alertDialog= AlertsUtils.createProgressDialog(ScannerActivity.this);
                alertDialog.show();
            }
            apicall();
        }
    }

    private void apicall() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://registryblocks.com/app/rest/registerProduct", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (alertDialog!=null)
                    alertDialog.dismiss();

                Fragment fragment=new ItemImages();
                getFragmentManager().beginTransaction().replace(R.id.scannerActivityContainer,fragment).addToBackStack("abc").commit();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (alertDialog!=null)
                    alertDialog.dismiss();
                AlertsUtils.showErrorDialog(ScannerActivity.this,error.getMessage().toString());
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
                params.put("serial",strSerialNo);
                params.put("model",strModelNo);
                editor.putString("serial",strModelNo).commit();
                editor.putString("model",strModelNo).commit();

                return params;
            }
        };

        RequestQueue mRequestQueue = Volley.newRequestQueue(ScannerActivity.this);
        stringRequest.setRetryPolicy(new
                DefaultRetryPolicy(200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);
    }

    private void requestPermissions() {

        ActivityCompat.requestPermissions(this,new String[]{CAMERA}, Request_Camera);
    }

    private boolean checkPermission() {

        return (ContextCompat.checkSelfPermission(ScannerActivity.this, CAMERA )== PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void handleResult(Result result) {

        final String scanResult=result.getText();
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(ScannerActivity.this);

            }
        });
        builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(scanResult));
                startActivity(intent);


            }
        });
        builder.setMessage(scanResult);
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
        setContentView(R.layout.activity_scanner);
    }

    public void onReuestPermission(int requestCode, String Permissions[], int grantResults[])
    {
        switch (requestCode)
        {
            case Request_Camera:
                if (grantResults.length>0)
                {
                    boolean cameraAccepted=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted)
                    {
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                        {
                            if (shouldShowRequestPermissionRationale(CAMERA))
                            {
                                displaAlertMessage("You need to allow both permissions", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                            requestPermissions(new String[]{CAMERA},Request_Camera);
                                        }
                                    }
                                });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            if (checkPermission())
            {
                if (scannerView==null)
                {
                    scannerView=new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }
            else
            {
                requestPermissions();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void displaAlertMessage(String message, DialogInterface.OnClickListener listener)
    {
        new AlertDialog.Builder(ScannerActivity.this)
                .setMessage(message)
                .setPositiveButton("OK",listener)
                .setNegativeButton("Cancel",null)
                .create()
                .show();
    }


    private void setCustomActionBar() {

        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) this).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.itemreg_actionbar, null);
        LinearLayout linearLayout=(LinearLayout)mCustomView.findViewById(R.id.llBackItemReg);
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
