package tkhub.project.autoonlineadmin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import tkhub.project.autoonlineadmin.Layout.Home;
import tkhub.project.autoonlineadmin.R;


/**
 * Created by Himanshu on 4/10/2015.
 */
public class UserRegAdapter extends RecyclerView.Adapter<UserRegAdapter.MyViewHolder> implements View.OnClickListener {

    Context mContext;
    ArrayList<UserRegItem> item;

    boolean status = false;


    public UserRegAdapter(Context mContext, ArrayList<UserRegItem> albumList) {
        this.mContext = mContext;
        this.item = albumList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_reg_users, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.userName.setText("User Name : " + item.get(position).userName);
        holder.phone.setText("Number : " + item.get(position).phone);

        if(item.get(position).email==null){
            holder.email.setVisibility(View.GONE);
            holder.nic.setVisibility(View.GONE);
            holder.sex.setVisibility(View.GONE);
            holder.date.setVisibility(View.GONE);
        }else {
            holder.email.setVisibility(View.VISIBLE);
            holder.nic.setVisibility(View.VISIBLE);
            holder.sex.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);
            holder.email.setText("Email : " + item.get(position).email);
            holder.nic.setText("NIC : " + item.get(position).nic);
            holder.sex.setText("Gender : " + item.get(position).sex);
            holder.date.setText("Date : " + item.get(position).date);
        }



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
        TextView userName, email, phone, nic, sex, date;

        public MyViewHolder(View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.textView6);
            email = (TextView) itemView.findViewById(R.id.textView7);
            phone = (TextView) itemView.findViewById(R.id.textView8);
            nic = (TextView) itemView.findViewById(R.id.textView9);
            sex = (TextView) itemView.findViewById(R.id.textView10);
            date = (TextView) itemView.findViewById(R.id.textView11);


        }


        @Override
        public void onClick(View v) {

        }

    }

}
