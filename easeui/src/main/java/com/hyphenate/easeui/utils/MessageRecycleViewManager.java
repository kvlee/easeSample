package com.hyphenate.easeui.utils;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.adapter.EaseMessageAdapter;

import java.util.List;


/**
 * Created by lee on 2016/11/3.
 */

public class MessageRecycleViewManager {
    private static MessageRecycleViewManager instance;

    private MessageRecycleViewManager() {
    }

    public static MessageRecycleViewManager getInstance() {
        if (instance == null) {
            instance = new MessageRecycleViewManager();
        }
        return instance;
    }
    private Context context;
    private RecyclerView recycleView;
    private boolean move;
    private int mIndex = 0;
    private LinearLayoutManager layoutManager;
    //
    private String userName;
    private int chatType;
    private EaseMessageAdapter adapter;
    private List<EMMessage> messages;

    /**
     * 设置item通用部分 事件接口回调
     */
    private EaseMessageAdapter.MessageListItemCommonClickListener messageListItemCommonClickListener;
    /**
     * 不同类型的Item操作工具接口实现
     */
    private EaseMessageAdapter.MultiTypeMessageItemHelper multiTypeMessageItemHelper;


    public void init(Context context, RecyclerView recyclerView,String userName,int chatType,EaseMessageAdapter.MessageListItemCommonClickListener messageListItemCommonClickListener,EaseMessageAdapter.MultiTypeMessageItemHelper multiTypeMessageItemHelper) {
        this.context = context;
        this.recycleView = recyclerView;
        this.userName = userName;
        this.chatType = chatType;
        this.messageListItemCommonClickListener=messageListItemCommonClickListener;
        this.multiTypeMessageItemHelper=multiTypeMessageItemHelper;
        //
        messages = getMessageList(userName,chatType);
        adapter = new EaseMessageAdapter(context,messages,messageListItemCommonClickListener,multiTypeMessageItemHelper);
        //
        layoutManager = new LinearLayoutManager(context);
        recycleView.setLayoutManager(layoutManager);
        recycleView.addOnScrollListener(new RecyclerViewListener());
        recycleView.setAdapter(adapter);
    }
    /**
     * 消息列表滚动最上一行
     */
    public void refreshSeekTop() {
        refreshData();
        smoothMoveToPosition(0);
    }

    /**
     * 消息列表滚动到指定位置
     */
    public void refreshSeekTo(int position) {
        refreshData();
        smoothMoveToPosition(position);
    }

    /**
     * 滚动到最下一行
     */
    public void refreshSelectLast() {
        refreshData();
        smoothMoveToPosition(messages.size() - 1);
    }

    /**
     * 刷新所有数据 （将原来所有数据替换）
     */
    private void refreshData() {
        messages = getMessageList(userName,chatType);
        adapter.replaceAll(messages);
    }


    /**
     * 自动滚动到指定为知
     *
     * @param n
     */
    private void smoothMoveToPosition(int n) {

        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int lastItem = layoutManager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            recycleView.smoothScrollToPosition(n);
        } else if (n <= lastItem) {
            int top = recycleView.getChildAt(n - firstItem).getTop();
            recycleView.smoothScrollBy(0, top);
        } else {
            recycleView.smoothScrollToPosition(n);
            move = true;
        }

    }

    /**
     * 获取消息聊天消息数据
     *
     * @param userName
     * @param chatType
     * @return
     */
    public List<EMMessage> getMessageList(String userName, int chatType) {
        List<EMMessage> list;
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName, EaseCommonUtils.getConversationType(chatType), true);
        list = conversation.getAllMessages();
        return list;
    }

    private class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false;
                int n = mIndex - layoutManager.findFirstVisibleItemPosition();
                if (0 <= n && n < recycleView.getChildCount()) {
                    int top = recycleView.getChildAt(n).getTop();
                    recycleView.smoothScrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (move) {
                move = false;
                int n = mIndex - layoutManager.findFirstVisibleItemPosition();
                if (0 <= n && n < recycleView.getChildCount()) {
                    int top = recycleView.getChildAt(n).getTop();
                    recycleView.scrollBy(0, top);
                }
            }
        }
    }
}
