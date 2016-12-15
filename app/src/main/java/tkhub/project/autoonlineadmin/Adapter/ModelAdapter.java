package tkhub.project.autoonlineadmin.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import tkhub.project.autoonlineadmin.Layout.Make;
import tkhub.project.autoonlineadmin.Layout.Model;
import tkhub.project.autoonlineadmin.R;


/**
 * Created by Himanshu on 4/10/2015.
 */
public class ModelAdapter extends RecyclerView.Adapter<ModelAdapter.MyViewHolder> implements View.OnClickListener {

    Context mContext;
    ArrayList<ModelItem> item;

    boolean status = false;


    public ModelAdapter(Context mContext, ArrayList<ModelItem> albumList) {
        this.mContext = mContext;
        this.item = albumList;
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_model, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
             holder.makeId.setText(String.valueOf(item.get(position).makeId));
             holder.make.setText(item.get(position).make);


        holder.layDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Model) mContext).deleteModelFromAdapter(item.get(position).makeId,item.get(position).make);
            }
        });

        holder.layUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Model) mContext).udateModelFromAdapter(item.get(position).makeId,item.get(position).make);
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
        TextView makeId,make;
        RelativeLayout layDelete,layUpdate;
        int status;

        public MyViewHolder(View itemView) {
            super(itemView);

            makeId =(TextView)itemView.findViewById(R.id.textView24);
            make =(TextView)itemView.findViewById(R.id.textView25);

            layDelete=(RelativeLayout)itemView.findViewById(R.id.relativeLayoutdelelte);
            layUpdate=(RelativeLayout)itemView.findViewById(R.id.relativeLayoutupdate);


        }


        @Override
        public void onClick(View v) {

        }

    }

}
