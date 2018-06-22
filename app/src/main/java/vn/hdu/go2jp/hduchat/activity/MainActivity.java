package vn.hdu.go2jp.hduchat.activity;

import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.widget.Toast;
import android.view.Menu;

import java.util.ArrayList;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.adapter.PagerAdapter;
import vn.hdu.go2jp.hduchat.fragment.ChatBoxFragment;
import vn.hdu.go2jp.hduchat.fragment.ChatListFragment;
import vn.hdu.go2jp.hduchat.fragment.ContactsFragment;
import vn.hdu.go2jp.hduchat.fragment.MoreFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initial();

    }

    private void initial() {

        setupToolBar();
        setupTabLayout();
        setupViewPager();
        findViewById(R.id.search).setOnClickListener(v -> {
            toolbar.setTitle("search clicked");
            Toast.makeText(getApplicationContext(), "clicked!", Toast.LENGTH_LONG).show();
        });
        findViewById(R.id.addPerson).setOnClickListener(v -> {
            toolbar.setTitle("addPerson clicked");
            Toast.makeText(getApplicationContext(), "clicked!", Toast.LENGTH_LONG).show();
        });

    }

    private void setupToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Recent");
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_settings_white_24dp);
        toolbar.setOverflowIcon(drawable);
    }

    private void setupViewPager() {
        final ViewPager viewPager = findViewById(R.id.viewPager);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new ChatListFragment());
        fragments.add(new ChatBoxFragment());
        fragments.add(new ContactsFragment());
        fragments.add(new MoreFragment());

        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), fragments, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getTag() != null) {
                    toolbar.setTitle(tab.getTag().toString());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupTabLayout() {
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_history_white_24dp).setTag("Recent"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_chat_white_24dp).setTag("Chats"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_contacts_white_24dp).setTag("Contacts"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_more_horiz_white_24dp).setTag("More..."));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.setting || super.onOptionsItemSelected(item);

    }

}
