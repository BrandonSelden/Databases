package com.mistershorr.databases;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FriendListActivity extends AppCompatActivity {
    private ListView friendsList;
    private FloatingActionButton addFriend;
    private List<Friend> friendList;
    private FriendListAdapter friendAdapter;
    private TextView name, moneyOwed, clumsiness;
    private Friend selectedFriend;
    public static final String FRIEND = "friend";


    private ListView listViewFriends;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        wireWidgets();

        listViewFriends = findViewById(R.id.listView_friend_list_activity_list);

        String userId = Backendless.UserService.CurrentUser().getObjectId();


        String whereClause = "ownerId = '" + userId + "'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause((whereClause));
        Backendless.Data.of( Friend.class ).find(queryBuilder, new AsyncCallback<List<Friend>>(){
            @Override
            public void handleResponse( List<Friend> foundFriends )
            {
                // every loaded object from the "Contact" table is now an individual java.util.Map
                Log.d("LOADED FRIENDS", "handleResponse: " + foundFriends.toString());
                //TODO make a custom adapter to display friends n load the list that is retrieved.
                friendList = foundFriends;
                friendAdapter = new FriendListAdapter(friendList);
                listViewFriends.setAdapter(friendAdapter);
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(FriendListActivity.this, fault.getDetail(), Toast.LENGTH_LONG).show();
            }
        });
        setListeners();

    }
    @Override
    protected void onResume(){
        String userId = Backendless.UserService.CurrentUser().getObjectId();


        String whereClause = "ownerId = '" + userId + "'";

        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause((whereClause));
        Backendless.Data.of( Friend.class ).find(queryBuilder, new AsyncCallback<List<Friend>>(){
            @Override
            public void handleResponse( List<Friend> foundFriends )
            {
                // every loaded object from the "Contact" table is now an individual java.util.Map
                Log.d("LOADED FRIENDS", "handleResponse: " + foundFriends.toString());
                //TODO make a custom adapter to display friends n load the list that is retrieved.
                friendList = foundFriends;
                friendAdapter = new FriendListAdapter(friendList);
                listViewFriends.setAdapter(friendAdapter);
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()
                Toast.makeText(FriendListActivity.this, fault.getDetail(), Toast.LENGTH_LONG).show();
            }
        });
        super.onResume();

    }

    private void setListeners(){
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startFriendsDetailIntent = new Intent(FriendListActivity.this, FriendsDetailActivity.class);
                startActivity(startFriendsDetailIntent);

            }
        });
        listViewFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent startFriendsDetailIntent = new Intent(FriendListActivity.this, FriendsDetailActivity.class);
                Friend friend = friendList.get(i);
                startFriendsDetailIntent.putExtra(FRIEND, friend);
                startActivity(startFriendsDetailIntent);
            }
        } );
//        listViewFriends.setOnItemLongClickListener(new AdapterView, new View view, int position, long id{
//            public void onItem
//        });


    }

    private void wireWidgets(){
        friendsList = findViewById(R.id.listView_friend_list_activity_list);
        addFriend = findViewById(R.id.floatingActionButton_add_friend_friend_list);
        name = findViewById(R.id.textView_friendlayout_name);
        moneyOwed = findViewById(R.id.textView_layoutfriend_moneyowed);
        clumsiness = findViewById(R.id.textView_friendlayout_clumsiness);
    }

    private class FriendListAdapter extends ArrayAdapter<Friend> {
        private List<Friend> friendList;
        //since we're in the HeroListActivity class, we already have the context
        //we're hardcoding in a particular layout, so we don't need to put it in the constructor either
        public FriendListAdapter(List<Friend> friendsList) {
            super(FriendListActivity.this, -1, friendsList);
            this.friendList = friendsList;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            if(convertView == null){
                convertView = inflater.inflate(R.layout.layout_friend, parent, false);
            }
            TextView textViewName = convertView.findViewById(R.id.textView_friendlayout_name);
            TextView textViewClumsiness = convertView.findViewById(R.id.textView_friendlayout_clumsiness);
            TextView textViewMoneyOwed = convertView.findViewById(R.id.textView_layoutfriend_moneyowed);
            textViewName.setText(friendList.get(position).getName());
            textViewClumsiness.setText(friendList.get(position).getClumsiness() + "");
            textViewMoneyOwed.setText(friendList.get(position).getMoneyOwed() + "");
            return convertView;
        }
    }

    public void deleteContact()
    {
        // put the initApp call somewhere early on in your app, perhaps main activity

        // create a new object, so there is something to delete

        Backendless.Persistence.save( friendList.get(0), new AsyncCallback<Friend>()
        {
            public void handleResponse( Friend savedFriend )
            {
                Backendless.Persistence.of( Friend.class ).remove( savedFriend,
                        new AsyncCallback<Long>()
                        {
                            public void handleResponse( Long response )
                            {
                                // Contact has been deleted. The response is the
                                // time in milliseconds when the object was deleted
                            }
                            public void handleFault( BackendlessFault fault )
                            {
                                // an error has occurred, the error code can be
                                // retrieved with fault.getCode()
                            }
                        } );
            }
            @Override
            public void handleFault( BackendlessFault fault )
            {
                // an error has occurred, the error code can be retrieved with fault.getCode()
            }
        });
    }
}
