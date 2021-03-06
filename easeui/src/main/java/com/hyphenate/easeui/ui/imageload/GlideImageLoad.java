package com.hyphenate.easeui.ui.imageload;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.classic.adapter.interfaces.ImageLoad;
import com.hyphenate.easeui.R;

public class GlideImageLoad implements ImageLoad {

    @Override public void load(Context context, ImageView imageView, String imageUrl) {
        if(TextUtils.isEmpty(imageUrl)) return;
        Glide.with(context).load(imageUrl).centerCrop().crossFade().placeholder(R.drawable.ease_default_avatar).into(imageView);
    }
}
