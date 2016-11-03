package com.hyphenate.easeui.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonRecyclerAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.model.domain.EaseUser;
import com.hyphenate.easeui.ui.imageload.GlideImageLoad;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.util.DateUtils;

import java.util.Date;
import java.util.List;

import static android.R.transition.move;


/**
 * Created by lee on 2016/10/31.
 */

public class EaseMessageAdapter extends CommonRecyclerAdapter<EMMessage> {


    /**
     * 设置item通用部分 事件接口回调
     */
    private MessageListItemCommonClickListener messageListItemCommonClickListener;
    /**
     * 不同类型的Item操作工具接口实现
     */
    private MultiTypeMessageItemHelper multiTypeMessageItemHelper;

    /**
     *
     * @param context
     * @param data
     */
    public EaseMessageAdapter(Context context, List<EMMessage> data) {
        this(context, -1, data, null,null);
    }

    /**
     *
     * @param context
     * @param data
     * @param messageListItemCommonClickListener
     * @param multiTypeMessageItemHelper
     */
    public EaseMessageAdapter(Context context, List<EMMessage> data, MessageListItemCommonClickListener messageListItemCommonClickListener,MultiTypeMessageItemHelper multiTypeMessageItemHelper) {
        this(context, -1, data, messageListItemCommonClickListener,multiTypeMessageItemHelper);
    }

    private EaseMessageAdapter(Context context, int layoutResId, List<EMMessage> data,MessageListItemCommonClickListener messageListItemCommonClickListener, MultiTypeMessageItemHelper multiTypeMessageItemHelper) {
        super(context, layoutResId, data);
        this.messageListItemCommonClickListener = messageListItemCommonClickListener;
        this.multiTypeMessageItemHelper = multiTypeMessageItemHelper;
    }


    @Override
    public int getLayoutResId(EMMessage item, int position) {
        int layoutId = -1;
        if (multiTypeMessageItemHelper != null) {
            layoutId = multiTypeMessageItemHelper.getMessageLayoutId(item);
        }
        return layoutId;
    }


    @Override
    public void onUpdate(BaseAdapterHelper helper, EMMessage item, int position) {
        onCommonUpdate(helper, item, position);
        if (multiTypeMessageItemHelper != null) {
            multiTypeMessageItemHelper.onMessageUpdate(helper, item, position);
        }
    }



    /**
     * 消息item公共部分显示及事件相应
     */
    private void onCommonUpdate(BaseAdapterHelper helper, final EMMessage item, int position) {
        final EaseUser easeUser = EaseUserUtils.getUserInfo(item.getFrom());
        //
        helper.setImageLoad(new GlideImageLoad());
        //  tv 消息时间戳
        int tvTimestampId = R.id.timestamp;
        if (tvTimestampId != -1) {
            if (position == 0) {
                helper.setText(tvTimestampId, DateUtils.getTimestampString(new Date(item.getMsgTime()))).setVisible(tvTimestampId, true);
            } else {
                // show time stamp if interval with last message is > 30 seconds
                EMMessage prevMessage = getItem(position - 1);
                if (prevMessage != null && DateUtils.isCloseEnough(item.getMsgTime(), prevMessage.getMsgTime())) {
                    helper.setVisible(tvTimestampId, false);
                } else {
                    helper.setText(tvTimestampId, DateUtils.getTimestampString(new Date(item.getMsgTime()))).setVisible(tvTimestampId, true);
                }
            }
        }
        // ivAvatar 头像
        int ivAvatarId = R.id.iv_userhead;
        if (ivAvatarId != -1) {
            helper.setImageUrl(ivAvatarId, easeUser.getAvatar());
            helper.setOnClickListener(ivAvatarId, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (messageListItemCommonClickListener != null) {
                        messageListItemCommonClickListener.onUserAvatarClick(easeUser.getUserName());
                    }
                }
            });
            helper.setOnLongClickListener(ivAvatarId, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (messageListItemCommonClickListener != null) {
                        messageListItemCommonClickListener.onUserAvatarLongClick(easeUser.getUserName());
                    }
                    return false;
                }
            });

        }
        // bubble 消息实体布局
        int viewMessageLayoutId = R.id.bubble;
        if (viewMessageLayoutId != -1) {
            helper.setOnClickListener(viewMessageLayoutId, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (messageListItemCommonClickListener != null) {
                        messageListItemCommonClickListener.onBubbleClick(item);
                    }
                }
            });
            helper.setOnLongClickListener(viewMessageLayoutId, new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (messageListItemCommonClickListener != null) {
                        messageListItemCommonClickListener.onBubbleLongClick(item);
                    }
                    return false;
                }
            });
        }
        int tvUerNickId = R.id.tv_userid;
        if (tvUerNickId != -1) {
            helper.setText(tvUerNickId, easeUser.getNick());
        }
//            int progressBarId = R.id.progress_bar;
//            if(progressBarId!=-1){
        //在此处不做处理，progress_bar 是用户发送消息是操作的
//            }
        int ivStatusBarId = R.id.msg_status;
        if (ivStatusBarId != -1) {
            helper.setOnClickListener(ivStatusBarId, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (messageListItemCommonClickListener != null) {
                        messageListItemCommonClickListener.onResendClick(item);
                    }
                }
            });
        }
        int tvAckId = R.id.tv_ack;
        if (tvAckId != -1) {
            helper.setVisible(tvAckId, !item.isAcked());
        }
        int tvDelivered = R.id.tv_delivered;
        if (tvDelivered != -1) {
            helper.setVisible(tvDelivered, item.isDelivered());
        }

    }



    /**
     * 设置item通用部分 事件接口 如果不设置，不报错，就没有响应时间
     *
     * @param messageListItemCommonClickListener
     */
    public void setMessageListItemClickListener(MessageListItemCommonClickListener messageListItemCommonClickListener) {
        this.messageListItemCommonClickListener = messageListItemCommonClickListener;
    }

    /**
     * 消息item 通用部分实现接口
     */

    public interface MessageListItemCommonClickListener {
        /**
         * 点击item重新发送按钮
         *
         * @param message
         */

        void onResendClick(EMMessage message);

        /**
         * 点击消息内容动作
         *
         * @param message
         * @return
         */
        boolean onBubbleClick(EMMessage message);

        /**
         * 长按消息内容
         *
         * @param message
         */
        void onBubbleLongClick(EMMessage message);

        /**
         * @param username
         */
        void onUserAvatarClick(String username);

        /**
         * 长按头像动作
         *
         * @param username
         */
        void onUserAvatarLongClick(String username);
    }

    /**
     * 设置不同类型的Item操作工具接口 实现
     * @param messageItemHelper
     */
    public void setMessageItemHelper(MultiTypeMessageItemHelper messageItemHelper) {
        this.multiTypeMessageItemHelper = messageItemHelper;
    }

    /**
     * 获取不同消息类型设置不同item布局显示的接口需要实现
     */
    public interface MultiTypeMessageItemHelper {
        public int getMessageLayoutId(EMMessage emMessage);

        public void onMessageUpdate(BaseAdapterHelper helper, EMMessage emMessage, int position);
    }
}
