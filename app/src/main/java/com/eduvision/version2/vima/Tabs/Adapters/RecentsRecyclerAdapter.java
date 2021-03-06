package com.eduvision.version2.vima.Tabs.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.Popular;
import com.eduvision.version2.vima.articlePage;

import java.util.ArrayList;

public class RecentsRecyclerAdapter extends RecyclerView.Adapter<RecentsRecyclerAdapter.MyViewHolder> {
    private ArrayList<IndividualArticle> myData;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View myLayout;
        public MyViewHolder(View v) {
            super(v);
            myLayout = v;
            ImageButton btn = v.findViewById(R.id.like_button);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecentsRecyclerAdapter(ArrayList<IndividualArticle> mData, Context mContext) {
        this.myData = mData;
        this.mContext = mContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecentsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pop_article_model, parent, false);
        MyViewHolder vh = new MyViewHolder(v);


        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        IndividualArticle myArticle;
        View convertView = holder.myLayout ;
        myArticle = Spinning.myData.get(position);
        ImageButton myBtn = convertView.findViewById(R.id.like_button);
        if(myArticle.isLiked) {
            myBtn.setImageResource(R.drawable.icon_b);
        }
        else{
            myBtn.setImageResource(R.drawable.icon_a);
        }
        Fetching.changeText(convertView, myArticle, mContext);
        int finalPosition = position;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(mContext, articlePage.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                myIntent.putExtra("LockerKey", finalPosition);
                mContext.startActivity(myIntent);
            }
        });
        myBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fetching.handleLike(myBtn, myArticle, mContext);
                for(IndividualArticle likedArticle : myData){
                    if (likedArticle.positionInDataBase == myArticle.positionInDataBase) {
                        likedArticle.isLiked = true;
                        notifyDataSetChanged();
                        Popular.mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return Spinning.myData.size();
    }

}