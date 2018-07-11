package vn.hdu.go2jp.hduchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import vn.hdu.go2jp.hduchat.base.OnResult;
import vn.hdu.go2jp.hduchat.common.AppConst;
import vn.hdu.go2jp.hduchat.model.constant.Status;
import vn.hdu.go2jp.hduchat.model.constant.UserType;
import vn.hdu.go2jp.hduchat.model.data.Message;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;

public class ChatBoxActivity extends AppCompatActivity {

    private String title;
    private String idRoom;
    private RecyclerView lvMessage;
    private MessageAdapter adapterMessage;
    private EditText edtTextSend;
    private ImageView ivSend;
    private String uId = FirebaseAuth.getInstance().getUid();
    private final TextWatcher twSend = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!edtTextSend.getText().toString().equals("")) {
                ivSend.setImageResource(R.drawable.ic_chat_send);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 0) {
                ivSend.setImageResource(R.drawable.ic_chat_send);
            } else {
                ivSend.setImageResource(R.drawable.ic_chat_send_active);
            }
        }
    };

    private TextView tvTitle;
    private List<Message> chatMessages;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        extractBundle();

        chatMessages = new ArrayList<>();
        fakeMessages();

        lvMessage = findViewById(R.id.lvChat);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        adapterMessage = new MessageAdapter(this, chatMessages, item -> {});
        lvMessage.setLayoutManager(layoutManager);
        lvMessage.setAdapter(adapterMessage);

        edtTextSend = findViewById(R.id.edtTextSend);
        edtTextSend.addTextChangedListener(twSend);
        ivSend = findViewById(R.id.ivSend);
        ivSend.setOnClickListener(send -> {
            Message msg = new Message(uId,edtTextSend.getText().toString(), UserType.SELF, Status.SENT);
            edtTextSend.setText("");
            hideKeyboard(this);
            FireBaseUtil.getInstance().sendMessage(idRoom, msg, new OnResult<Boolean>() {
                @Override
                public void onResult(Boolean aBoolean) {
                    if (aBoolean) {
                        adapterMessage.notifyDataSetChanged();
                        lvMessage.scrollToPosition(chatMessages.size()-1);
                    }
                }
            });
        });
        tvTitle = findViewById(R.id.tvReceiver);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

        back = findViewById(R.id.back);
        back.setOnClickListener(click -> {
            this.finish();
        });
    }

    private void fakeMessages() {
        chatMessages.add(new Message("QYYMQmqKrZfkbiflH6EK34WWaTA3","はじめまして。 私たちは　Mobile-Team　です。", UserType.OTHER, Status.DELIVERED));
        chatMessages.add(new Message("QYYMQmqKrZfkbiflH6EK34WWaTA3","どうぞ。ぞろしく　おねがいします。", UserType.OTHER, Status.DELIVERED));
        FireBaseUtil.getInstance().getListMessage(idRoom, new OnResult<Message>() {
            @Override
            public void onResult(Message messages) {
                chatMessages.add(messages);
                adapterMessage.notifyDataSetChanged();
                lvMessage.scrollToPosition(chatMessages.size()-1);
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void extractBundle() {
        Intent intent = this.getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (!TextUtils.isEmpty(bundle.getString(AppConst.KEY_ROOM_ID))) {
                idRoom = bundle.getString(AppConst.KEY_ROOM_ID);
            }
            if (!TextUtils.isEmpty(bundle.getString(AppConst.KEY_ROOM_TITLE))) {
                title = bundle.getString(AppConst.KEY_ROOM_TITLE);
            }
        }
    }

}
