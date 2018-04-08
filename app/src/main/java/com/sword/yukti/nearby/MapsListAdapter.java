package com.sword.yukti.nearby;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sword on 30-03-2018.
 */

public class MapsListAdapter extends BaseAdapter {
    Context mContext;
    public ArrayList<Getter_setter> mListimage;
    LayoutInflater inflter;


    public MapsListAdapter(Context context, ArrayList<Getter_setter> mapsList) {
        this.mContext=context;
        this.mListimage=mapsList;
        inflter=(LayoutInflater.from(mContext));


    }

    @Override
    public int getCount() {
        return mListimage.size();
    }

    @Override
    public Object getItem(int position) {
        return mListimage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflter.inflate(R.layout.custom_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.listshow.setText(mListimage.get(position).getPlaceName()+"\n"+ mListimage.get(position).getVicinity());
        Log.d(mListimage.get(position).getRating(),"rateadapter");
        holder.listshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,DescriptionActivity.class);
                intent.putExtra("place_name",mListimage.get(position).getPlaceName());
                intent.putExtra("place_address",mListimage.get(position).getVicinity());
                intent.putExtra("reference",mListimage.get(position).getReference());
                intent.putExtra("place_rating",mListimage.get(position).getRating());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
    private class ViewHolder
    {
        TextView listshow;
        ConstraintLayout constraintLayout;

        public ViewHolder(View view) {
            listshow = (TextView) view.findViewById(R.id.place_text);






        }
    }
}
