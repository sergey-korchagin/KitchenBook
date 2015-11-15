package com.book.kitchen.kitchenbook.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.book.kitchen.kitchenbook.R;

import java.util.List;

/**
 * Created by User on 15/11/2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;



    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
    holder.tv.setText("Test, here will be short recipe introduction");
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView tv;

        ViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cv);
            tv = (TextView)itemView.findViewById(R.id.tmpTv);

            tv.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == tv.getId()) {
                Toast toast = Toast.makeText(v.getContext(),
                        "Recipe clicked", Toast.LENGTH_SHORT);
                toast.show();
            }
        }


    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card_view, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}