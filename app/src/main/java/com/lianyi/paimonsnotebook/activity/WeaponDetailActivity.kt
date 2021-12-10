package com.lianyi.paimonsnotebook.activity

import android.os.Bundle
import android.view.WindowManager
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.base.BaseActivity
import com.lianyi.paimonsnotebook.bean.EntityJsonBean
import com.lianyi.paimonsnotebook.config.WeaponType
import com.lianyi.paimonsnotebook.databinding.ActivityWeaponDetailBinding
import com.lianyi.paimonsnotebook.databinding.ItemMaterialsBinding
import com.lianyi.paimonsnotebook.util.ReAdapter
import com.lianyi.paimonsnotebook.util.loadImage

class WeaponDetailActivity : BaseActivity() {
    companion object{
        lateinit var detailInformation:EntityJsonBean
    }
    lateinit var bind:ActivityWeaponDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        bind = ActivityWeaponDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)

        with(bind){
            loadImage(icon, detailInformation.entity.iconUrl)
            name.text = detailInformation.entity.name
            subName.text = detailInformation.entity.name
            weaponType.text = WeaponType.getNameByType(detailInformation.entityType)
            star.text = when(detailInformation.star){
                4->"★★★★"
                5->"★★★★★"
                else->"★"
            }
            mainProperty.text = detailInformation.mainProperty
            attack.text = detailInformation.propertyList.first()
            effectName.text = detailInformation.effectName
            effect.text = detailInformation.effect
            story.text = detailInformation.story

            materialsList.adapter = ReAdapter(detailInformation.materialsList,R.layout.item_materials){
                    view, pair, position ->
                val item = ItemMaterialsBinding.bind(view)
                item.name.text = pair.first
                loadImage(item.icon,pair.second)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out)
    }

}