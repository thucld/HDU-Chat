package vn.hdu.go2jp.hduchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.adapter.MessageAdapter;
import vn.hdu.go2jp.hduchat.common.AppConst;
import vn.hdu.go2jp.hduchat.model.constant.Status;
import vn.hdu.go2jp.hduchat.model.constant.UserType;
import vn.hdu.go2jp.hduchat.model.data.Message;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;

public class ChatBoxActivity extends AppCompatActivity {

    public static String onChatBoxRoom = null;
    private EditText edtTextSend;
    private ImageView btnSend;
    private final TextWatcher twSend = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!TextUtils.isEmpty(edtTextSend.getText())) {
                btnSend.setImageResource(R.drawable.ic_chat_send);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                btnSend.setImageResource(R.drawable.ic_chat_send);
            } else {
                btnSend.setImageResource(R.drawable.ic_chat_send_active);
            }
        }
    };
    private ImageButton btnBack;
    private RecyclerView rvMessage;
    private MessageAdapter adapterMessage;
    private List<Message> chatMessages;
    private String uId = FirebaseAuth.getInstance().getUid();
    private String title;
    private String idRoom;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);
        extractBundle();
        initViews();
        setupEvents();
        getData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        onChatBoxRoom = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        onChatBoxRoom = idRoom;
    }

    private void extractBundle() {
        Intent intent = this.getIntent();
        if (intent != null && intent.getExtras() != null) {
            Bundle bundle = intent.getExtras();
            if (!TextUtils.isEmpty(bundle.getString(AppConst.KEY_ROOM_ID))) {
                idRoom = bundle.getString(AppConst.KEY_ROOM_ID);
            }
            if (!TextUtils.isEmpty(bundle.getString(AppConst.KEY_ROOM_TITLE))) {
                title = bundle.getString(AppConst.KEY_ROOM_TITLE);
            }
        }
    }

    private void initViews() {
        edtTextSend = findViewById(R.id.edtTextSend);
        TextView tvTitle = findViewById(R.id.tvReceiver);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        btnBack = findViewById(R.id.back);
        btnSend = findViewById(R.id.ivSend);
        rvMessage = findViewById(R.id.lvChat);
        chatMessages = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapterMessage = new MessageAdapter(this, chatMessages, item -> {
        });
        rvMessage.setLayoutManager(layoutManager);
        rvMessage.setAdapter(adapterMessage);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(click -> this.finish());
        edtTextSend.addTextChangedListener(twSend);
        btnSend.setOnClickListener(send -> {
            Message msg = new Message(uId, edtTextSend.getText().toString(), UserType.SELF, Status.SENT);
            edtTextSend.setText("");
            hideKeyboard(this);
            FireBaseUtil.getInstance().sendMessage(idRoom, msg, isSuccess -> {
                if (isSuccess) {
                    adapterMessage.notifyDataSetChanged();
                    rvMessage.scrollToPosition(chatMessages.size() - 1);
                }
            });
        });
    }

    private void getData() {
        chatMessages.add(new Message("QYYMQmqKrZfkbiflH6EK34WWaTA3", "はじめまして。 私たちは　Mobile-Team　です。", UserType.OTHER, Status.DELIVERED));
        chatMessages.add(new Message("QYYMQmqKrZfkbiflH6EK34WWaTA3", "どうぞ。ぞろしく　おねがいします。", UserType.OTHER, Status.DELIVERED));
        FireBaseUtil.getInstance().getListMessage(idRoom, messages -> {
            chatMessages.add(messages);
            adapterMessage.notifyDataSetChanged();
            rvMessage.scrollToPosition(chatMessages.size() - 1);
        });
    }
}
