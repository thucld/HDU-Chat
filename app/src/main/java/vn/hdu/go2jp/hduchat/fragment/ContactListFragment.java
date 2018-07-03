package vn.hdu.go2jp.hduchat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.adapter.ContactListAdapter;
import vn.hdu.go2jp.hduchat.base.OnResult;
import vn.hdu.go2jp.hduchat.data.models.User;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;

/**
 * Where to show contacts.
 */
public class ContactListFragment extends Fragment {
    private RecyclerView contactsRecycler;
    private ContactListAdapter contactListAdapter;
    private List<User> userList = new ArrayList<>();

    public ContactListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        for (int i = 0; i < 9; i++) {
//            User user = new User();
//            user.setAvatarPath("aa" + i);
//            user.setUserName("User " + i);
//            user.setNote("aa" + i);
//            userList.add(user);
//        }

        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        FireBaseUtil.getListContact(new OnResult<List<User>>() {
            @Override
            public void onResult(List<User> users) {
                userList = users;
                contactsRecycler = view.findViewById(R.id.rv_contact);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL, false);
                contactListAdapter = new ContactListAdapter(getContext(), userList
                        , new ContactListAdapter.PostItemListener() {
                    @Override
                    public void onPostClick(User item) {

                    }
                });
                contactsRecycler.setLayoutManager(layoutManager);
                contactsRecycler.setAdapter(contactListAdapter);
            }
        });

        return view;
    }

}
