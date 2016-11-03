package com.hyphenate.easeui.utils;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUIConstant;
import com.hyphenate.easeui.R;


/**
 * Created by lee on 2016/11/3.
 */

public class EaseMessageTypeUtil {
    /**
     * 获取消息类型
     *
     * @param message
     * @return
     */
    private static int getKnownMessageType(EMMessage message) {
        int type = -1;
        //语音通话
        if (message.getBooleanAttribute(EaseUIConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
            return message.direct() == EMMessage.Direct.RECEIVE ? EaseUIConstant.MESSAGE_TYPE_RECEIVED_VOICE_CALL : EaseUIConstant.MESSAGE_TYPE_SENT_VOICE_CALL;
        }
        //视频通话类型
        if (message.getBooleanAttribute(EaseUIConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
            //video call
            return message.direct() == EMMessage.Direct.RECEIVE ? EaseUIConstant.MESSAGE_TYPE_RECEIVED_VIDEO_CALL : EaseUIConstant.MESSAGE_TYPE_SENT_VIDEO_CALL;
        }
        //
        if (message.getType() == EMMessage.Type.TXT) {
            if (message.getBooleanAttribute(EaseUIConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                return message.direct() == EMMessage.Direct.RECEIVE ? EaseUIConstant.MESSAGE_TYPE_RECEIVED_EXPRESSION : EaseUIConstant.MESSAGE_TYPE_SENT_EXPRESSION;
            }
            //普通文本类型
            return message.direct() == EMMessage.Direct.RECEIVE ? EaseUIConstant.MESSAGE_TYPE_RECEIVED_TXT : EaseUIConstant.MESSAGE_TYPE_SENT_TXT;
        }
        //图片类型
        if (message.getType() == EMMessage.Type.IMAGE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? EaseUIConstant.MESSAGE_TYPE_RECEIVED_IMAGE : EaseUIConstant.MESSAGE_TYPE_SENT_IMAGE;
        }
        //位置类型
        if (message.getType() == EMMessage.Type.LOCATION) {
            return message.direct() == EMMessage.Direct.RECEIVE ? EaseUIConstant.MESSAGE_TYPE_RECEIVED_LOCATION : EaseUIConstant.MESSAGE_TYPE_SENT_LOCATION;
        }
        //语音类型
        if (message.getType() == EMMessage.Type.VOICE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? EaseUIConstant.MESSAGE_TYPE_RECEIVED_VOICE : EaseUIConstant.MESSAGE_TYPE_SENT_VOICE;
        }
        //视频类型
        if (message.getType() == EMMessage.Type.VIDEO) {
            return message.direct() == EMMessage.Direct.RECEIVE ? EaseUIConstant.MESSAGE_TYPE_RECEIVED_VIDEO : EaseUIConstant.MESSAGE_TYPE_SENT_VIDEO;
        }
        //文件类型
        if (message.getType() == EMMessage.Type.FILE) {
            return message.direct() == EMMessage.Direct.RECEIVE ? EaseUIConstant.MESSAGE_TYPE_RECEIVED_FILE : EaseUIConstant.MESSAGE_TYPE_SENT_FILE;
        }
        return type;
    }

    public static int getMessageLayoutId(EMMessage emMessage) {
        int layoutId = -1;
        int type = getKnownMessageType(emMessage);
        switch (type) {
            case EaseUIConstant.MESSAGE_TYPE_SENT_TXT:
                layoutId = R.layout.ease_row_sent_message;
                break;
            case EaseUIConstant.MESSAGE_TYPE_RECEIVED_TXT:
                layoutId = R.layout.ease_row_received_message;
                break;
            case EaseUIConstant.MESSAGE_TYPE_SENT_IMAGE:
                layoutId = R.layout.ease_row_sent_picture;
                break;
            case EaseUIConstant.MESSAGE_TYPE_RECEIVED_IMAGE:
                layoutId = R.layout.ease_row_received_picture;
                break;
            case EaseUIConstant.MESSAGE_TYPE_SENT_LOCATION:
                layoutId = R.layout.ease_row_sent_location;
                break;
            case EaseUIConstant.MESSAGE_TYPE_RECEIVED_LOCATION:
                layoutId = R.layout.ease_row_received_location;
                break;
            case EaseUIConstant.MESSAGE_TYPE_SENT_VOICE:
                layoutId = R.layout.ease_row_sent_voice;
                break;
            case EaseUIConstant.MESSAGE_TYPE_RECEIVED_VOICE:
                layoutId = R.layout.ease_row_received_voice;
                break;
            case EaseUIConstant.MESSAGE_TYPE_SENT_VIDEO:
                layoutId = R.layout.ease_row_sent_video;
                break;
            case EaseUIConstant.MESSAGE_TYPE_RECEIVED_VIDEO:
                layoutId = R.layout.ease_row_received_video;
                break;
            case EaseUIConstant.MESSAGE_TYPE_SENT_FILE:
                layoutId = R.layout.ease_row_sent_file;
                break;
            case EaseUIConstant.MESSAGE_TYPE_RECEIVED_FILE:
                layoutId = R.layout.ease_row_received_file;
                break;
            case EaseUIConstant.MESSAGE_TYPE_SENT_EXPRESSION:
                layoutId = R.layout.ease_row_sent_bigexpression;
                break;
            case EaseUIConstant.MESSAGE_TYPE_RECEIVED_EXPRESSION:
                layoutId = R.layout.ease_row_received_bigexpression;
                break;
            case EaseUIConstant.MESSAGE_TYPE_SENT_VOICE_CALL:
                layoutId = R.layout.ease_row_sent_voice_call;
                break;
            case EaseUIConstant.MESSAGE_TYPE_RECEIVED_VOICE_CALL:
                layoutId = R.layout.ease_row_received_voice_call;
                break;
            case EaseUIConstant.MESSAGE_TYPE_SENT_VIDEO_CALL:
                layoutId = R.layout.ease_row_sent_video_call;
                break;
            case EaseUIConstant.MESSAGE_TYPE_RECEIVED_VIDEO_CALL:
                layoutId = R.layout.ease_row_received_video_call;
                break;
        }
        return layoutId;
    }
}
