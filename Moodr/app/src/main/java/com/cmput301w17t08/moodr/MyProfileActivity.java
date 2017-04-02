package com.cmput301w17t08.moodr;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.Transaction;

import java.util.ArrayList;

/**
 * This activity displays the user's moods. The user has the option to add a mood from this activity.
 * The user can create a new story from here.
 *
 * Due to the common load method, this extends Profile.
 * @see Profile
 */
public class MyProfileActivity extends Profile implements AddStory.OnCompleteListener{
    private User user;
    private ArrayList<Mood> moods = new ArrayList<>();
    private ProfileMoodAdapter adapter;
    private ListView moodsListview;
    private ListView notifications;
    private ArrayList<Story> stories;
    private Filter filter;
    private ArrayAdapter notificationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new NavDrawerSetup(this, toolbar).setupNav();

        moods.addAll(CurrentUserSingleton.getInstance().getMyMoodList().getListOfMoods());
        user = CurrentUserSingleton.getInstance().getUser();
        setTitle(user.getName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMood();
            }
        });

        FloatingActionButton map_buttom = (FloatingActionButton) findViewById(R.id.go_to_map);
        map_buttom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyProfileActivity.this, "Go to map activity", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        MenuItem item1 = menu.findItem(R.id.storyButton);
        MenuItem item2 = menu.findItem(R.id.filter_menu);
        MenuItem item3 = menu.findItem(R.id.action_add_complete);
        MenuItem item4 = menu.findItem(R.id.action_add_cancel);

        if (item1 != null){
            item1.setVisible(true);
        }

        if (item2 != null){
            item2.setVisible(true);
        }

        if (item3 != null){
            item3.setVisible(false);
        }

        if (item4 != null){
            item4.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.storyButton){
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.add(new AddStory(), "AddStoryFragment_1");
            fragmentTransaction.commit();
        }
        return false;
    }


    public void toggleCheckBoxes(Boolean checked){
        adapter.setcheck(checked);
    }

    public ArrayList<Mood> getSelected(){
        return adapter.getChecked();
    }



    @Override
    public void onStart(){
        super.onStart();

        moodsListview = (ListView) findViewById(R.id.profile_moodlist);
        adapter = new ProfileMoodAdapter(this, moods);

        filter = adapter.getFilter();
        setFilter(filter);

        moodsListview.setAdapter(adapter);
        moodsListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToMood(i);
            }
        });



        notifications = (ListView) findViewById(R.id.notifications);
        stories = CurrentUserSingleton.getInstance().getUser().getStories();

        Log.d("STORIES", Integer.toString(stories.size()));

        notificationAdapter = new ArrayAdapter<>(this, R.layout.notification, stories);
        notifications.setAdapter(notificationAdapter);

        notifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // view Story
            }
        });
    }

    /**
     * goes to add mood activity to add a mood
     */
    private void addMood(){
        Intent intent  = new Intent(this, AddMoodActivity.class);
        startActivityForResult(intent,1);
    }

    /**
     * To view the details of the mood. Passes an index instead of a mood instance in the parent
     * @param i index of the mood in moodList
     */
    private void goToMood(int i){
        i = CurrentUserSingleton.getInstance().getMyMoodList().getListOfMoods().indexOf(moods.get(i));
        Intent intent = new Intent(this, ViewMyMoodActivity.class);
        intent.putExtra("index", i);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            // on any change to moods, the filter will reset.
            adapter.setMoods(CurrentUserSingleton.getInstance().getMyMoodList().getListOfMoods());

        }
    }

    @Override
    public void onResume(){
        super.onResume();
        adapter.setMoods(CurrentUserSingleton.getInstance().getMyMoodList().getListOfMoods());
    }

    // when returning from adding story. restores menu items and floating buttons.
    public void OnComplete(){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(manager.findFragmentByTag("AddStoryFragment_1"));
        transaction.commit();

        adapter.setcheck(false);
        supportInvalidateOptionsMenu();
        findViewById(R.id.go_to_map).setVisibility(View.VISIBLE);
        findViewById(R.id.fab).setVisibility(View.VISIBLE);
    }

}
