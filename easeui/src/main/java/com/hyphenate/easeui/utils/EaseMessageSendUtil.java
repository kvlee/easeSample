package com.hyphenate.easeui.utils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUIConstant;
import com.hyphenate.easeui.model.manager.EaseAtMessageHelper;
import com.hyphenate.util.EMLog;



/**
 * Created by lee on 2016/11/1.
 */

public class EaseMessageSendUtil {

    private static final String TAG = EaseMessageSendUtil.class.getSimpleName();

    /**
     * 发送文本消息
     * @param chatType
     * @param content
     * @param toChatUsername
     */
    public static void sendTextMessage(int chatType,String content,String toChatUsername) {
        if(EaseAtMessageHelper.getInstance().containsAtUsername(content)){
            sendAtMessage(chatType,content,toChatUsername);
        }else{
            EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
            sendMessage(chatType,message);
        }
    }

    /**
     * 发送@消息， 仅限群组消息
     * @param chatType
     * @param content
     * @param toChatUsername
     */
    @SuppressWarnings("ConstantConditions")
    private static void sendAtMessage(int chatType,String content,String toChatUsername){
        if(chatType != EaseUIConstant.CHATTYPE_GROUP){
            EMLog.e(TAG, "only support group chat message");
            return;
        }
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
        if(EMClient.getInstance().getCurrentUser().equals(group.getOwner()) && EaseAtMessageHelper.getInstance().containsAtAll(content)){
            message.setAttribute(EaseUIConstant.MESSAGE_ATTR_AT_MSG, EaseUIConstant.MESSAGE_ATTR_VALUE_AT_MSG_ALL);
        }else {
            message.setAttribute(EaseUIConstant.MESSAGE_ATTR_AT_MSG,EaseAtMessageHelper.getInstance().atListToJsonArray(EaseAtMessageHelper.getInstance().getAtMessageUsernames(content)));
        }
        sendMessage(chatType,message);

    }

    /**
     * 发送大表情消息
     * @param chatType
     * @param name
     * @param identityCode
     * @param toChatUsername
     */
    public static void sendBigExpressionMessage(int chatType,String name, String identityCode,String toChatUsername){
        EMMessage message = EaseCommonUtils.createExpressionMessage(toChatUsername, name, identityCode);
        sendMessage(chatType,message);
    }

    /**
     * 发送语音文件消息
     * @param chatType
     * @param filePath
     * @param length
     * @param toChatUsername
     */
    public static void sendVoiceMessage(int chatType,String filePath, int length,String toChatUsername) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        sendMessage(chatType,message);
    }

    /**
     * 发送图片消息
     * @param chatType
     * @param imagePath
     * @param toChatUsername
     */
    public static void sendImageMessage(int chatType,String imagePath,String toChatUsername) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        sendMessage(chatType,message);
    }

    /**
     * 发送位置消息
     * @param chatType
     * @param latitude
     * @param longitude
     * @param locationAddress
     * @param toChatUsername
     */
    public static void sendLocationMessage(int chatType,double latitude, double longitude, String locationAddress,String toChatUsername) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        sendMessage(chatType,message);
    }

    /**
     * 发送视频文件消息
     * @param chatType
     * @param videoPath
     * @param thumbPath
     * @param videoLength
     * @param toChatUsername
     */
    public static void sendVideoMessage(int chatType,String videoPath, String thumbPath, int videoLength,String toChatUsername) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        sendMessage(chatType,message);
    }

    /**
     * 发送文件消息
     * @param chatType
     * @param filePath
     * @param toChatUsername
     */
    public static void sendFileMessage(int chatType,String filePath,String toChatUsername) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        sendMessage(chatType,message);
    }
    /**
     * 发送消息
     * @param chatType
     * @param message
     */
    private static void sendMessage(int chatType,EMMessage message){
        if (message == null) {
            return;
        }
        switch (chatType){
            case EaseUIConstant.CHATTYPE_GROUP:
                message.setChatType(EMMessage.ChatType.GroupChat);
                break;
            case EaseUIConstant.CHATTYPE_CHATROOM:
                message.setChatType(EMMessage.ChatType.ChatRoom);
                break;
            case EaseUIConstant.CHATTYPE_SINGLE:
                message.setChatType(EMMessage.ChatType.Chat);
                break;
        }
        //send message
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    /**
     * 重新发送消息
     * @param message
     */
    public static void resendMessage(EMMessage message){
        message.setStatus(EMMessage.Status.CREATE);
        EMClient.getInstance().chatManager().sendMessage(message);
    }
}
