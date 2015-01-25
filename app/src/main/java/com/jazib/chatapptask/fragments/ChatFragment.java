package com.jazib.chatapptask.fragments;

import android.app.Fragment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.jazib.chatapptask.FragmentContainerActivity;
import com.jazib.chatapptask.R;
import com.jazib.chatapptask.adapters.ChatListAdapter;
import com.jazib.chatapptask.models.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class ChatFragment extends Fragment {

    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 30;
    private ParseUser mUser;
    private ListView lvChat;
    private ChatListAdapter mAdapter;
    private EditText etMessage;
    private Button btSend;
    private Handler handler = new Handler();


    private void setClickListener() {
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String body = etMessage.getText().toString();
                Message message = new Message();
                message.setUserId(mUser.getObjectId());
                message.setUserName(mUser.getUsername());
                message.setMessage(body);
                mAdapter.addItem(message);
                lvChat.setSelection(mAdapter.getCount() - 1);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                    }
                });
                etMessage.setText("");
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // init views and widgets
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        etMessage = (EditText) view.findViewById(R.id.etMessage);
        btSend = (Button) view.findViewById(R.id.btSend);
        lvChat = (ListView) view.findViewById(R.id.lvChat);
        mAdapter = new ChatListAdapter(getActivity(), mUser.getObjectId());
        lvChat.setAdapter(mAdapter);
        setClickListener();
        setHasOptionsMenu(true);
        return view;
    }

    // Query messages from Parse so we can load them into the chat adapter
    private void receiveMessage() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByAscending("createdAt");
        // Execute query for messages asynchronously
        query.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    // no error = response successful
                    mAdapter.addAllItems(messages);
                } else {
                    Log.d("message", "Error: " + e.getMessage());
                }
                // fetch new messages
                handler.postDelayed(runnable, 100);
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            receiveMessage();

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = ParseUser.getCurrentUser();

        // start fetching messages
        handler.postDelayed(runnable, 100);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        ParseUser.logOut();
        FragmentContainerActivity activity = (FragmentContainerActivity) getActivity();
        activity.switchFragment(new LoginFragment());
    }
}
