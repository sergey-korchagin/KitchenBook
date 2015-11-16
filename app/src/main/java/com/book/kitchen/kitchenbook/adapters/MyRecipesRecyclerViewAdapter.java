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
import com.parse.ParseObject;

import java.util.List;


/**
 * Created by User on 16/11/2015.
 */
public class MyRecipesRecyclerViewAdapter extends RecyclerView.Adapter<MyRecipesRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<ParseObject> mList;


    public MyRecipesRecyclerViewAdapter(List<ParseObject> list)
    {
        mList = list;

    }

    @Override
    public void onBindViewHolder(MyRecipesRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.description.setText(mList.get(position).get("description").toString());
        holder.title.setText(mList.get(position).get("title").toString());
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView description;
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cvMy);
            description = (TextView)itemView.findViewById(R.id.tmpTvMy);
            title= (TextView)itemView.findViewById(R.id.cardTitleMy);

            description.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == description.getId()) {
                Toast toast = Toast.makeText(v.getContext(),
                        "Recipe clicked", Toast.LENGTH_SHORT);
                toast.show();
            }
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card_my_recipes, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
