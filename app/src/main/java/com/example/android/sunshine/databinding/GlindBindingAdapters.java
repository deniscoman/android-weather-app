package com.example.android.sunshine.databinding;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class GlindBindingAdapters {

    @BindingAdapter("imageResource")
    public static void setImageResource(ImageView view, int imageUrl){

        Context context = view.getContext();
        RequestOptions option = new RequestOptions();

        Glide.with(context).setDefaultRequestOptions(option).load(imageUrl).into(view);
    }
}
