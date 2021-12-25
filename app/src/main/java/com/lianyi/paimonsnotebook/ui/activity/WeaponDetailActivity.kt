package com.lianyi.paimonsnotebook.ui.activity

import android.os.Bundle
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.bean.WeaponBean
import com.lianyi.paimonsnotebook.databinding.ActivityWeaponDetailBinding
import com.lianyi.paimonsnotebook.lib.information.Star
import com.lianyi.paimonsnotebook.lib.information.WeaponType
import com.lianyi.paimonsnotebook.util.loadImage

class WeaponDetailActivity : BaseActivity() {
    companion object{
        lateinit var weapon:WeaponBean
    }
    lateinit var bind: ActivityWeaponDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityWeaponDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)

        with(bind){
            loadImage(icon, weapon.icon)
            name.text = weapon.name
            subName.text = weapon.name
            weaponType.text = WeaponType.getNameByType(weapon.weaponType)
            star.text = Star.getStarSymbolByStarNum(weapon.star)

            attribute.text = weapon.attributeName
            attributeValue.text = weapon.attributeNameValue
            atk.text = weapon.ATK
            effectName.text = weapon.effectName
            effect.text = weapon.effect
            story.text = weapon.describe.trim()

            starBackground.setImageResource(Star.getStarResourcesByStarNum(weapon.star,false))

            Star.setStarBackgroundAndIcon(dailyMaterial,weapon.dailyMaterials.icon, weapon.dailyMaterials.star)
            Star.setStarBackgroundAndIcon(eliteMaterial,weapon.eliteMonsterMaterial.icon, weapon.eliteMonsterMaterial.star)
            Star.setStarBackgroundAndIcon(monsterMaterial,weapon.monsterMaterials.icon, weapon.monsterMaterials.star)

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out)
    }

}