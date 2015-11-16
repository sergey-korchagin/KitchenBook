package com.book.kitchen.kitchenbook.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.interfaces.OnItemClickListener;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.List;


/**
 * Created by User on 16/11/2015.
 */
public class MyRecipesRecyclerViewAdapter extends RecyclerView.Adapter<MyRecipesRecyclerViewAdapter.ViewHolder> {

    Context context;
     static List<ParseObject> mList;
    static OnItemClickListener onItemClickListener;

    public MyRecipesRecyclerViewAdapter(List<ParseObject> list, OnItemClickListener listener)
    {
        mList = list;
        onItemClickListener = listener;

    }

    @Override
    public void onBindViewHolder(final MyRecipesRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.description.setText(mList.get(position).get("description").toString());
        holder.title.setText(mList.get(position).get("title").toString());
        if(mList.get(position).get("mainImage")!=null){
            ParseFile applicantResume = (ParseFile) mList.get(position).get("mainImage");
            applicantResume.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        holder.icon.setImageBitmap(bmp);
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView description;
        TextView title;
        ImageView icon;

        ViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cvMy);
            description = (TextView)itemView.findViewById(R.id.tmpTvMy);
            title= (TextView)itemView.findViewById(R.id.cardTitleMy);
            icon = (ImageView)itemView.findViewById(R.id.itemIconMy);

            description.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == description.getId()) {
                onItemClickListener.onCardClickListener(mList.get(getAdapterPosition()));

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
