package com.techease.registryblocks.Activities.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.techease.registryblocks.Activities.Activities.BottomNavigationActivity;
import com.techease.registryblocks.Activities.Activities.HTTPMultiPartEntity;
import com.techease.registryblocks.Activities.Activities.ScannerActivity;
import com.techease.registryblocks.Activities.Utils.AlertsUtils;
import com.techease.registryblocks.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class ItemImages extends Fragment implements View.OnClickListener {

    ImageView iv1,iv2,iv3,iv1Plus,iv2Plus,iv3Plus;
    Button btnUpload;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO=2;
    String selectedPath1 ;
    String selectedPath2 ;
    String selectedPath3 ;
    Bitmap bm;
    Bitmap thumbnail;
    File file1,file2,file3,destination;
    Uri uri;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    android.support.v7.app.AlertDialog alertDialog;
    String userId,ModelNo,SerialNo,checkImage;
    int a=0,b=0,c=0;
    final CharSequence[] items = { "Take Photo", "Choose from Library","Cancel" };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_images, container, false);

        setCustomActionBar();
        sharedPreferences = getActivity().getSharedPreferences("abc", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userId=sharedPreferences.getString("userId","");
        ModelNo=sharedPreferences.getString("model","");
        SerialNo=sharedPreferences.getString("serial","");
        iv1=(ImageView)view.findViewById(R.id.iv1);
        iv2=(ImageView)view.findViewById(R.id.iv2);
        iv3=(ImageView)view.findViewById(R.id.iv3);
        iv1Plus=(ImageView)view.findViewById(R.id.iv1Plus);
        iv2Plus=(ImageView)view.findViewById(R.id.iv2Plus);
        iv3Plus=(ImageView)view.findViewById(R.id.iv3Plus);
        btnUpload=(Button)view.findViewById(R.id.btnUpload);


        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        iv3Plus.setOnClickListener(this);
        iv2Plus.setOnClickListener(this);
        iv1Plus.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();

        switch (id)
        {
            case R.id.iv1Plus:
                a++;
               CallALertDilaog();
                break;
            case R.id.iv2Plus:
                b++;
                CallALertDilaog();
                break;
            case R.id.iv3Plus:
                c++;
                CallALertDilaog();
                break;
            case R.id.btnUpload:
                if (selectedPath1!=null && selectedPath2!=null && selectedPath3!=null)
                {
                    if (alertDialog==null)
                    {
                        alertDialog= AlertsUtils.createProgressDialog(getActivity());
                        alertDialog.show();
                    }
                    ItemImages.UploadFileToServer uploadFileToServer=new UploadFileToServer();
                    uploadFileToServer.execute();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Uploading failed");
                    builder.setMessage("Please select all the three images!");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }

        }
    }

    private void CallHttp() {
        File file1 = new File(selectedPath1);
        File file2 = new File(selectedPath2);
        File file3 = new File(selectedPath3);
        String urlString = "http://techeasesol.com/pos_app/pos_apis/insert_job_completed_images";
    }

    public void CallALertDilaog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (items[which].equals("Take Photo"))
                {
                    callCamera();
                }
                else if (items[which].equals("Choose from Library"))
                {
                    callGallery();
                }
                else if (items[which].equals("Cancel"))
                {

                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }
    private void callCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    private void callGallery()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_TAKE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            onCaptureImageResult(data);
        }
        else if (requestCode==REQUEST_TAKE_PHOTO)
        {
            onSelectFromGalleryResult(data);

        }



    }

    private void onCaptureImageResult(Intent data) {

        thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (a>0)
        {
            selectedPath1=BitMapToString(thumbnail);
            if (selectedPath1!=null)
            {
                iv1.setImageBitmap(thumbnail);
                a=0;
                iv1Plus.setVisibility(View.GONE);
            }
            else
            {
                iv1Plus.setVisibility(View.VISIBLE);
            }

        }
        else
            if (b>0)
            {
                selectedPath2=BitMapToString(thumbnail);
                if (selectedPath2!=null)
                {
                    iv2.setImageBitmap(thumbnail);
                    b=0;
                    iv2Plus.setVisibility(View.GONE);
                }
                else
                {
                    iv2Plus.setVisibility(View.VISIBLE);
                }

            }
            else
            if (c>0)
            {
                selectedPath3=BitMapToString(thumbnail);
                if (selectedPath3!=null)
                {
                    iv3.setImageBitmap(thumbnail);
                    c=0;
                    iv3Plus.setVisibility(View.GONE);
                }
                else
                {
                    iv3Plus.setVisibility(View.VISIBLE);
                }

            }

    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (data!=null)
        {
            uri=data.getData();
            if (data != null) {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    if (uri!=null)
                    {
                        String picpath=getPath(uri);
                        destination=new File(picpath);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), e.getCause().toString(), Toast.LENGTH_SHORT).show();
                }
        }


        }
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        if (a>0)
        {
            selectedPath1=filePath;
            if (selectedPath1!=null)
            {
                iv1.setImageBitmap(BitmapFactory.decodeFile(filePath));
                a=0;
                iv1Plus.setVisibility(View.GONE);
            }
            else
            {
                iv1Plus.setVisibility(View.VISIBLE);
            }

        }
        else
        if (b>0)
        {
            selectedPath2=filePath;
            if (selectedPath2!=null)
            {
                iv2.setImageBitmap(BitmapFactory.decodeFile(filePath));
                b=0;
                iv2Plus.setVisibility(View.GONE);
            }
              else
            {
                iv2Plus.setVisibility(View.VISIBLE);
            }

        }
        else
        if (c>0)
        {
            selectedPath3=filePath;
            if (selectedPath3!=null)
            {
                iv3.setImageBitmap(BitmapFactory.decodeFile(filePath));
                c=0;
                iv3Plus.setVisibility(View.GONE);
            }
            else
            {
                iv3Plus.setVisibility(View.VISIBLE);
            }

        }

        return cursor.getString(column_index);
    }


    class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            file1 = new File(selectedPath1);
             file2 = new File(selectedPath2);
            file3 = new File(selectedPath3);
            String responseString;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://rogervaneijk.com/registeryblocks/rest/uploadPicture");
            try {
                HTTPMultiPartEntity entity = new HTTPMultiPartEntity(
                        new HTTPMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) 100) * 100));
                            }
                        });
                // Adding file data to http body
                // Extra parameters if you want to pass to server
                entity.addPart("file1", new FileBody(file1));
                entity.addPart("file2", new FileBody(file2));
                entity.addPart("file3", new FileBody(file3));
                Looper.prepare();
                entity.addPart("serial", new StringBody(SerialNo));
                entity.addPart("model", new StringBody(ModelNo));
                entity.addPart("userid", new StringBody(userId));

                Log.d("model",ModelNo);
                Log.d("serial",SerialNo);


                httppost.setEntity(entity);
                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                responseString = EntityUtils.toString(r_entity);


            } catch (ClientProtocolException e) {
                responseString = e.toString();
                Log.d("zmaClient", e.getCause().toString());

            } catch (IOException e) {
                responseString = e.toString();
                Log.d("zmaIo", e.getCause().toString());
            }

            Log.d("zma return string", responseString);
            return responseString;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (alertDialog!=null)
                alertDialog.dismiss();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Uploaded Successfully!");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                   startActivity(new Intent(getActivity(), BottomNavigationActivity.class));
                   getActivity().finish();
                }
            });
            builder.show();
        }
    }

    private void setCustomActionBar() {

        android.support.v7.app.ActionBar mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.custom_image_item, null);
        LinearLayout linearLayout=(LinearLayout)mCustomView.findViewById(R.id.llBackItemImage);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(),ScannerActivity.class));
                getActivity().finish();
            }
        });
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }
}
