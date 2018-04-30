package com.techease.registryblocks.Activities.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adamnoor.registryblocks.R;
import com.techease.registryblocks.Activities.Controller.AllProductsModel;

import java.util.ArrayList;

public class AllProductsAdapter extends BaseAdapter {
    ArrayList<AllProductsModel> models;
    Context context;
    private LayoutInflater layoutInflater;
    MyViewHolder viewHolder = null;
    public AllProductsAdapter(Context context, ArrayList<AllProductsModel> allProductsModelArrayList) {
        this.context=context;
        this.models=allProductsModelArrayList;
        if (context!=null)
        {
            this.layoutInflater=LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if (models!=null) return models.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(models != null && models.size() > position) return  models.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        final AllProductsModel model=models.get(position);
        if(models != null && models.size() > position) return  models.size();
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AllProductsModel model=models.get(position);
        viewHolder=new MyViewHolder() ;
        convertView=layoutInflater.inflate(R.layout.custom_all_products,parent,false);
        viewHolder.tvTitle=(TextView)convertView.findViewById(R.id.tvProductTitle);
        viewHolder.tvModel=(TextView)convertView.findViewById(R.id.tvProductModel);
        viewHolder.tvProductId=(TextView)convertView.findViewById(R.id.tvProductId);
        viewHolder.tvTitle.setText(model.getProductName());
        viewHolder.tvModel.setText(model.getProductModel());
        viewHolder.tvProductId.setText(model.getProductId());
        convertView.setTag(viewHolder);
        return convertView;
    }


    public class MyViewHolder {

        ImageView ivProductImage;
        TextView tvTitle,tvModel,tvProductId;



    }
}
