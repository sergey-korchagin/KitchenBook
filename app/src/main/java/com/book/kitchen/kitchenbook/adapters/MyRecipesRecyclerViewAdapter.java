package com.book.kitchen.kitchenbook.adapters;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.book.kitchen.kitchenbook.interfaces.OnItemClickListener;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


/**
 * Created by User on 16/11/2015.
 */
public class MyRecipesRecyclerViewAdapter extends RecyclerView.Adapter<MyRecipesRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<ParseObject> mList;
    OnItemClickListener onItemClickListener;

    public MyRecipesRecyclerViewAdapter(List<ParseObject> list, OnItemClickListener listener) {
        mList = list;
        onItemClickListener = listener;

    }

    public void updateChanges(int position) {
        notifyItemChanged(position);

    }

    @Override
    public void onBindViewHolder(final MyRecipesRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.description.setText(Utils.capitalizeFirstLetter(mList.get(position).get("description").toString()));
        holder.title.setText(Utils.capitalizeFirstLetter(mList.get(position).get("title").toString()));
        holder.category.setText(Utils.getCategoryFromCode(context, (int) mList.get(position).get("category")));//.toString());
        holder.public_or_private.setText(Utils.capitalizeFirstLetter(mList.get(position).get("public").toString()));
        if (mList.get(position).get("mainImage") != null) {
            ParseFile applicantResume = (ParseFile) mList.get(position).get("mainImage");
            applicantResume.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        holder.icon.setImageBitmap(bmp);
                        holder.icon.toString();
                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView description;
        TextView title;
        ImageView icon;
        ImageView btnRemove;
        TextView category;
        TextView public_or_private;


        ViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cvMy);
            description = (TextView) itemView.findViewById(R.id.tmpTvMy);
            title = (TextView) itemView.findViewById(R.id.cardTitleMy);
            icon = (ImageView) itemView.findViewById(R.id.itemIconMy);
            icon.setOnClickListener(this);
            description.setOnClickListener(this);
            btnRemove = (ImageView) itemView.findViewById(R.id.removeButton);
            btnRemove.setOnClickListener(this);
            category = (TextView)itemView.findViewById(R.id.my_card_category);
            public_or_private = (TextView)itemView.findViewById(R.id.public_private);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == description.getId() || v.getId() == icon.getId()) {
                onItemClickListener.onCardClickListener(mList.get(getAdapterPosition()), true);

            } else if (v.getId() == btnRemove.getId()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure?")
                        .setCancelable(true).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParseObject.createWithoutData("recipe", mList.get(getAdapterPosition()).getObjectId()).deleteEventually();

//                        ParseQuery query = new ParseQuery("recipe");
//                        query.whereEqualTo("objectId", mList.get(getAdapterPosition()).getObjectId());
//                        query.findInBackground(new FindCallback<ParseObject>() {                            @Override
//                            public void done(List<ParseObject> nameList, ParseException e)
//                            {
//                                if (e == null)
//                                {
//                                    for (ParseObject nameObj : nameList)
//                                    {
//                                        nameObj.put("userId", "deletedByUser");
//                                        nameObj.saveInBackground();
//
//                                    }
//                                }
//                                else
//                                {
//                                }
//                            }
//                        });


                        mList.remove(mList.get(getAdapterPosition()));
                        MyRecipesRecyclerViewAdapter.this.updateChanges(getAdapterPosition());
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

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
