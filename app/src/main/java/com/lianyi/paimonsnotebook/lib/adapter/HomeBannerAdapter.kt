package com.lianyi.paimonsnotebook.lib.adapter

import android.view.ViewGroup
import android.widget.ImageView
import com.lianyi.paimonsnotebook.bean.home.HomeInformationBean
import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.util.loadImage
import com.lianyi.paimonsnotebook.util.show
import com.lianyi.paimonsnotebook.util.sp
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.util.BannerUtils

class HomeBannerAdapter(val list: List<HomeInformationBean.CarouselsBean>,val block:(HomeInformationBean.CarouselsBean)->Unit):BannerAdapter<HomeInformationBean.CarouselsBean,ImageAdapter.ImageHolder>(list){

    override fun onCreateHolder(p0: ViewGroup?, p1: Int): ImageAdapter.ImageHolder {
        val image =ImageView(p0!!.context)
        image.layoutParams =ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        BannerUtils.setBannerRound(image,6f)
        return ImageAdapter.ImageHolder(image)
    }

    override fun onBindView(
        p0: ImageAdapter.ImageHolder?,
        p1: HomeInformationBean.CarouselsBean?,
        p2: Int,
        p3: Int
    ) {
        loadImage(p0!!.imageView,p1!!.cover)
        p0.imageView.setOnClickListener {
            if(sp.getBoolean(AppConfig.SP_HOME_BANNER_JUMP_TO_ARTICLE,true)){
                block(p1)
            }
        }
    }

}

