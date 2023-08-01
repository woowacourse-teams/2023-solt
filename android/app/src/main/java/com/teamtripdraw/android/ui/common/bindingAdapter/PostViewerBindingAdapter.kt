package com.teamtripdraw.android.ui.common.bindingAdapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.teamtripdraw.android.R

@BindingAdapter("app:setThumbnail")
fun ImageView.setImgUrl(imgUrl: String?) {
    if (imgUrl == null) {
        this.visibility = View.GONE
        return
    }

    Glide.with(this.context)
        .load(imgUrl)
        .placeholder(R.drawable.shape_td_gray_fill_0_rect)
        .error(R.drawable.shape_td_gray_fill_0_rect)
        .apply(RequestOptions.bitmapTransform(RoundedCorners(12)))
        .into(this)
}
