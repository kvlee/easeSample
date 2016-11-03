package com.hyphenate.easeui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by lee on 2016/10/28.
 */

public abstract class BaseActivity extends SupportActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }

    /**
     * 设置 layout
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化views
     */
    public abstract void initViews();
}
