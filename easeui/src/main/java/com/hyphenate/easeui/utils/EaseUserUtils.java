package com.hyphenate.easeui.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.EaseUIHelper;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.model.domain.EaseUser;


/**
 * 该类需要重新设计，获取聊天用户信息需要在用户打开相应界面之前进行
 * 场景1，在用户打开用户的会话列表时，在展现会话列表之前请求所有会话用户的用户信息
 * 场景2，在用户收到消息时，与某个用户（可以是单聊、群聊（获取群信息）陌生人）的聊天页面打开之前先获取用户信息
 * 场景3，在用户收到推送消息时，比如收到1条消息通知，点击通知即将进入聊天页面，进入聊天页面之前先获取用户信息
 * 获取用户信息设计，在展现所有会话窗口的页面展现之前，先从后天批量更新一次用户信息，并保存在本地数据库，当收到用户信息变更推送，更新用户数据并刷新页面
 */
public class EaseUserUtils {

    private static EaseUIHelper.EaseUserInfoProvider userProvider;


    /**
     * get EaseUser according username
     *
     * @param username
     * @return
     */

    public static EaseUser getUserInfo(String username) {
        if (userProvider == null) {
            userProvider = EaseUIHelper.getInstance().getEaseUserInfoProvider();
        }
        return userProvider.getEaseUser(username);
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        EaseUser user = getUserInfo(username);
        if (user != null && user.getAvatar() != null) {
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        } else {
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNick() != null) {
                textView.setText(user.getNick());
            } else {
                textView.setText(username);
            }
        }
    }


}
