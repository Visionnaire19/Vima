package com.eduvision.version2.vima;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;

import com.eduvision.version2.vima.SearchEngine.Search_engine;
import com.eduvision.version2.vima.BoutiquesTab.tab_boutiques;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Bag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Bag extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    TabAdapter adapter;
    int position;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText searchView;
    RecyclerView searchResults;
    DatabaseReference databaseReference;
    FirebaseStorage storage = FirebaseStorage.getInstance();




    ArrayList<String> nameList;
    ArrayList<StorageReference> photoList;

    Search_engine search = new Search_engine();

    public Bag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Bag.
     */
    // TODO: Rename and change types and number of parameters
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


//Setting up the tabLayout
        viewPager = getView().findViewById(R.id.bag_view_pager);
        tabLayout = getView().findViewById(R.id.bag_tabLayout);
        adapter = new TabAdapter(getFragmentManager());
        adapter.addFragment(new tab_boutiques(), "Boutiques");
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
        databaseReference = FirebaseDatabase.getInstance().getReference();
        searchView = getView().findViewById(R.id.search);

        searchResults.setHasFixedSize(true);
        searchResults.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResults.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        nameList = new ArrayList<>();
        photoList = new ArrayList<>();

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {

                if(!s.toString().isEmpty()){
                    //calling the search engine activity and the function that handles the search
                    search.setAdapter(s.toString(),searchResults,getContext(),nameList,photoList);
                }
                else {
                    nameList.clear();
                    photoList.clear();
                    searchResults.removeAllViews();

                }

            }


        });
    }



    }









