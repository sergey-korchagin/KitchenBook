package com.book.kitchen.kitchenbook.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.book.kitchen.kitchenbook.fragments.Login;
import com.book.kitchen.kitchenbook.fragments.RecipeFullScreen;
import com.book.kitchen.kitchenbook.interfaces.FindInterface;
import com.book.kitchen.kitchenbook.interfaces.OnItemClickListener;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by User on 15/11/2015.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements FindInterface {

    static Context context;
    static List<ParseObject> mList;
    static OnItemClickListener onItemClickListener;
   static List<ParseObject> filteredList;


    public RecyclerViewAdapter(List<ParseObject> list, OnItemClickListener listener)
    {
        mList = list;
        onItemClickListener = listener;
        this.filteredList = new ArrayList<>();
        filteredList.addAll(mList);

    }

    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.description.setText(Utils.capitalizeFirstLetter(filteredList.get(position).get("description").toString()));
        holder.title.setText(Utils.capitalizeFirstLetter(filteredList.get(position).get("title").toString()));
        holder.userName.setText(Utils.capitalizeFirstLetter(filteredList.get(position).get("userName").toString()));
        holder.category.setText(Utils.getCategoryFromCode(context, (int) filteredList.get(position).get("category")));
        holder.cookingTime.setText((filteredList.get(position).get("cookingTime").toString()));
        if(filteredList.get(position).get("mainImage")!=null){
            ParseFile applicantResume = (ParseFile) filteredList.get(position).get("mainImage");
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
        TextView userName;
        TextView category;
        TextView cookingTime;

        ViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cv);
            description = (TextView)itemView.findViewById(R.id.tmpTv);
            title= (TextView)itemView.findViewById(R.id.cardTitle);
            icon = (ImageView)itemView.findViewById(R.id.itemIcon);
            icon.setOnClickListener(this);
            userName = (TextView)itemView.findViewById(R.id.cardUserName);
            description.setOnClickListener(this);
            category = (TextView)itemView.findViewById(R.id.card_category);
            cookingTime = (TextView)itemView.findViewById(R.id.coockingTime);


        }

        @Override
        public void onClick(View v) {
            if(v.getId() == description.getId()|| v.getId() == icon.getId()) {
                onItemClickListener.onCardClickListener(filteredList.get(getAdapterPosition()), false);
//                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
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
        return filteredList.size();
    }

    public void filter(String constraint) {
        filteredList = new ArrayList<ParseObject>();

        for (ParseObject item : mList) {
            if (item.get("title").toString().contains(constraint)|| item.get("description").toString().contains(constraint)) {
                filteredList.add(item);
            }
        }

        notifyDataSetChanged();
    }


}