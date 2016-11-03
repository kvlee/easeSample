package com.hyphenate.easeui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.model.domain.EaseEmojicon;
import com.hyphenate.easeui.model.domain.EaseUser;
import com.hyphenate.easeui.model.manager.EaseAtMessageHelper;
import com.hyphenate.easeui.model.manager.EaseNotificationManager;
import com.hyphenate.easeui.model.manager.EasePreferenceManager;
import com.hyphenate.util.EMLog;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lee on 2016/11/1.
 */

public class EaseUIHelper {

    private static EaseUIHelper instance;

    private EaseUIHelper (){}

    public static EaseUIHelper getInstance(){
        if(instance==null){
            instance = new EaseUIHelper();
        }
        return instance;
    }

    /**
     * 初始化
     * @param context
     * @param options
     * @param easeUserInfoProvider
     * @param easeSettingsProvider
     * @param easeEmojiconInfoProvider
     */
    private void init(Context context,EMOptions options,EaseUserInfoProvider easeUserInfoProvider,EaseSettingsProvider easeSettingsProvider,EaseEmojiconInfoProvider easeEmojiconInfoProvider){
        if(isInit){
            return;
        }
        this.context = context;

        //初始化环信
        initEase(context,options);
        // 注册消息监听器
        registerMessageListener();
        //init notification manager
        EaseNotificationManager.getInstance().init(context);
        //初始化 EasePreferenceManager
        EasePreferenceManager.getInstance().init(context);
        //设置用户提供者
        this.easeUserInfoProvider = easeUserInfoProvider;
        //设置消息设置
        this.easeSettingsProvider = easeSettingsProvider;
        //设置表情集合提供者
        this.easeEmojiconInfoProvider = easeEmojiconInfoProvider;
        //
        isInit = true;
    }

    /**
     * 初始化
     * @param context
     * @param options
     */
    private void init(Context context,EMOptions options){
        if(isInit){
            return;
        }
        this.context = context;
        //初始化环信
        initEase(context,options);
        // 注册消息监听器
        registerMessageListener();
        //init notification manager
        EaseNotificationManager.getInstance().init(context);
        //初始化 EasePreferenceManager
        EasePreferenceManager.getInstance().init(context);
        //
        isInit = true;
    }

    private static final String TAG = EaseUIHelper.class.getSimpleName();
    // Context
    private Context context;
    // is inited
    private boolean isInit = false;
    // get user info
    private EaseUserInfoProvider easeUserInfoProvider;
    // 消息默认设置
    private EaseSettingsProvider easeSettingsProvider;
    //表情集合
    private EaseEmojiconInfoProvider easeEmojiconInfoProvider;


    /**
     * get // notification manager
     * @return
     */
//    public EaseNotificationManager getNotificationManager(){
//        return EaseNotificationManager.getInstance();
//    }
    /**
     * 获取用户数据
     */
    public EaseUser getEaseUser(String userName){
        EaseUser user = null;
        if(easeUserInfoProvider!=null){
            user = easeUserInfoProvider.getEaseUser(userName);
        }
        return user;
    }

    /**
     * 获取用户提供者
     * @return
     */
    public EaseUserInfoProvider getEaseUserInfoProvider() {
        if(easeUserInfoProvider==null){
            throw new RuntimeException("init ease ui first");
        }
        return easeUserInfoProvider;
    }

    /**
     * 设置用户提供者
     * @param easeUserInfoProvider
     */
    public void setEaseUserInfoProvider(EaseUserInfoProvider easeUserInfoProvider) {
        this.easeUserInfoProvider = easeUserInfoProvider;
    }

    /**
     * 用户信息提供者接口
     */
    public interface EaseUserInfoProvider{
        public EaseUser getEaseUser(String userName);
    }

    /**
     * 获取消息设置提供者
     * @return
     */
    public EaseSettingsProvider getEaseSettingsProvider() {
        if(easeSettingsProvider==null){
            easeSettingsProvider =new DefaultSettingsProvider();
        }
        return easeSettingsProvider;
    }
    /**
     * 设置消息设置提供者
     */
    public void setEaseSettingsProvider(EaseSettingsProvider easeSettingsProvider) {
        this.easeSettingsProvider = easeSettingsProvider;
    }
    /**
     * 获取表情集合提供者
     * @return
     */
    public EaseEmojiconInfoProvider getEaseEmojiconInfoProvider() {
        return easeEmojiconInfoProvider;
    }

    /**
     * 设置表情结婚提供者
     * @param easeEmojiconInfoProvider
     */
    public void setEaseEmojiconInfoProvider(EaseEmojiconInfoProvider easeEmojiconInfoProvider) {
        this.easeEmojiconInfoProvider = easeEmojiconInfoProvider;
    }

    /**
     * new message options provider
     *
     */
    public interface EaseSettingsProvider {
        boolean isMsgNotifyAllowed(EMMessage message);
        boolean isMsgSoundAllowed(EMMessage message);
        boolean isMsgVibrateAllowed(EMMessage message);
        boolean isSpeakerOpened();
    }
    /**
     * default settings provider
     *
     */
    protected class DefaultSettingsProvider implements EaseSettingsProvider {

        @Override
        public boolean isMsgNotifyAllowed(EMMessage message) {
            return true;
        }

        @Override
        public boolean isMsgSoundAllowed(EMMessage message) {
            return true;
        }

        @Override
        public boolean isMsgVibrateAllowed(EMMessage message) {
            return true;
        }

        @Override
        public boolean isSpeakerOpened() {
            return true;
        }
    }

    /**
     * 表情集合提供者
     *
     */
    public interface EaseEmojiconInfoProvider {
        /**
         * return EaseEmojicon for input emojiconIdentityCode
         * @param emojiconIdentityCode
         * @return
         */
        EaseEmojicon getEmojiconInfo(String emojiconIdentityCode);

        /**
         * get Emojicon map, key is the text of emoji, value is the resource id or local path of emoji icon(can't be URL on internet)
         * @return
         */
        Map<String, Object> getTextEmojiconMapping();
    }

    /**
     * 注册消息监听器  用于EaseAtMessageHelper
     */
    private void registerMessageListener() {
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                EaseAtMessageHelper.getInstance().parseMessages(messages);
            }
            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {

            }
            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> messages) {
            }
            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {

            }
        });
    }

    /**
     * 初始化 EMOptions
     * @return
     */
    protected EMOptions initChatOptions(){
        EMLog.d(TAG, "init HuanXin Options");

        EMOptions options = new EMOptions();
        // change to need confirm contact invitation
        options.setAcceptInvitationAlways(false);
        // set if need read ack
        options.setRequireAck(true);
        // set if need delivery ack
        options.setRequireDeliveryAck(false);

        return options;
    }

    /**
     * 获取应用名称 初始化环信sdk使用
     * check the application process name if process name is not qualified, then we think it is a service process and we will not init SDK
     * @param pID
     * @return
     */
    private String getAppName(Context context,int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * 初始化环信
     * @param context
     * @param options
     */
    private void initEase(Context context,EMOptions options){
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(context,pid);
        EMLog.d(TAG, "process app name : " + processAppName);
        if (processAppName == null || !processAppName.equalsIgnoreCase(context.getPackageName())) {
            EMLog.e(TAG, "enter the service process!");
        }
        if(options == null){
            EMClient.getInstance().init(context, initChatOptions());
        }else{
            EMClient.getInstance().init(context, options);
        }
    }

    /**
     * 获取是否初始化
     * @return
     */
    public boolean isSdkInit(){
        return isInit;
    }
    /**
     * 获取Context
     */
    public Context getContext(){
        return context;
    }
}
