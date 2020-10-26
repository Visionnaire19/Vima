package com.eduvision.version2.vima.Tabs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.eduvision.version2.vima.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class Fetching {
    public static int PopularPageNumber = 1;
    public static int RecentsPageNumber = 1;
    public static ArrayList<IndividualArticle> myData = new ArrayList<>(80);
    public static ArrayList<IndividualArticle> shopData = new ArrayList<>(80);
    public static String isDataFetched = "No";
    public static String isDataBeingFetched = "No";
    public static ArrayList<IndividualArticle> homeArticlesData = new ArrayList<>(4);
/*
    public static void handleLike(int n, ImageButton myBtn, IndividualArticle myArticle){
        switch(n%2){
            case 0:
                myBtn.setImageResource(R.drawable.icon_a);
                myArticle.positionInArray = likedItemsPosition.size();
                likedItemsPosition.add(myArticle.positionInDataBase);
                break;
             case 1:
                myBtn.setImageResource(R.drawable.icon_b);
                 likedItemsPosition.remove(myArticle.positionInArray);
                 break;
        }
    }

 */

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        return isConnected;
    }

    public static void changeText(View convertView, IndividualArticle myArticle, Context mContext){
        TextView myShop = convertView.findViewById(R.id.shop);
        TextView myDescription = convertView.findViewById(R.id.article_name);
        TextView myPrice = convertView.findViewById(R.id.article_price);
        myShop.setText(myArticle.getShop_name());
        myDescription.setText(myArticle.getName());
        myPrice.setText(myArticle.getPrice().toString());
        ArticleAdapter.glideIt(convertView.findViewById(R.id.article_picture), myArticle.getP_photo(), mContext);
    }

    public static void makeCustomToast(Context mContext, String mText, int length){
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View layout = mInflater.inflate(R.layout.custom_toast, null);

        TextView text = layout.findViewById(R.id.text);
        text.setText(mText);

        Toast toast = new Toast(mContext);
        toast.setGravity(Gravity.BOTTOM, 0, 40);
        toast.setDuration(length);
        toast.setView(layout);
        toast.show();
    }

    public static void getItems(){
        isDataBeingFetched = "Yes";
        final DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Articles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myData.clear();
                IndividualArticle currentArticle;
                String sCounter = snapshot.child("counter").getValue().toString();
                int counter = Integer.parseInt(sCounter);
                for(int i = 1; i<=(80); i++){
                    if(i < counter){
                        currentArticle = snapshot.child(Integer.toString(i)).getValue(IndividualArticle.class);
                    }
                    else{
                        currentArticle = snapshot.child(sCounter).getValue(IndividualArticle.class);
                    }
                    Objects.requireNonNull(currentArticle).positionInDataBase = i;

                    if(i < 5){
                        homeArticlesData.add(currentArticle);
                    }
                    myData.add(currentArticle);
                }
                isDataFetched = "Yes";

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
