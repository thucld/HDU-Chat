package vn.hdu.go2jp.hduchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.adapter.PagerAdapter;
import vn.hdu.go2jp.hduchat.common.Tag;
import vn.hdu.go2jp.hduchat.fragment.ChatBoxFragment;
import vn.hdu.go2jp.hduchat.fragment.ChatListFragment;
import vn.hdu.go2jp.hduchat.fragment.ContactListFragment;
import vn.hdu.go2jp.hduchat.fragment.MoreFragment;
import vn.hdu.go2jp.hduchat.services.ChatService;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;
import vn.hdu.go2jp.hduchat.util.ToastUtil;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initial();
        FireBaseUtil.test();
        startService(new Intent(this, ChatService.class));
    }

    private void initial() {

        setupToolBar();
        setupTabLayout();
        setupViewPager();
        findViewById(R.id.search).setOnClickListener(v -> {
            toolbar.setTitle("search clicked");
            new ToastUtil().showLong(getApplicationContext(), "search clicked!");
        });
        findViewById(R.id.addPerson).setOnClickListener(v -> {
            toolbar.setTitle("addPerson clicked");
            new ToastUtil().showShort(getApplicationContext(), "addPerson clicked!");
        });

    }

    private void setupToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Recent");
//        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.header_ic_setting);
//        toolbar.setOverflowIcon(drawable);
    }

    private void setupViewPager() {
        final ViewPager viewPager = findViewById(R.id.viewPager);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new ContactListFragment());
        fragments.add(new ChatListFragment());
        fragments.add(new ChatListFragment());
        fragments.add(new ChatBoxFragment());
        fragments.add(new MoreFragment());

        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), fragments, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Tag tag = (Tag) tab.getTag();
                if (tag != null) {
                    toolbar.setTitle(tag.toString());
                    if (tab.getCustomView() != null) {
                        View view = tab.getCustomView().findViewById(R.id.icon);
                        switch (tag) {
                            case CONTACT:
                                view.setBackgroundResource(R.drawable.tab_ic_contacts_selected);
                                break;
                            case CHAT:
                                view.setBackgroundResource(R.drawable.tab_ic_chats_selected);
                                break;
                            case TIMELINE:
                                view.setBackgroundResource(R.drawable.tab_ic_timeline_selected);
                                break;
                            case CALL:
                                view.setBackgroundResource(R.drawable.tab_ic_calls_selected);
                                break;
                            case MORE:
                                view.setBackgroundResource(R.drawable.tab_ic_more_selected);
                                break;
                            default:
                                break;
                        }
                    }
                }

            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Tag tag = (Tag) tab.getTag();
                if (tab.getCustomView() != null && tag != null) {
                    View view = tab.getCustomView().findViewById(R.id.icon);
                    switch (tag) {
                        case CONTACT:
                            view.setBackgroundResource(R.drawable.tab_ic_contacts1);
                            break;
                        case CHAT:
                            view.setBackgroundResource(R.drawable.tab_ic_chats1);
                            break;
                        case TIMELINE:
                            view.setBackgroundResource(R.drawable.tab_ic_timeline2);
                            break;
                        case CALL:
                            view.setBackgroundResource(R.drawable.tab_ic_calls);
                            break;
                        case MORE:
                            view.setBackgroundResource(R.drawable.tab_ic_more);
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupTabLayout() {
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(R.drawable.tab_ic_contacts_selected)).setTag(Tag.CONTACT));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(R.drawable.tab_ic_chats1)).setTag(Tag.CHAT));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(R.drawable.tab_ic_timeline2)).setTag(Tag.TIMELINE));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(R.drawable.tab_ic_calls)).setTag(Tag.CALL));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(R.drawable.tab_ic_more)).setTag(Tag.MORE));
    }

    @NonNull
    private View getView(int icon) {
        View view1 = getLayoutInflater().inflate(R.layout.custom_tab_layout, null);
        view1.findViewById(R.id.icon).setBackgroundResource(icon);
        return view1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.setting:
                new ToastUtil().showShort(getApplicationContext(), "Setting");
                return true;
            case R.id.about:
                new ToastUtil().showShort(getApplicationContext(), "About");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
