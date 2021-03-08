package com.igalogs.jinlog.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.igalogs.jinlog.R
import com.igalogs.jinlog.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalArgumentException

@AndroidEntryPoint
class PlaceDetailActivity : AppCompatActivity(R.layout.activity_place_detail) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {

            val placeId = intent.getStringExtra(ARG_PLACE_ID)
            if(placeId.isNullOrBlank()) {
                throw IllegalArgumentException("invalid place id.")
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PlaceDetailFragment.newInstance(placeId))
                .commit()
        }
    }

    companion object {

        private const val ARG_PLACE_ID = "place_id"

        @JvmStatic
        fun createIntent(context: Context, placeId: String) =
            Intent(context, PlaceDetailActivity::class.java).apply {
                val bundle = Bundle().apply {
                    this.putString(ARG_PLACE_ID, placeId)
                }
                this.putExtras(bundle)
            }
    }

}