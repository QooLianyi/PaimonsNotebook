package com.lianyi.paimonsnotebook.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.lib.information.PagerIndex

class RedirectToDailyMaterialsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FragmentContainerActivity.pagerIndex = PagerIndex.DAILY_MATERIALS_PAGE

        startActivity(Intent(this,FragmentContainerActivity::class.java))
        finish()
    }
}