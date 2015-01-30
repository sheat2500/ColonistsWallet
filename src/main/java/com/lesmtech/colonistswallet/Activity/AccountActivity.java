package com.lesmtech.colonistswallet.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.lesmtech.colonistswallet.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Te on 1/24/15.
 */
public class AccountActivity extends FragmentActivity {

    final int LOGINFRAGMENT = 0;
    final int REGISTERFRAGMENT = 1;
    final int NUM_FRAGMENTS = 2;

    LogInFragment logInFragment;
    RegisterFrgament registerFrgament;
    Fragment[] Fragments;
    FragmentManager mFragmentManager;

    // LogInFragment
    EditText mUsername_LogIn;
    EditText mPassword_LogIn;

    // RegisterFragment
    EditText mUsername_Register;
    EditText mPassword_Register;
    EditText mEmail_Register;

    final int RESULTCODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        initView();
    }

    private void initView() {
        mFragmentManager = getSupportFragmentManager();
        registerFrgament = (RegisterFrgament) mFragmentManager.findFragmentById(R.id.registerFragment);
        logInFragment = (LogInFragment) mFragmentManager.findFragmentById(R.id.logInFragment);
        Fragments = new Fragment[]{logInFragment, registerFrgament};
        mUsername_LogIn = (EditText) findViewById(R.id.etUserName_LogIn);
        mUsername_Register = (EditText) findViewById(R.id.etUserName_Rgt);
        mPassword_LogIn = (EditText) findViewById(R.id.etPass_LogIn);
        mPassword_Register = (EditText) findViewById(R.id.etPass_Rgt);
        mEmail_Register = (EditText) findViewById(R.id.etEmail_Rgt);

        // LogIn or Register Fragement? 0 - LogIn ; 1 - Register
        int index = getIntent().getIntExtra("Status", 0);

        if (index == 0) {
            showThisFragment(LOGINFRAGMENT);
        } else {
            showThisFragment(REGISTERFRAGMENT);
        }
    }

    private void showThisFragment(int fragment) {
        // hide all fragments and show the one need
        for (int i = 0; i < NUM_FRAGMENTS; i++) {
            mFragmentManager.beginTransaction().hide(Fragments[i]).commit();
        }
        mFragmentManager.beginTransaction().show(Fragments[fragment]).commit();
    }


    public static class LogInFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_login, container, false);
            return v;
        }
    }

    public static class RegisterFrgament extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_register, container, false);
            return v;
        }
    }

    // Background thread with HttpPost Request
    private class LogInThread extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://www.bostonmiya.com/test/registration.do");
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("username", "rindt"));
            nameValuePairs.add(new BasicNameValuePair("password", "12345"));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                System.out.print(builder.toString());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "true";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }

    /*
        LogInFragment
     */
    public void btnSingIn(View v) {
        ParseUser.logInInBackground(mUsername_LogIn.getText().toString(), mPassword_LogIn.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    displayToast("Success to Log In");
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("username", user.getUsername());
                    bundle.putString("email", user.getEmail());

                    // photo_lite url
                    ParseFile photo_lite = user.getParseFile("photo_lite");
                    bundle.putString("photo_lite", photo_lite.getUrl());

                    intent.putExtra("user", bundle);
                    setResult(RESULTCODE, intent);
                    finish();
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    displayToast("Fail to Log In");
                }
            }
        });
    }

    // LogInFragment to RegisterFragment
    public void btnToRgt(View v) {
        showThisFragment(REGISTERFRAGMENT);
    }


    /*
        RegisterFragment
     */
    public void btnRgt(View v) {
        // Apply ParseUser to verify User Account;
        ParseUser mParseUser = new ParseUser();
        mParseUser.setUsername(mUsername_Register.getText().toString());
        mParseUser.setPassword(mPassword_Register.getText().toString());
        mParseUser.setEmail(mEmail_Register.getText().toString());
        mParseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    displayToast("Success to Register");
                    Intent intent = new Intent();
                    setResult(RESULTCODE, intent);
                    finish();
                } else {
                    displayToast("Fail to Register");


                }
            }
        });
    }

    // For Testing
    public void displayToast(String content) {
        Toast.makeText(getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }

}
