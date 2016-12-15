package tkhub.project.autoonlineadmin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import tkhub.project.autoonlineadmin.Layout.Home;
import tkhub.project.autoonlineadmin.R;


/**
 * Created by Himanshu on 4/10/2015.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> implements View.OnClickListener {

    Context mContext;
    ArrayList<OrderItem> item;

    boolean status = false;


    public OrderAdapter(Context mContext, ArrayList<OrderItem> albumList) {
        this.mContext = mContext;
        this.item = albumList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_orders, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.orderId.setText("Order ID : " + item.get(position).orderId);
        holder.userID.setText("User : " + item.get(position).userName);
        int num = item.get(position).number;
        if (num == 0) {
            holder.number.setVisibility(View.GONE);
            holder.phonNumber = item.get(position).regUserNumber;
        } else {
            holder.number.setText("Number : " + String.valueOf(num));
            holder.phonNumber = String.valueOf(num);
        }
        holder.make.setText("Make : " + item.get(position).make);
        holder.model.setText("Model : " + item.get(position).model);
        holder.chassi.setText("Chassi : " + item.get(position).chassi);
        holder.year.setText("Year : " + item.get(position).year);
        holder.city.setText("City : " + item.get(position).city);
        holder.date.setText("Date : " + item.get(position).date);
        holder.status.setText("Status : " + item.get(position).status);
        holder.approve.setText("Approved : " + item.get(position).approve);
        holder.discription.setText("Description : " + item.get(position).discription);


        holder.layoutImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((Home) mContext).showOrderImages(item.get(position).imageID,item.get(position).orderId);

            }
        });

        holder.main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Home) mContext).showOrderStstus(item.get(position).orderId, item.get(position).userID, holder.phonNumber, item.get(position).status);

            }
        });

        holder.layoutFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Home) mContext).showFeedback(item.get(position).orderId);

            }
        });


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
        TextView orderId, userID, number, make, model, chassi, year, city, discription, date, status, approve;
        RelativeLayout main,layoutImages,layoutFeedback;
        String phonNumber;


        public MyViewHolder(View itemView) {
            super(itemView);

            orderId = (TextView) itemView.findViewById(R.id.textView6);
            userID = (TextView) itemView.findViewById(R.id.textView7);
            number = (TextView) itemView.findViewById(R.id.textView8);
            make = (TextView) itemView.findViewById(R.id.textView9);

            model = (TextView) itemView.findViewById(R.id.textView10);
            chassi = (TextView) itemView.findViewById(R.id.textView11);
            year = (TextView) itemView.findViewById(R.id.textView12);
            city = (TextView) itemView.findViewById(R.id.textView13);

            discription = (TextView) itemView.findViewById(R.id.textView17);
            date = (TextView) itemView.findViewById(R.id.textView14);
            status = (TextView) itemView.findViewById(R.id.textView15);
            approve = (TextView) itemView.findViewById(R.id.textView16);

            main = (RelativeLayout) itemView.findViewById(R.id.relativeLayoutmain);

            layoutImages=(RelativeLayout)itemView.findViewById(R.id.RelativeLayout_image);
            layoutFeedback=(RelativeLayout)itemView.findViewById(R.id.RelativeLayout_feedback);
        }


        @Override
        public void onClick(View v) {

        }

    }

}
