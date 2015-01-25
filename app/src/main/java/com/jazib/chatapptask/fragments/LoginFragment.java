package com.jazib.chatapptask.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jazib.chatapptask.FragmentContainerActivity;
import com.jazib.chatapptask.R;
import com.jazib.chatapptask.models.Message;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

/**
 * Created by Jazib on 1/25/2015.
 */
public class LoginFragment extends Fragment {
    EditText etName;
    EditText etPassword;
    EditText etPhone;
    Button btLogin;
    Button btSignup;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        etName = (EditText) rootView.findViewById(R.id.etName);
        etPhone = (EditText) rootView.findViewById(R.id.etPhone);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        btLogin = (Button) rootView.findViewById(R.id.btLogin);
        btSignup = (Button) rootView.findViewById(R.id.btSignup);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs(etName, etPassword)) {
                    loginUser(etName.getText().toString(), etPassword.getText().toString());
                }
            }
        });

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs(etName, etPassword, etPhone)) {
                    signupUser(etName.getText().toString(), etPassword.getText().toString(), etPhone.getText().toString());
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
           launchChat();
        }
    }

    private void loginUser(String username, String password) {
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Logging in. Please wait...", false);
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // user logged in!
                    dialog.dismiss();
                    launchChat();
                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Unable to log in. Pls make sure username and password is correct!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void signupUser(String username, String password, String phone) {
        // initialize parse user and put data

        final ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.put("phone", phone);
        user.put("phone_model", Build.MODEL);

        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "",
                "Signing up. Please wait...", false);

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    dialog.dismiss();
                    saveInitialMessage(user);
                    launchChat();
                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private void saveInitialMessage(ParseUser user) {
        // initial message when the user joins the chat room. This is kind of an introduction to others.
        Message message = new Message();
        message.setUserId(user.getObjectId());
        message.setUserName(user.getUsername());
        message.setMessage("Hello! I am " + user.getUsername() + ". My device is: " + user.get("phone_model"));
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

            }
        });
    }


    private void launchChat() {
        FragmentContainerActivity activity = (FragmentContainerActivity) getActivity();
        activity.switchFragment(new ChatFragment());
    }

    private boolean validateInputs(EditText... editTexts) {
        for (EditText et : editTexts) {
            if (!validateEmptyEditText(et)) {
                return false;
            }
        }
        return true;
    }

    private boolean validateEmptyEditText(EditText et) {
        if (TextUtils.isEmpty(et.getText())) {
            et.setError("Field can't be empty!");
            return false;
        }
        return true;
    }
}