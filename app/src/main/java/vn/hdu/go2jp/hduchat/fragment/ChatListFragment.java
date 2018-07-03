package vn.hdu.go2jp.hduchat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.adapter.ChatListAdapter;
import vn.hdu.go2jp.hduchat.data.models.RoomChat;

/**
 * Where to show chat rooms.
 */
public class ChatListFragment extends Fragment {
    private RecyclerView chatsRecyclerView;
    private ChatListAdapter chatListAdapter;
    private List<RoomChat> listRoom;

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        for (int i = 0; i < 9; i++) {
            RoomChat user = new RoomChat();
            user.setTitle("Room " + i);
            listRoom.add(user);
        }
        chatsRecyclerView = view.findViewById(R.id.rv_chat_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        chatListAdapter = new ChatListAdapter(getContext(), listRoom
                , new ChatListAdapter.PostItemListener() {
            @Override
            public void onPostClick(RoomChat item) {

            }
        });
        chatsRecyclerView.setLayoutManager(layoutManager);
        chatsRecyclerView.setAdapter(chatListAdapter);
        return view;
    }

}
