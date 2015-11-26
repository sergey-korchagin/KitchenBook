package com.book.kitchen.kitchenbook.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.book.kitchen.kitchenbook.R;
import com.book.kitchen.kitchenbook.interfaces.OnItemClickListener;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by User on 24/11/2015.
 */
public class BookmarkRecyclerViewAdapter extends RecyclerView.Adapter<BookmarkRecyclerViewAdapter.ViewHolder> {

    List<ParseObject> mList;
    Context context;
    OnItemClickListener onItemClickListener;


    public BookmarkRecyclerViewAdapter(List<ParseObject> list, OnItemClickListener listener){
        mList = list;
        onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_card, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);    }

    @Override
    public void onBindViewHolder(BookmarkRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
       // return mList.size();
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cv;
        TextView description;
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvBo);
            description = (TextView) itemView.findViewById(R.id.tmpTvBo);
            title = (TextView) itemView.findViewById(R.id.cardTitleBo);



        }

        @Override
        public void onClick(View v) {

            onItemClickListener.onCardClickListener(mList.get(getAdapterPosition()), true);

        }
    }

}
