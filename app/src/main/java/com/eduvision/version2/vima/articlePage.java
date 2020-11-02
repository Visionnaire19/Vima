package com.eduvision.version2.vima;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.eduvision.version2.vima.Tabs.ArticleAdapter;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.Recents;
import com.google.gson.Gson;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.eduvision.version2.vima.Tabs.Fetching.myData;
import static com.eduvision.version2.vima.Tabs.Recents.likedItemsPosition;

public class articlePage extends AppCompatActivity {

    long article_id;
    TextView price, title, description, shop_name, shop_description, shop_location;
    ImageView  big_pic, sm_pic1, sm_pic2, sm_pic3, shop_pic;
    CircleImageView profile_picture;
    Spinner spin1, spin2;
    String phoneNumber;
    CardView like;

    public void whatclick(View v){
        String number = "+22676603608";
        String url = "https://api.whatsapp.com/send?phone="+number;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public void callclick(View v){

        String number = "+22676603608";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
    public void smsclick(View v){
        String number = "+22676603608";
        String message = "J'ai connu votre boutique a travers l'applocation Vima";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"+number));
        intent.putExtra("sms_body", message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indiv_article_page);

        LinearLayout myll = findViewById(R.id.ll);
        myll.setBackgroundResource(R.drawable.border_home);
        Bundle i = getIntent().getExtras();
        IndividualArticle article = new IndividualArticle();
        if(i != null){
            article = myData.get(i.getInt("LockerKey"));
        }

        article_id = article.getRank();
        profile_picture = findViewById(R.id.profile_image);
        price = findViewById(R.id.article_price);
        title = findViewById(R.id.article_title);
        description = findViewById(R.id.article_description);
        shop_name = findViewById(R.id.shop_name);
        shop_description = findViewById(R.id.shop_description);
        shop_location = findViewById(R.id.shop_location);
        big_pic = findViewById(R.id.big_picture);
        sm_pic1 = findViewById(R.id.smaller_images1);
        sm_pic2 = findViewById(R.id.smaller_images2);
        sm_pic3 = findViewById(R.id.smaller_images3);
        shop_pic = findViewById(R.id.shop_picture);
        spin1 = findViewById(R.id.color_spinner);
        spin2 = findViewById(R.id.sex_spinner);
        like = findViewById(R.id.like);
        ImageButton goBack = findViewById(R.id.go_back);
        TextView textLike = findViewById(R.id.textLike);
        IndividualArticle finalArticle = article;
        if(finalArticle.isLiked){
            like.getBackground().setTint(getResources().getColor(R.color.Red));
            textLike.setTextColor(getResources().getColor(R.color.White));
            textLike.setText("Vous aimez cet article!");
        }
        else{
            like.getBackground().setTint(getResources().getColor(R.color.Grey));
            textLike.setTextColor(getResources().getColor(R.color.Black));
            textLike.setText("Liker");
        }
        like.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                finalArticle.isLiked = !finalArticle.isLiked;
                boolean temp = finalArticle.isLiked;

                if (!(temp)) {
                    for(IndividualArticle likedArticle : myData){
                        if (likedArticle.positionInDataBase == finalArticle.positionInDataBase) {
                            likedArticle.isLiked = false;
                        }
                    }
                    like.getBackground().setTint(getResources().getColor(R.color.Grey));
                    textLike.setTextColor(getResources().getColor(R.color.Black));
                    textLike.setText("Liker");
                    if(likedItemsPosition.contains(finalArticle.positionInArray)) {
                        likedItemsPosition.remove(finalArticle.positionInArray);
                    }
                } else if (temp) {
                    like.getBackground().setTint(getResources().getColor(R.color.Red));
                    textLike.setTextColor(getResources().getColor(R.color.White));
                    textLike.setText("Vous aimez cet article!");
                    if(!Recents.myLikedItems.contains(finalArticle)){
                        Recents.myLikedItems.add(finalArticle);
                        finalArticle.positionInArray = likedItemsPosition.size();
                        likedItemsPosition.add(finalArticle.positionInDataBase);
                        if(Recents.myLikedItems != null){
                            SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs",MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            Gson gson = new Gson();
                            String jsonText = gson.toJson(Recents.myLikedItems);
                            editor.putString("LikedItems", jsonText);
                            editor.apply();
                        }
                        for(IndividualArticle likedArticle : myData){
                            if (likedArticle.positionInDataBase == finalArticle.positionInDataBase) {
                                likedArticle.isLiked = true;
                            }
                        }
                    }
                }
            }
        });
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });

        big_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(articlePage.this, ArticleAlone.class);
                if (i != null) {
                    intent.putExtra("LockerKey", i.getInt("LockerKey"));
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        sm_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(articlePage.this, ArticleAlone.class);
                if (i != null) {
                    intent.putExtra("LockerKey", i.getInt("LockerKey"));
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        sm_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(articlePage.this, ArticleAlone.class);
                if (i != null) {
                    intent.putExtra("LockerKey", i.getInt("LockerKey"));
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        sm_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(articlePage.this, ArticleAlone.class);
                if (i != null) {
                    intent.putExtra("LockerKey", i.getInt("LockerKey"));
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        Context mContext = getApplicationContext();

        ArticleAdapter.glideIt(big_pic, article.getP_photo(), mContext);
        ArticleAdapter.glideIt(sm_pic1, article.getP_photo(), mContext);
        ArticleAdapter.glideIt(sm_pic2, article.getP_photo(), mContext);
        ArticleAdapter.glideIt(sm_pic3, article.getP_photo(), mContext);

        price.setText(String.valueOf(article.getPrice()));
        title.setText(article.getName());
        description.setText(article.getName());
        shop_name.setText(article.getShop_name());
        shop_description.setText(article.getShop_name());
        shop_location.setText(article.getShop_name());

        phoneNumber = article.getPhone();

    }
}
