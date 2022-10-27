package com.mnowo.offlineschoolmanager.core.feature_core.domain.util

import android.app.Activity
import android.content.Context
import com.google.android.play.core.review.ReviewManagerFactory
import com.mnowo.offlineschoolmanager.core.feature_core.presentation.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext

object ReviewService : Activity() {

    fun askForReview() {
        val manager = ReviewManagerFactory.create(this)
        manager.requestReviewFlow().addOnCompleteListener { request ->
            if (request.isSuccessful) {
                val reviewInfo = request.result
                manager.launchReviewFlow(this, reviewInfo).addOnFailureListener {
                }.addOnCompleteListener { _ ->
                }
            }
        }
    }
}