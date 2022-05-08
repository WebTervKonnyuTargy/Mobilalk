package com.example.gyumi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class ShoppingitemAdapter extends RecyclerView.Adapter<ShoppingitemAdapter.ViewHolder> {
private ArrayList<ShoppingItem> mShoppingItemsData;
private Context mContext;
private int lastPosition=-1;

    ShoppingitemAdapter(Context context, ArrayList<ShoppingItem> itemsData) {
        this.mShoppingItemsData=itemsData;
        this.mContext=context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ShoppingitemAdapter.ViewHolder holder, int position) {
        ShoppingItem currentitem=mShoppingItemsData.get(position);

        holder.bindTo(currentitem);
    }

    @Override
    public int getItemCount() {
        return mShoppingItemsData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitle;
        private TextView mInfo;
        private TextView mXd;
        private ImageView mImg;
        private RatingBar mRating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle=itemView.findViewById(R.id.itemNev);
            mInfo=itemView.findViewById(R.id.itemInfo);
            mXd=itemView.findViewById(R.id.Moeloremegynemhatra);
            mImg=itemView.findViewById(R.id.itemImg);
            mRating=itemView.findViewById(R.id.rating);
        }

        public void bindTo(ShoppingItem currentitem){
            mTitle.setText(currentitem.getNev());
            mInfo.setText(currentitem.getInfo());
            mXd.setText(currentitem.getXd());
            mRating.setRating(currentitem.getRating());

            Glide.with(mContext).load(currentitem.getImgResource()).into(mImg);
        }
    }
}
