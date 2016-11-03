package com.hyphenate.easeui.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseUIConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.model.domain.EaseUser;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.HanziToPinyin;
import com.hyphenate.util.HanziToPinyin.Token;

import java.util.ArrayList;
import java.util.List;


public class EaseCommonUtils {
	private static final String TAG = EaseCommonUtils.class.getSimpleName();
	/**
	 * 检查网络可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable() && mNetworkInfo.isConnected();
			}
		}

		return false;
	}

	/**
	 *检查sdk 是否存在
	 * 
	 * @return
	 */
	public static boolean isSdcardExist() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}
	
	public static EMMessage createExpressionMessage(String toChatUsername, String expressioName, String identityCode){
	    EMMessage message = EMMessage.createTxtSendMessage("["+expressioName+"]", toChatUsername);
        if(identityCode != null){
            message.setAttribute(EaseUIConstant.MESSAGE_ATTR_EXPRESSION_ID, identityCode);
        }
        message.setAttribute(EaseUIConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, true);
        return message;
	}

	/**
     * 获取消息描述
     * 
     * @param message
     * @param context
     * @return
     */
    public static String getMessageDigest(EMMessage message, Context context) {
        String digest = "";
        switch (message.getType()) {
        case LOCATION:
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                digest = getString(context, R.string.location_recv);
                digest = String.format(digest, message.getFrom());
                return digest;
            } else {
                digest = getString(context, R.string.location_prefix);
            }
            break;
        case IMAGE:
            digest = getString(context, R.string.picture);
            break;
        case VOICE:
            digest = getString(context, R.string.voice_prefix);
            break;
        case VIDEO:
            digest = getString(context, R.string.video);
            break;
        case TXT:
            EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
            if(message.getBooleanAttribute(EaseUIConstant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
                digest = getString(context, R.string.voice_call) + txtBody.getMessage();
            }else if(message.getBooleanAttribute(EaseUIConstant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
                digest = getString(context, R.string.video_call) + txtBody.getMessage();
            }else if(message.getBooleanAttribute(EaseUIConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
                if(!TextUtils.isEmpty(txtBody.getMessage())){
                    digest = txtBody.getMessage();
                }else{
                    digest = getString(context, R.string.dynamic_expression);
                }
            }else{
                digest = txtBody.getMessage();
            }
            break;
        case FILE:
            digest = getString(context, R.string.file);
            break;
        default:
            EMLog.e(TAG, "未知消息类型");
            return "";
        }

        return digest;
    }
    
    static String getString(Context context, int resId){
        return context.getResources().getString(resId);
    }
	
	/**
	 * get top activity
	 * @param context
	 * @return
	 */
	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}
	
	/**
     * set initial letter of according user's nickname( username if no nickname)
     *
     * @param user
     */
    public static void setUserInitialLetter(EaseUser user) {
        final String DefaultLetter = "#";
        String letter = DefaultLetter;
        
        final class GetInitialLetter {
            String getLetter(String name) {
                if (TextUtils.isEmpty(name)) {
                    return DefaultLetter;
                }
                char char0 = name.toLowerCase().charAt(0);
                if (Character.isDigit(char0)) {
                    return DefaultLetter;
                }
                ArrayList<Token> l = HanziToPinyin.getInstance().get(name.substring(0, 1));
                if (l != null && l.size() > 0 && l.get(0).target.length() > 0)
                {
                    Token token = l.get(0);
                    String letter = token.target.substring(0, 1).toUpperCase();
                    char c = letter.charAt(0);
                    if (c < 'A' || c > 'Z') {
                        return DefaultLetter;
                    }
                    return letter;
                }
                return DefaultLetter;
            }
        }
        
        if ( !TextUtils.isEmpty(user.getNick()) ) {
            letter = new GetInitialLetter().getLetter(user.getNick());
            user.setInitialLetter(letter);
            return;
        } 
        if (letter.equals(DefaultLetter) && !TextUtils.isEmpty(user.getUserName())) {
            letter = new GetInitialLetter().getLetter(user.getUserName());
        }
        user.setInitialLetter(letter);
    }
    
    /**
     * change the chat type to EMConversationType
     * 获取会话分类
     * @param chatType
     * @return
     */
    public static EMConversationType getConversationType(int chatType) {
        if (chatType == EaseUIConstant.CHATTYPE_SINGLE) {
            return EMConversationType.Chat;
        } else if (chatType == EaseUIConstant.CHATTYPE_GROUP) {
            return EMConversationType.GroupChat;
        } else {
            return EMConversationType.ChatRoom;
        }
    }

}
