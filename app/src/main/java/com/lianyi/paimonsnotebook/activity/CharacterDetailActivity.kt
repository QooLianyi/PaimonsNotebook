package com.lianyi.paimonsnotebook.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.EntityJsonBean
import com.lianyi.paimonsnotebook.config.CharacterProperty
import com.lianyi.paimonsnotebook.config.WeaponType
import com.lianyi.paimonsnotebook.databinding.ActivityCharacterDetailBinding
import com.lianyi.paimonsnotebook.databinding.ItemMaterialsBinding
import com.lianyi.paimonsnotebook.databinding.ItemPropertyBinding
import com.lianyi.paimonsnotebook.util.ReAdapter
import com.lianyi.paimonsnotebook.util.loadImage

class CharacterDetailActivity : AppCompatActivity() {

    companion object{
        lateinit var detailInformation:EntityJsonBean
    }

    lateinit var bind: ActivityCharacterDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        bind = ActivityCharacterDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)

        with(bind){
            area.text = detailInformation.area
            characterProperty.text = CharacterProperty.getNameByType(detailInformation.entityType)
            equipType.text = WeaponType.getNameByType(detailInformation.equipType)
            loadImage(icon, detailInformation.entity.iconUrl)
            name.text = detailInformation.entity.name

            propertyList.adapter = ReAdapter(detailInformation.propertyList,R.layout.item_property){
                view, s, position ->
                val item = ItemPropertyBinding.bind(view)
                val values = s.split(":")
                item.value.text = values.last().trim()
                item.name.text = if(position== detailInformation.propertyList.size-1){
                    "${values.first()}(突破属性)"
                }else{
                    values.first()
                }
            }

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