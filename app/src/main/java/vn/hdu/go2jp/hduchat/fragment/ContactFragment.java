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
import vn.hdu.go2jp.hduchat.listener.OnResult;
import vn.hdu.go2jp.hduchat.model.data.User;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;
import vn.hdu.go2jp.hduchat.widget.UserDialog;

/**
 * Where to show contacts.
 */
public class ContactFragment extends Fragment {
    private RecyclerView contactsRecycler;
    private ContactListAdapter contactListAdapter;
    public List<User> userList = new ArrayList<>();

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        initView(view);
        setupEvent();
        getContacts();

        return view;
    }

    private void initView(View view){
        contactsRecycler = view.findViewById(R.id.rv_contact);

        contactListAdapter = new ContactListAdapter(getContext(), userList
                , item -> new UserDialog().showDialog(getActivity(), item));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        contactsRecycler.setLayoutManager(layoutManager);
        contactsRecycler.setAdapter(contactListAdapter);
    }

    private void setupEvent(){

    }

    private void getContacts(){
        FireBaseUtil.getInstance().getThisUser(user -> {
            userList.add(user);
            FireBaseUtil.getInstance().getListContact(new OnResult<User>() {
                @Override
                public void onResult(User user) {
                    userList.add(user);
                    contactListAdapter.notifyDataSetChanged();
                }
            });
        });
    }

}
