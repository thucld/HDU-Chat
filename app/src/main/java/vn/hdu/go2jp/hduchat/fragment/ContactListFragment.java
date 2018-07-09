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
import vn.hdu.go2jp.hduchat.model.data.User;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;
import vn.hdu.go2jp.hduchat.widget.UserDialog;

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

        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        FireBaseUtil.getInstance().getThisUser(user -> {
            userList.add(user);
            FireBaseUtil.getInstance().getListContact(users -> {
                userList.addAll(users);
                contactsRecycler = view.findViewById(R.id.rv_contact);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                        LinearLayoutManager.VERTICAL, false);
                contactListAdapter = new ContactListAdapter(getContext(), userList
                        , item -> new UserDialog().showDialog(getActivity(), item));
                contactsRecycler.setLayoutManager(layoutManager);
                contactsRecycler.setAdapter(contactListAdapter);
            });
        });


        return view;
    }

}
