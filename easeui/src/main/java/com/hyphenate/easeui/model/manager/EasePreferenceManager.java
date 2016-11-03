package com.hyphenate.easeui.model.manager;

import android.annotation.SuppressLint;
import android.content.Context;

import com.hyphenate.easeui.utils.SPUtils;

import java.util.Set;

public class EasePreferenceManager {
    private static final String KEY_AT_GROUPS = "AT_GROUPS";

    private Context context;
    
    @SuppressLint("CommitPrefEdits")
    private EasePreferenceManager(){
//        context = EaseUI.getInstance().getContext();
    }
    private static EasePreferenceManager instance;
    
    public synchronized static EasePreferenceManager getInstance(){
        if(instance == null){
            instance = new EasePreferenceManager();
        }
        return instance;
    }

    public void init(Context context){
        this.context = context;
    }
    
    
    public void setAtMeGroups(Set<String> groups) {
        SPUtils.clear(context,KEY_AT_GROUPS);
        SPUtils.putSetString(context,KEY_AT_GROUPS,groups);
    }
    
    public Set<String> getAtMeGroups(){
        return SPUtils.getSetString(context,KEY_AT_GROUPS);
    }
    
}
