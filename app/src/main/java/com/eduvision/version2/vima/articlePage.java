package com.eduvision.version2.vima;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;

import de.hdodenhof.circleimageview.CircleImageView;

public class articlePage extends AppCompatActivity {

    private long article_id;
    TextView price, title, description, shop_name, shop_description, shop_location;
    ImageView  big_pic, sm_pic1, sm_pic2, sm_pic3, sm_pic4, shop_pic;
    CircleImageView profile_picture;
    Spinner spin1, spin2;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.indiv_article_page);

        LinearLayout myll = findViewById(R.id.ll);
        myll.setBackgroundResource(R.drawable.border_home);
        Bundle i = getIntent().getExtras();
        IndividualArticle article = new IndividualArticle();
        if(i != null){
            article = Fetching.myData.get(i.getInt("LockerKey"));
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
        shop_pic = findViewById(R.id.shop_picture);
        spin1 = findViewById(R.id.color_spinner);
        spin2 = findViewById(R.id.sex_spinner);

        //Fetch
        /*
        ArticleAdapter.glideIt(big_pic, bPictures.getPhoto(), mContext);
        ArticleAdapter.glideIt(sm_pic1, bPictures.getSmall_pic1(), mContext);
        ArticleAdapter.glideIt(sm_pic2, bPictures.getSmall_pic2(), mContext);
        ArticleAdapter.glideIt(sm_pic3, bPictures.getSmall_pic3(), mContext);
        ArticleAdapter.glideIt(sm_pic4, bPictures.getSmall_pic4(), mContext);
        */

        price.setText(article.getPrice().toString());
        title.setText(article.getName());
        description.setText(article.getName());
        shop_name.setText(article.getShop_name());
        shop_description.setText(article.getShop_name());
        shop_location.setText(article.getShop_name());


        /*
        final String[] sizeSelected = new String[1];
        final String[] colorSelected = new String[1];

        mDatabase.child("Articles").child(Long.toString(article_id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                info = dataSnapshot.child("infos").child("name").getValue(Article_info.class);
                bDescription = dataSnapshot.child("infos").child("name").getValue(Article_description.class);
                bPictures = dataSnapshot.child("infos").child("name").getValue(Article_pictures.class);

                //Setting up the spinner for colors
                ArrayList<String> colors = new ArrayList<>();
                int color_count = (int) dataSnapshot.child("infos").child("name").child("description").child("colors").getChildrenCount();
                for(int i = 1; i<=color_count; i++){
                    colors.add(dataSnapshot.child("infos").child("name").child("description").child("colors").child(Integer.toString(i)).toString());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, colors);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin1.setAdapter(arrayAdapter);
                spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Do anything you want with this String, Yay!
                        //For instance: TODO: Pass this String when the user adds the article to their panier
                        colorSelected[0] = parent.getItemAtPosition(position).toString();
                    }
                    @Override
                    public void onNothingSelected(AdapterView <?> parent) {
                    }
                });

                //Setting up the spinner for sizes
                ArrayList<String> sizes = new ArrayList<>();
                int size_count = (int) dataSnapshot.child("infos").child("name").child("description").child("size").getChildrenCount();
                for(int i = 1; i<=size_count; i++){
                    sizes.add(dataSnapshot.child("infos").child("name").child("description").child("size").child(Integer.toString(i)).toString());
                }
                ArrayAdapter<String> sizeArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, sizes);
                sizeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin1.setAdapter(sizeArrayAdapter);
                spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //Do anything you want with this String, Yay!
                        //For instance: TODO: Pass this String when the user adds the article to their panier
                        sizeSelected[0] = parent.getItemAtPosition(position).toString();
                    }
                    @Override
                    public void onNothingSelected(AdapterView <?> parent) {
                    }
                });

                //Getting the info of the shop that sells the article
                mDatabase.child("Shops").child(Integer.toString(info.getSeller_id())).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        individualArticleConstructor = dataSnapshot.getValue(IndividualArticleConstructor.class);
                        //I am adding all of this inside of the onDataChange because of the asynchronous loading
                        shop_name.setText(individualArticleConstructor.getName());
                        shop_description.setText(individualArticleConstructor.getDescription());
                        shop_location.setText(individualArticleConstructor.getLocation());
                        Glide.with(mContext)
                                .load(individualArticleConstructor.getPicture_logo())
                                .into(shop_pic);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                //I am adding all of this inside of the onDataChange function because of the asynchronous loading
                price.setText(info.getPrice());
                title.setText(info.getName());
                description.setText(bDescription.getDescription());

                //Setting the ImageResource of ImageViews from Firebase data
                Glide.with(mContext)
                        .load(bPictures.getPhoto())
                        .into(big_pic);
                Glide.with(mContext)
                        .load(bPictures.getSmall_pic1())
                        .into(sm_pic1);
                Glide.with(mContext)
                        .load(bPictures.getSmall_pic2())
                        .into(sm_pic2);
                Glide.with(mContext)
                        .load(bPictures.getSmall_pic3())
                        .into(sm_pic3);
                Glide.with(mContext)
                        .load(bPictures.getSmall_pic4())
                        .into(sm_pic4);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        */


    }
}
