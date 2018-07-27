package vn.hdu.go2jp.hduchat.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.adapter.PagerAdapter;
import vn.hdu.go2jp.hduchat.common.TAB;
import vn.hdu.go2jp.hduchat.fragment.CallFragment;
import vn.hdu.go2jp.hduchat.fragment.ContactFragment;
import vn.hdu.go2jp.hduchat.fragment.MoreFragment;
import vn.hdu.go2jp.hduchat.fragment.RoomFragment;
import vn.hdu.go2jp.hduchat.fragment.TimelineFragment;
import vn.hdu.go2jp.hduchat.services.ChatService;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;
import vn.hdu.go2jp.hduchat.util.ToastUtil;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private RoomFragment roomFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolBar();
        setupTabLayout();
        setupViewPager();
        setupEvents();
        FireBaseUtil.test();
        startService(new Intent(this, ChatService.class));
    }

    private void setupToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupTabLayout() {
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(TAB.CONTACT.getIconSelected())).setTag(TAB.CONTACT));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(TAB.CHAT.getIcon())).setTag(TAB.CHAT));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(TAB.TIMELINE.getIcon())).setTag(TAB.TIMELINE));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(TAB.CALL.getIcon())).setTag(TAB.CALL));
        tabLayout.addTab(tabLayout.newTab().setCustomView(getView(TAB.MORE.getIcon())).setTag(TAB.MORE));
    }

    private void setupViewPager() {
        final ViewPager viewPager = findViewById(R.id.viewPager);
        ArrayList<Fragment> fragments = getFragments();
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
                        LinearLayout layout = findViewById(tabTag.getResIdToolbarView());
                        layout.setVisibility(View.VISIBLE);
                    }
                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TAB tabTag = (TAB) tab.getTag();
                if (tab.getCustomView() != null && tabTag != null) {
                    ImageView view = tab.getCustomView().findViewById(R.id.icon);
                    view.setImageResource(tabTag.getIcon());
                    LinearLayout layout = findViewById(tabTag.getResIdToolbarView());
                    layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void setupEvents() {
        findViewById(R.id.btSearchContact).setOnClickListener(v -> {
            toolbar.setTitle("search clicked");
            new ToastUtil().showLong(getApplicationContext(), "search clicked!");
        });
        findViewById(R.id.btAddPerson).setOnClickListener(v -> {
            toolbar.setTitle("addPerson clicked");
            new ToastUtil().showShort(getApplicationContext(), "addPerson clicked!");
        });
        findViewById(R.id.btRoomMore).setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(MainActivity.this, findViewById(R.id.btSearchContact));
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popup_contact_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {
                Toast.makeText(MainActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });

            popup.show();//showing popup menu
        });
        findViewById(R.id.btCallMore).setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(MainActivity.this, findViewById(R.id.btContact));
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popup_call_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {
                Toast.makeText(MainActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });

            popup.show();//showing popup menu
        });
        findViewById(R.id.btCreateGroupChat).setOnClickListener(view -> {
            Intent intent = new Intent(getApplication(), CreateRoomActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btWritePost).setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(MainActivity.this, findViewById(R.id.btWritePost));
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popup_timeline, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {
                Toast.makeText(MainActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });


            try {
                Field[] fields = popup.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popup);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

//            MenuPopupHelper menuHelper = new MenuPopupHelper(this,
//                    (MenuBuilder) popup.getMenu(), null);
//            menuHelper.setForceShowIcon(true);
//            menuHelper.show();
            popup.show();//showing popup menu


        });
    }

    @NonNull
    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        roomFragment = new RoomFragment();
        fragments.add(new ContactFragment());
        fragments.add(roomFragment);
        fragments.add(new TimelineFragment());
        fragments.add(new CallFragment());
        fragments.add(new MoreFragment());
        return fragments;
    }

    @NonNull
    private View getView(int icon) {
        View view1 = getLayoutInflater().inflate(R.layout.custom_tab_layout, null);
        ImageView imageView = view1.findViewById(R.id.icon);
        imageView.setImageResource(icon);
        return view1;
    }

}
