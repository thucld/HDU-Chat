package vn.hdu.go2jp.hduchat.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import vn.hdu.go2jp.hduchat.R;
import vn.hdu.go2jp.hduchat.adapter.MessageAdapter;
import vn.hdu.go2jp.hduchat.common.AppConst;
import vn.hdu.go2jp.hduchat.model.constant.Status;
import vn.hdu.go2jp.hduchat.model.constant.UserType;
import vn.hdu.go2jp.hduchat.model.data.Message;

public class ChatBoxActivity extends AppCompatActivity{

    private String title;
    private String idRoom;
    private ListView lvMessage;
    private MessageAdapter adapterMessage;
    private EditText edtTextSend;
    private ImageView ivSend;
    private TextView tvTitle;
    private ArrayList<Message> chatMessages;

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
        tvTitle = findViewById(R.id.tvReceiver);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }

    private void fakeMessages() {
        chatMessages.add(new Message("dfjsdk", "hajimemashite. hau desu. shitsuredesuga onamaeha.", new Date(), UserType.OTHER, Status.DELIVERED));
        chatMessages.add(new Message("dfjsdl", "hajimemashite. tuan desu.", new Date(), UserType.SELF, Status.SENT));
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
