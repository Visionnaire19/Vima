package com.eduvision.version2.vima;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.eduvision.version2.vima.SearchEngine.Search_engine;
import com.eduvision.version2.vima.Tabs.Boutiques;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.eduvision.version2.vima.Tabs.IndividualArticle;
import com.eduvision.version2.vima.Tabs.IndividualShop;
import com.eduvision.version2.vima.Tabs.Popular;
import com.eduvision.version2.vima.Tabs.Recents;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Bag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Bag extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TabAdapter adapter;
    EditText searchView;
    RecyclerView searchResults;
    DatabaseReference databaseReference;
    Button clear;
    ImageView profile;
    ArrayList<IndividualArticle> articleList;
    ArrayList<ShopModel> shopList;
    ArrayList<String> typeList;
    ArrayList<Integer> positionList;

    Search_engine search = new Search_engine();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String mParam1;
    private String mParam2;
    SharedPreferences sharedPreferences;

    public Bag() {
        // Required empty public constructor
    }

    public void putLikedItems(){
        if(Recents.myLikedItems != null){
            SharedPreferences prefs = getActivity().getSharedPreferences("prefs",getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            String jsonText = gson.toJson(Recents.myLikedItems);
            editor.putString("LikedItems", jsonText);
            editor.apply();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        putLikedItems();
    }

    public static Bag newInstance(String param1, String param2) {
        Bag fragment = new Bag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bag, container, false);
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getApplicationContext().getSharedPreferences("prefID", Context.MODE_PRIVATE);
//Setting up the tabLayout
        viewPager = getView().findViewById(R.id.bag_view_pager);
        tabLayout = getView().findViewById(R.id.bag_tabLayout);
        adapter = new TabAdapter(getFragmentManager());
        adapter.addFragment(new Boutiques(), "Boutiques");
        adapter.addFragment(new Recents(), "Recents");
        adapter.addFragment(new Popular(), "Populaires");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        String url;

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                position = 3;

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                position = 0;

            }
        });

        //Setting up the search engine
        searchResults = getView().findViewById(R.id.search_results);
        clear = getView().findViewById(R.id.clear);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        searchView = getView().findViewById(R.id.search);

        searchResults.setHasFixedSize(true);
        searchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResults.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        articleList = new ArrayList<>();
        shopList = new ArrayList<>();
        typeList = new ArrayList<>();
        positionList = new ArrayList<>();



        clear.setVisibility(View.INVISIBLE);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                clear.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                clear.setVisibility(View.INVISIBLE);
            }
            @Override
            public void afterTextChanged(Editable s) {


                if(!s.toString().isEmpty()){
                    clear.setVisibility(View.VISIBLE);

                    //calling the search engine activity and the function that handles the search
                    search.setResearch(Fetching.myData,s.toString(),searchResults,getContext(),articleList,shopList,typeList,positionList);
                }
                else {
                    articleList.clear();
                    shopList.clear();
                    searchResults.removeAllViews();

                }

            }


        });
clear.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        searchView.setText("");
        hideKeyboard(getView());
    }
});

//Access Profile
profile = getView().findViewById(R.id.profile_image);

profile = getView().findViewById(R.id.profile_image);
        String profilePicture = sharedPreferences.getString("profile", "https://www.google.com/search?q=placeholder+profile+pictures+free+to+use&tbm=isch&ved=2ahUKEwjA6ZvV2tDrAhUElBoKHd_bDRIQ2-cCegQIABAA&oq=placeholder+profile+pictures+free+to+use&gs_lcp=CgNpbWcQAzoECAAQHlC7YVixcGCvcWgAcAB4AIAB5QWIAbsVkgEHNC0zLjEuMZgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=ANVSX8DpHoSoat-3t5AB&bih=792&biw=1536#imgrc=_JeJ3jskVgcZaM");
        Glide.with(getApplicationContext())
                .load(profilePicture)
                .placeholder(R.drawable.categorie_enfant)
                .into(profile);
profile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent= new Intent(getContext(), ProfilePage.class);
        startActivity(intent);
    }
});
    }

private void hideKeyboard(View v){
    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
}





    }









