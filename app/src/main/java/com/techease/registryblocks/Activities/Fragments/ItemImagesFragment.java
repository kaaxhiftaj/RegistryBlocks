package com.techease.registryblocks.Activities.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
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
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class ItemImagesFragment extends Fragment implements View.OnClickListener {

    ImageView iv1,iv2,iv3,iv1Plus,iv2Plus,iv3Plus;
    Button btnUpload;
    final int CAMERA_CAPTURE = 1;
    final int RESULT_LOAD_IMAGE = 2;
    File file1,file2,file3;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    android.support.v7.app.AlertDialog alertDialog;
    String userId,ModelNo,SerialNo;
    int a,b,c;
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

        a=0;
        b=0;
        c=0;

        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        iv3Plus.setOnClickListener(this);
        iv2Plus.setOnClickListener(this);
        iv1Plus.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {

            }

        }).check();
        return view;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();

        switch (id)
        {
            case R.id.iv1Plus:
                a++;
               cameraBuilder();
                break;
            case R.id.iv2Plus:
                b++;
                cameraBuilder();
                break;
            case R.id.iv3Plus:
                c++;
                cameraBuilder();
                break;
            case R.id.btnUpload:
                if (file1!=null && file2!=null && file3!=null)
                {
                    if (alertDialog==null)
                    {
                        alertDialog= AlertsUtils.createProgressDialog(getActivity());
                        alertDialog.show();
                    }
                    ItemImagesFragment.UploadFileToServer uploadFileToServer=new UploadFileToServer();
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

    public void cameraBuilder() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Open");
        String[] pictureDialogItems = {
                "\tGallery",
                "\tCamera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                galleryIntent();
                                break;
                            case 1:
                                cameraIntent();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void cameraIntent() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(captureIntent, CAMERA_CAPTURE);
    }

    public void galleryIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (a>0)
        {
            if (requestCode == RESULT_LOAD_IMAGE && null != data) {
                Uri selectedImageUri = data.getData();
                String imagepath = getPath(selectedImageUri);
                file1 = new File(imagepath);

            } else if (resultCode == RESULT_OK && requestCode == CAMERA_CAPTURE && data != null) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                file1 = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    file1.createNewFile();
                    fo = new FileOutputStream(file1);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                iv1.setImageBitmap(thumbnail);
                a=0;
            }
        }
        else
            if (b>0)
            {
                if (requestCode == RESULT_LOAD_IMAGE && null != data) {
                    Uri selectedImageUri = data.getData();
                    String imagepath = getPath(selectedImageUri);
                    file2 = new File(imagepath);

                } else if (resultCode == RESULT_OK && requestCode == CAMERA_CAPTURE && data != null) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    file2 = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");
                    FileOutputStream fo;
                    try {
                        file2.createNewFile();
                        fo = new FileOutputStream(file2);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    iv2.setImageBitmap(thumbnail);
                    b=0;
                }
            }
            else
                if (c>0)
                {
                    if (requestCode == RESULT_LOAD_IMAGE && null != data) {
                        Uri selectedImageUri = data.getData();
                        String imagepath = getPath(selectedImageUri);
                        file3 = new File(imagepath);

                    } else if (resultCode == RESULT_OK && requestCode == CAMERA_CAPTURE && data != null) {
                        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                        file3 = new File(Environment.getExternalStorageDirectory(),
                                System.currentTimeMillis() + ".jpg");
                        FileOutputStream fo;
                        try {
                            file3.createNewFile();
                            fo = new FileOutputStream(file3);
                            fo.write(bytes.toByteArray());
                            fo.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        iv3.setImageBitmap(thumbnail);
                        c=0;
                    }
                }

    }

    @SuppressLint("SetTextI18n")
    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        if (a>0)
        {
            iv1.setImageBitmap(BitmapFactory.decodeFile(filePath));
            a=0;
        }
        else
            if (b>0)
            {
                iv2.setImageBitmap(BitmapFactory.decodeFile(filePath));
                b=0;
            }
            else
                if (c>0)
                {
                    iv3.setImageBitmap(BitmapFactory.decodeFile(filePath));
                    c=0;
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

                Log.d("zmaFile",file1.toString());
                Log.d("zmaFile2",file2.toString());
                Log.d("zmaFile3",file3.toString());


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
                Log.d("zmaIo", responseString);
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
            if (s.contains("true"))
            {
                builder.setMessage("Uploaded successfully!");
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
            else
            {
                builder.setMessage(s);
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
