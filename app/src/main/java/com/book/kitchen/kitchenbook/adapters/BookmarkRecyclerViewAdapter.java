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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.Utils.Utils;
import com.book.kitchen.kitchenbook.interfaces.OnItemClickListener;
import com.book.kitchen.kitchenbook.managers.VolleyManager;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 24/11/2015.
 */
public class BookmarkRecyclerViewAdapter extends RecyclerView.Adapter<BookmarkRecyclerViewAdapter.ViewHolder> {

    List<ParseObject> mList;
    Context context;
    OnItemClickListener onItemClickListener;

    VolleyManager volleyManager;

    public BookmarkRecyclerViewAdapter(List<ParseObject> list, OnItemClickListener listener){
        mList = list;
        onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_card, parent, false);
        context = parent.getContext();
        volleyManager = VolleyManager.getInstance();

        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(final BookmarkRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.description.setText(Utils.capitalizeFirstLetter(mList.get(position).get("description").toString()));
        holder.title.setText(Utils.capitalizeFirstLetter(mList.get(position).get("title").toString()));
        if (mList.get(position).get("mainImage") != null) {
            ParseFile applicantResume = (ParseFile) mList.get(position).get("mainImage");
            volleyManager.addToRequestQueue(volleyManager.createImageRequest(applicantResume.getUrl(), new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                   holder.icon.setImageBitmap(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }));

        }//        holder.category.setText(Utils.getCategoryFromCode(context, (int) mList.get(position).get("category")));//.toString());



    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView description;
        TextView title;
        TextView category;
        ImageView icon;
        ImageView btnRemove;



        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvBo);
            description = (TextView) itemView.findViewById(R.id.tmpTvBo);
            title = (TextView) itemView.findViewById(R.id.cardTitleBo);

            icon = (ImageView) itemView.findViewById(R.id.itemIconBo);
            icon.setOnClickListener(this);
            description.setOnClickListener(this);

            btnRemove = (ImageView) itemView.findViewById(R.id.removeButtonBo);
            btnRemove.setOnClickListener(this);



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
                      //  ParseObject.createWithoutData("recipe", mList.get(getAdapterPosition()).getObjectId()).deleteEventually();
                        ParseObject.createWithoutData("recipe", mList.get(getAdapterPosition()).getObjectId()).deleteEventually();
                        mList.remove(mList.get(getAdapterPosition()));
                        BookmarkRecyclerViewAdapter.this.updateChanges(getAdapterPosition());
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        }
    }

    public void updateChanges(int position) {
        notifyItemChanged(position);

    }

}
