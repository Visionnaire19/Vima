package com.eduvision.version2.vima.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.eduvision.version2.vima.Login.SeConnecter;
import com.eduvision.version2.vima.Login.Sinscrire;
import com.eduvision.version2.vima.MainPage;
import com.eduvision.version2.vima.R;
import com.eduvision.version2.vima.Spinning;
import com.eduvision.version2.vima.TabAdapter;
import com.eduvision.version2.vima.Tabs.DownloadFilesTask;
import com.eduvision.version2.vima.Tabs.FetchShops;
import com.eduvision.version2.vima.Tabs.Fetching;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class login_activity extends AppCompatActivity {

    SignInButton signup;
    FirebaseAuth mAuth;
    private final static int RC_SIGN_IN = 2;
    GoogleSignInClient mGoogleSignInClient;
    private final String PREFERENCE_FILE_KEY = "myAppPreference";
    private static final String KEY_USERNAME = "prefUserNameKey";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context = this;
    View fragment;
    private TabAdapter adapter;

    CallbackManager mCallbackManager;


    private TabLayout tabLayout;
    private ViewPager viewPager;
    FrameLayout frameLayout;
    FragmentManager fm;
    public static Context mContext;


    public static boolean tryFetching(){
        final boolean[] result = {false};
        for (int i = 0; i<=100; i++){
            if (DownloadFilesTask.downloadProgress != 100){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(DownloadFilesTask.downloadProgress != 100){
                            result[0] = false;
                        }
                        else {
                            result[0] = true;
                        }
                    }
                },100000);
            }
            else{
                result[0] = true;
                break;
            }
        }
        return result[0];

    }
    public void afterSpinning(){
        progress.setVisibility(View.VISIBLE);
        if (!tryFetching()) {
            progress.setVisibility(View.GONE);
            tryAgain.setVisibility(View.VISIBLE);
            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    afterRetryButtonClick();
                }
            });
        }
        else {
            Intent intent = new Intent(getApplicationContext(), MainPage.class);
            startActivity(intent);
            finish();
        }
    }
    public void afterRetryButtonClick(){
        afterSpinning();
        tryAgain.setVisibility(View.GONE);
    }
    View progress;
    Button tryAgain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mContext = getApplicationContext();

        viewPager = findViewById(R.id.sign_up_view_pager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Sinscrire(), "S'inscrire");
        adapter.addFragment(new SeConnecter(), "Se connecter");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        progress = findViewById(R.id.progress);
        tryAgain = findViewById(R.id.retry);
        ImageView myLogo = (ImageView) findViewById(R.id.logo);
        final int[] i = {0};
        myLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Fetching.waitInternetAvailable(getApplicationContext())){
                    Fetching.makeCustomToast(getApplicationContext(), "Connectez-vous à Internet", Toast.LENGTH_SHORT);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), Spinning.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        /*

        //   signup = findViewById(R.id.google_sign_in);
       mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("prefID", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();



        FacebookSdk.sdkInitialize(getApplicationContext());


        // 0 - for private mode

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.facebook_login);
        loginButton.setReadPermissions("email", "public_profile", "id", "name");
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonclickFb(v);
            }
        });


    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUI(user);
                            Intent aftersigningupIntent = new Intent(login_activity.this, MainActivity.class);
                            startActivity(aftersigningupIntent);

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            UpdateUI(user);
                            Intent afterFacebooksigningupIntent = new Intent(login_activity.this, MainActivity.class);
                            startActivity(afterFacebooksigningupIntent);
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            // ;
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void buttonclickFb(View v) {
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(login_activity.this, "User Cancelled", Toast.LENGTH_SHORT).show();
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(login_activity.this, "ERROR", Toast.LENGTH_SHORT).show();
                // ...
            }
        });

    }


    public void UpdateUI(FirebaseUser user) {
        String name = user.getDisplayName();

        editor.putString("nameKey", name);
        editor.apply();
    }

*/
    }

}
