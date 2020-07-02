package com.willy.metu.component

import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import androidx.annotation.RequiresApi
import com.willy.metu.MeTuApplication
import com.willy.metu.R

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class ProfileAvatarOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        view.clipToOutline = true
        val radius = MeTuApplication.instance.resources.getDimensionPixelSize(R.dimen.radius_profile_avatar)
        outline.setOval(0, 0, radius, radius)
    }
}