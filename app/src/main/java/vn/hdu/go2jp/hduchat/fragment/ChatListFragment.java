package vn.hdu.go2jp.hduchat.fragment;

import android.content.Intent;
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
import vn.hdu.go2jp.hduchat.activity.ChatBoxActivity;
import vn.hdu.go2jp.hduchat.adapter.ChatListAdapter;
import vn.hdu.go2jp.hduchat.common.AppConst;
import vn.hdu.go2jp.hduchat.model.data.Room;

/**
 * Where to show chat rooms.
 */
public class ChatListFragment extends Fragment {

    public ChatListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        List<Room> listRoom = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            Room user = new Room();
            user.setTitle("Room " + i);
            listRoom.add(user);
        }
        RecyclerView chatsRecyclerView = view.findViewById(R.id.rv_chat_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        ChatListAdapter chatListAdapter = new ChatListAdapter(getContext(), listRoom, roomChat -> {
            Intent intent = new Intent(getContext(), ChatBoxActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(AppConst.KEY_ROOM_ID, roomChat.getRoomId());
            bundle.putString(AppConst.KEY_ROOM_TITLE, roomChat.getTitle());
            intent.putExtras(bundle);
            startActivity(intent);
        });
        chatsRecyclerView.setLayoutManager(layoutManager);
        chatsRecyclerView.setAdapter(chatListAdapter);
        return view;
    }

}
