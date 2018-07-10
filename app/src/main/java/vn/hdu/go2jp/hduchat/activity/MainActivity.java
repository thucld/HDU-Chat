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
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.adapter.PagerAdapter;
import vn.hdu.go2jp.hduchat.common.TAB;
import vn.hdu.go2jp.hduchat.fragment.TimelineFragment;
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
        FireBaseUtil.getInstance().test();
        startService(new Intent(this, ChatService.class));
    }

    private void initial() {

        setupToolBar();
        setupTabLayout();
        setupViewPager();
        findViewById(R.id.btSearchContact).setOnClickListener(v -> {
            toolbar.setTitle("search clicked");
            new ToastUtil().showLong(getApplicationContext(), "search clicked!");
        });
        findViewById(R.id.btAddPerson).setOnClickListener(v -> {
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
        fragments.add(new TimelineFragment());
        fragments.add(new MoreFragment());
        fragments.add(new MoreFragment());

        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), fragments, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(5);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                TAB tabTag = (TAB) tab.getTag();
                if (tabTag != null) {
                    toolbar.setTitle(tabTag.getValue());
                    if (tab.getCustomView() != null) {
                        ImageView view = tab.getCustomView().findViewById(R.id.icon);
                        view.setImageResource(tabTag.getIconSelected());
                        LinearLayout layout = findViewById(tabTag.getLlId());
                        layout.setVisibility(View.VISIBLE);
                    }
                    if (tabTag.getVisible()) {

//                        getSupportActionBar().hide();
//                        getSupportActionBar().;
//                       toolbar.hideOverflowMenu();
                    } else {
//                        setSupportActionBar(new Toolbar(getBaseContext()));
//                       toolbar.showOverflowMenu();
                    }
                }

            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TAB tabTag = (TAB) tab.getTag();
                if (tab.getCustomView() != null && tabTag != null) {
//                    View view = tab.getCustomView().findViewById(R.id.icon);
                    ImageView view = tab.getCustomView().findViewById(R.id.icon);

                    view.setImageResource(tabTag.getIcon());

                    LinearLayout layout = findViewById(tabTag.getLlId());
                    layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupTabLayout() {
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(TAB.CONTACT.getIconSelected())).setTag(TAB.CONTACT));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(TAB.CHAT.getIcon())).setTag(TAB.CHAT));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(TAB.TIMELINE.getIcon())).setTag(TAB.TIMELINE));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(TAB.CALL.getIcon())).setTag(TAB.CALL));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(TAB.MORE.getIcon())).setTag(TAB.MORE));
    }

    @NonNull
    private View getView(int icon) {
        View view1 = getLayoutInflater().inflate(R.layout.custom_tab_layout, null);
        ImageView imageView = view1.findViewById(R.id.icon);
        imageView.setImageResource(icon);
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
