package com.igalogs.jinlog.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.igalogs.jinlog.R

@BindingAdapter("gcsPath")
fun ImageView.loadImageWithGcs(path: String?) {
    if(path.isNullOrBlank()) {
        return
    }

    val storage = FirebaseStorage.getInstance()
    val imageRef = storage.reference.child(path)

    Glide.with(this)
        .load(imageRef)
        .into(this)
}