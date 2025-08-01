package com.ppmdev.agriman.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ppmdev.agriman.R;
import com.ppmdev.agriman.model.MarketPlaceAdModel;
import com.ppmdev.agriman.utils.AndroidUtil;

import java.util.ArrayList;

public class RecyclerMarketPlaceAdAdapter extends RecyclerView.Adapter<RecyclerMarketPlaceAdAdapter.ViewHolder> {

    Context context;
    ArrayList<MarketPlaceAdModel> arrayList;

    public RecyclerMarketPlaceAdAdapter (Context context, ArrayList<MarketPlaceAdModel> arrayList){
        this.context=context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_marketplacead_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MarketPlaceAdModel ad = arrayList.get(position);

        holder.productName.setText(arrayList.get(position).getProductName());
        holder.productDescription.setText(arrayList.get(position).getProductDescription());
        holder.productValue.setText(arrayList.get(position).getProductValue());
        holder.profileName.setText(arrayList.get(position).getProfileName());

        holder.userNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = ad.getUserNumber();
                if(phoneNumber != null && !phoneNumber.isEmpty()){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    context.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView productName;
        private TextView productDescription;
        private TextView productValue;
        private TextView profileName;
        private ConstraintLayout userNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.tv_marketPlace_ProductName);
            productDescription = itemView.findViewById(R.id.tv_marketPlace_productDescription);
            productValue = itemView.findViewById(R.id.tv_marketPlace_productValue);
            profileName=itemView.findViewById(R.id.tv_marketPlace_userName);
            userNumber=itemView.findViewById(R.id.constraint_marketPlaceCall);
        }
    }
}
