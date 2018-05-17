package com.techease.registryblocks.Activities.Adapters;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.techease.registryblocks.Activities.Controller.AllProductsModel;
import com.techease.registryblocks.Activities.Fragments.ProductDetailsFragment;
import com.techease.registryblocks.R;

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
        viewHolder.tvDate=(TextView)convertView.findViewById(R.id.tvProductDate);
        viewHolder.linearLayout=(LinearLayout)convertView.findViewById(R.id.ll);
        viewHolder.ivProductImage=(ImageView)convertView.findViewById(R.id.ivProductImage);
        viewHolder.tvTitle.setText(model.getProductName());
        viewHolder.tvModel.setText(model.getProductModel());
        viewHolder.tvDate.setText(model.getProductDate());
        Glide.with(context).load(model.getProductImage()).into(viewHolder.ivProductImage);

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ProductId=model.getProductId();
                Bundle bundle=new Bundle();
                bundle.putString("id",ProductId);
                Fragment fragment=new ProductDetailsFragment();
                fragment.setArguments(bundle);
                ((AppCompatActivity)context).getFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack("abc").commit();
            }
        });


        convertView.setTag(viewHolder);
        return convertView;
    }


    public class MyViewHolder {

        ImageView ivProductImage;
        TextView tvTitle,tvModel,tvDate;
        LinearLayout linearLayout;


    }
}
