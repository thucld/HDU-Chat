package vn.hdu.go2jp.hduchat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.adapter.MessageAdapter;
import vn.hdu.go2jp.hduchat.base.OnResult;
import vn.hdu.go2jp.hduchat.common.AppConst;
import vn.hdu.go2jp.hduchat.model.constant.Status;
import vn.hdu.go2jp.hduchat.model.constant.UserType;
import vn.hdu.go2jp.hduchat.model.data.Message;
import vn.hdu.go2jp.hduchat.model.data.User;
import vn.hdu.go2jp.hduchat.util.FireBaseUtil;

public class ChatBoxActivity extends AppCompatActivity {

    private String title;
    private String idRoom;
    private ListView lvMessage;
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
    private ArrayList<Message> chatMessages;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_box);

        extractBundle();

        chatMessages = new ArrayList<>();
        fakeMessages();
        adapterMessage = new MessageAdapter(chatMessages, this);
        lvMessage = findViewById(R.id.lvChat);
        lvMessage.setAdapter(adapterMessage);

        edtTextSend = findViewById(R.id.edtTextSend);
        edtTextSend.addTextChangedListener(twSend);
        ivSend = findViewById(R.id.ivSend);
        ivSend.setOnClickListener(send -> {
            Message msg = new Message(uId,edtTextSend.getText().toString(), UserType.SELF, Status.SENT);
            edtTextSend.setText("");
            FireBaseUtil.getInstance().sendMessage(idRoom, msg, new OnResult<Boolean>() {
                @Override
                public void onResult(Boolean aBoolean) {
                    if (aBoolean) {
                        adapterMessage.updateData(chatMessages);
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
                adapterMessage.updateData(chatMessages);
            }
        });
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
