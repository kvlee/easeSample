package com.hyphenate.easeui.model.domain;

import java.io.Serializable;

import com.hyphenate.easeui.utils.EaseCommonUtils;

/**
 * Created by lee on 2016/10/31.
 */

public class EaseUser <T> implements Serializable {
    /**
     * initial letter for nickname
     */
    private String initialLetter;
    /**
     * avatar of the user
     */
    private String avatar;
    /**
     * 环信id
     */
    private String userName;
    /**
     * 扩展信息
     */
    private T otherInfo;

    /**
     * 昵称
     */
    private String nick;


    public EaseUser(String userName) {
        this.userName = userName;
    }

    public String getInitialLetter() {
        if(initialLetter == null){
            EaseCommonUtils.setUserInitialLetter(this);
        }
        return initialLetter;
    }

    public void setInitialLetter(String initialLetter) {
        this.initialLetter = initialLetter;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public T getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(T otherInfo) {
        this.otherInfo = otherInfo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getUserName() {
        return userName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EaseUser<?> easeUser = (EaseUser<?>) o;

        return userName.equals(easeUser.userName);

    }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }

    @Override
    public String toString() {
        return nick == null ? userName : nick;
    }
}
