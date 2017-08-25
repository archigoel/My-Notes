package com.app.mynotes;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class TabActivity extends AppCompatActivity implements AddNotesFragment.ViewInterface,AddNotesFragment.SaveEditText{
    
    public String value;
    public int position;
    View fragmentView;
    String text;
    int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Add Notes"));
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#3F51B5"));
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFF00"));
        tabLayout.addTab(tabLayout.newTab().setText("MY Notes"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                EditText  txtSpeechInput = (EditText) fragmentView.findViewById(R.id.txtSpeechInput);
                position = tab.getPosition();
                txtSpeechInput.setText(text);
                DisplayFragment f = (DisplayFragment)adapter.instantiateItem(viewPager,1);
                viewPager.setCurrentItem(tab.getPosition());
//                f.listUpdated();
                if (viewPager.getCurrentItem() == 1)
                f.listUpdated();
                System.out.println("TAB POSITION " + tab.getPosition());
                System.out.println("Flag  " +flag);

                if(flag == 1){
                    txtSpeechInput.setText(value);
                    flag = 0;
                }
                else{
                    txtSpeechInput.setText("");
                }


            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

                System.out.println("TAB REselected");

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateText(String t){
        flag = 1;
        System.out.println("TEXT UPDATE  " + t);
        System.out.println("Flag"   +flag);
        value = t;
    }


    @Override
    public void onLinearLayoutCreated(View view) {

        fragmentView = view;

    }
    @Override
    public void onTextChange(String text) {

        this.text = text;

    }

}