package com.mistershorr.databases;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import java.util.HashMap;
import java.util.Map;

public class FriendsDetailActivity extends AppCompatActivity {
    private EditText name, moneyOwed;
    private SeekBar clumsiness, gymFrequency;
    private Switch awesomeness;
    private RatingBar trustworthiness;
    private Button confirm;
    private Friend newFriend;

    public static final String NEW_FRIEND = "newfriend";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_detail);

        newFriend = new Friend();
        wireWidgets();
        setListeners();
        Intent previousFriendDetail = getIntent();

        if(previousFriendDetail.getParcelableExtra(FriendListActivity.FRIEND)!= null){ //determines if the friend is nll or not
            newFriend = previousFriendDetail.getParcelableExtra(FriendListActivity.FRIEND);
            name.setText(newFriend.getName());
            moneyOwed.setText(String.valueOf(newFriend.getMoneyOwed()));
            clumsiness.setProgress(newFriend.getClumsiness());
            gymFrequency.setProgress((int)newFriend.getGymFrequency());
            awesomeness.setChecked(newFriend.awesome());
            trustworthiness.setNumStars(newFriend.getTrustworthiness());
        }
        else{
            newFriend = new Friend();
        }

        // load the Friend from the list activity by getting the extra
        // if that's null, then assign a new Friend object
    }


    private void setListeners(){
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newFriend.setName(name.getText().toString());
                newFriend.setMoneyOwed(Double.valueOf(moneyOwed.getText().toString()));
                newFriend.setClumsiness(clumsiness.getProgress());
                newFriend.setAwesome(awesomeness.isChecked());
                newFriend.setGymFrequency(gymFrequency.getProgress());
                newFriend.setTrustworthiness(trustworthiness.getNumStars());
                Backendless.Persistence.save(newFriend, new AsyncCallback<Friend>() {
                            @Override
                            public void handleResponse(Friend response) {
                                finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(FriendsDetailActivity.this, fault.getMessage(), Toast.LENGTH_SHORT);
                            }
                });
            }
        });

    }

    private void wireWidgets(){
        name = findViewById(R.id.editText_name_friends_detail);
        moneyOwed = findViewById(R.id.editText_money_owed_friends_detail);
        clumsiness = findViewById(R.id.seekBar_clumsiness_friends_detail);
        gymFrequency = findViewById(R.id.seekBar_gym_frequency_friends_detail);
        awesomeness = findViewById(R.id.switch_awesome_friends_detail);
        trustworthiness = findViewById(R.id.ratingBar_trust_friends_detail);
        confirm = findViewById(R.id.button_confirm_friends_activity);
    }
}
