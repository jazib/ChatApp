package com.jazib.chatapptask;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.jazib.chatapptask.fragments.LoginFragment;


public class FragmentContainerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new LoginFragment())
                    .commit();
        }
    }



    public void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

}
