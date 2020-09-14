package com.igalogs.jinlog.util

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

object ViewExt {

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun ImageView.loadImageWithUrl(url: String?) {
        if (url.isNullOrBlank()) {
            return
        }

        Glide.with(this)
            .load(url)
            .into(this)

        Log.d("image load", url)
    }

    @BindingAdapter("gcsPath")
    @JvmStatic
    fun ImageView.loadImageWithGcs(path: String?) {
        if (path.isNullOrBlank()) {
            return
        }

        val storage = FirebaseStorage.getInstance()
        val imageRef = storage.reference.child(path)

        Glide.with(this)
            .load(imageRef)
            .into(this)
    }
}


