package com.book.kitchen.kitchenbook.adapters;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.book.kitchen.kitchenbook.R;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by User on 29/11/2015.
 */
public class CommentsListAdapter extends ArrayAdapter<ParseObject> {
    Context context;
    List<ParseObject> mList;
    public CommentsListAdapter (Context context,List<ParseObject> parseObjects){
        super(context, R.layout.single_comment_list_item, parseObjects);
        this.context = context;
        this.mList = parseObjects;

    }

    static class ViewHolder {
        public TextView userName;
        public TextView commentText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.single_comment_list_item, null, true);
            holder = new ViewHolder();
            holder.userName = (TextView) rowView.findViewById(R.id.userNameComment);
            holder.commentText = (TextView) rowView.findViewById(R.id.userComment);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }
        holder.userName.setText(mList.get(position).get("userName").toString());
        holder.commentText.setText(mList.get(position).get("comment").toString());
        return rowView;
    }
}
