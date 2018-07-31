package vn.hdu.go2jp.hduchat.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.LinearLayout;
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
    private ImageView collapsed;
    private LinearLayout expended;
    private EditText edtTextSend;
    private ImageView btnSend;
    private final TextWatcher twSend = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            expend(false);
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
    private SwipeRefreshLayout swipeRefreshMessage;
    private ImageButton btnBack;
    private RecyclerView rvMessage;
    private MessageAdapter adapterMessage;
    private List<Message> chatMessages = new ArrayList<>();
    private String uId = FirebaseAuth.getInstance().getUid();
    private String title;
    private String idRoom;

    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void expend(boolean expend) {
        if (expend) {
            expended.setVisibility(View.VISIBLE);
            collapsed.setVisibility(View.GONE);
        } else {
            expended.setVisibility(View.GONE);
            collapsed.setVisibility(View.VISIBLE);
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
        expended = findViewById(R.id.expended);
        collapsed = findViewById(R.id.collapsed);

        edtTextSend = findViewById(R.id.edtTextSend);
        TextView tvTitle = findViewById(R.id.tvReceiver);
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        btnBack = findViewById(R.id.btnBack);
        btnSend = findViewById(R.id.ivSend);
        swipeRefreshMessage = findViewById(R.id.swipeRefreshMessage);
        rvMessage = findViewById(R.id.rvChat);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapterMessage = new MessageAdapter(this, chatMessages, item -> {
        });
        rvMessage.setLayoutManager(layoutManager);
        rvMessage.setAdapter(adapterMessage);
    }

    private void setupEvents() {
        btnBack.setOnClickListener(click -> this.finish());
        edtTextSend.setOnClickListener(click -> expend(false));
        edtTextSend.addTextChangedListener(twSend);
        collapsed.setOnClickListener(click -> expend(true));
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
        swipeRefreshMessage.setOnRefreshListener(() -> {
            //TODO load more message > end end refresh swipeRefreshMessage
            Long oldest = (long) chatMessages.get(0).getTimestamp();
            FireBaseUtil.getInstance().getMoreMessage(idRoom, oldest, messages -> {
                chatMessages.addAll(0, messages);
                swipeRefreshMessage.setRefreshing(false);
                adapterMessage.notifyDataSetChanged();
            });
        });
    }

    private void getData() {
        FireBaseUtil.getInstance().getListMessage(idRoom, messages -> {
            chatMessages.add(messages);
            adapterMessage.notifyDataSetChanged();
            rvMessage.scrollToPosition(chatMessages.size() - 1);
        });
    }
}
