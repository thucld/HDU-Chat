package vn.hdu.go2jp.hduchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.activity.ChatBoxActivity;
import vn.hdu.go2jp.hduchat.activity.CreateRoomActivity;
import vn.hdu.go2jp.hduchat.adapter.RoomAdapter;
import vn.hdu.go2jp.hduchat.common.AppConst;
import vn.hdu.go2jp.hduchat.model.data.Room;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;

/**
 * Where to show chat rooms.
 */
public class RoomFragment extends Fragment {

    private View llEmpty;
    private RecyclerView rvRooms;
    private Button btnNewChat;
    private List<Room> listRoom = new ArrayList<>();
    private List<String> listIdRoom = new ArrayList<>();
    private RoomAdapter roomAdapter;

    public RoomFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_room, container, false);
        initView(view);
        setupEvents();
        getRooms();
        return view;
    }

    private void initView(View view) {
        llEmpty = view.findViewById(R.id.llEmpty);
        btnNewChat = view.findViewById(R.id.btnNewChat);
        rvRooms = view.findViewById(R.id.rvRooms);

        roomAdapter = new RoomAdapter(getContext(), listRoom, roomChat -> {
            Intent intent = new Intent(getContext(), ChatBoxActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(AppConst.KEY_ROOM_ID, roomChat.getRoomId());
            bundle.putString(AppConst.KEY_ROOM_TITLE, roomChat.getTitle());
            intent.putExtras(bundle);
            startActivity(intent);
        });
        //RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setStackFromEnd(true);
        rvRooms.setLayoutManager(layoutManager);
        rvRooms.setAdapter(roomAdapter);
    }

    private void getRooms() {
        FireBaseUtil.getInstance().getListRoom(rooms -> {
            String roomId = rooms.getRoomId();
            if (listIdRoom.contains(roomId)) {
                int indexRoom = listIdRoom.indexOf(roomId);
                listIdRoom.remove(indexRoom);
                listRoom.remove(indexRoom);
            }
            listIdRoom.add(roomId);
            listRoom.add(rooms);
            roomAdapter.notifyDataSetChanged();

            llEmpty.setVisibility(listRoom.isEmpty() ? View.VISIBLE : View.GONE);
            rvRooms.setVisibility(listRoom.isEmpty() ? View.GONE : View.VISIBLE);
        });
    }

    private void setupEvents() {
        btnNewChat.setOnClickListener(btnNewChat -> {
            Intent intent = new Intent(this.getContext(), CreateRoomActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean isEmpty = listRoom.isEmpty();
        if (llEmpty != null && rvRooms != null) {
            llEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            rvRooms.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }
    }
}
