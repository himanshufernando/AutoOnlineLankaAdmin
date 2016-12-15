package tkhub.project.autoonlineadmin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tkhub.project.autoonlineadmin.R;


/**
 * Created by Himanshu on 4/10/2015.
 */
public class OrderImageAdapter extends RecyclerView.Adapter<OrderImageAdapter.MyViewHolder> implements View.OnClickListener {

    Context mContext;
    ArrayList<OrderImageItem> item;

    boolean status = false;


    public OrderImageAdapter(Context mContext, ArrayList<OrderImageItem> albumList) {
        this.mContext = mContext;
        this.item = albumList;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card_image, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Picasso.with(mContext).load(item.get(position).imageurl).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    @Override
    public void onClick(View v) {
        System.out.println("sssdsdsdsdsdsdsdsd");
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       ImageView image;
        public MyViewHolder(View itemView) {
            super(itemView);
            image =(ImageView) itemView.findViewById(R.id.thumbnail);
        }


        @Override
        public void onClick(View v) {

        }

    }

}
