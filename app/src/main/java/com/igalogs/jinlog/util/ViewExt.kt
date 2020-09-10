package com.igalogs.jinlog.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

@BindingAdapter("gcsPath")
fun ImageView.loadImageWithGcs(url: String?) {
    url ?: return

    val storage = FirebaseStorage.getInstance()
    val imageRef = storage.reference.child(url)

    Glide.with(this)
        .load(imageRef)
        .into(this)
}