package com.lianyi.paimonsnotebook.ui.activity

import android.os.Bundle
import android.view.ViewAnimationUtils
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.bean.WeaponBean
import com.lianyi.paimonsnotebook.databinding.ActivityWeaponDetailBinding
import com.lianyi.paimonsnotebook.databinding.PopInformationBinding
import com.lianyi.paimonsnotebook.lib.information.Star
import com.lianyi.paimonsnotebook.lib.information.WeaponType
import com.lianyi.paimonsnotebook.util.loadImage
import com.lianyi.paimonsnotebook.util.setContentMargin
import com.lianyi.paimonsnotebook.util.showAlertDialog
import kotlin.math.sqrt

class WeaponDetailActivity : BaseActivity() {
    companion object{
        lateinit var weapon:WeaponBean
    }
    lateinit var bind: ActivityWeaponDetailBinding
    private var animationFlag = true
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

        bind.information.setOnClickListener {
            val layout = PopInformationBinding.bind(layoutInflater.inflate(R.layout.pop_information,null))
            val win = showAlertDialog(bind.root.context,layout.root)
            layout.content.text = getString(R.string.information_entity_materials)
            layout.close.setOnClickListener {
                win.dismiss()
            }
        }
        setContentMargin(bind.root)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if(animationFlag){
            bind.motion.transitionToEnd()

            val starBackGroundWidth = bind.starBackground.width
            val starBackGroundHeight = bind.starBackground.height
            val endRadius = sqrt(starBackGroundWidth.toDouble()*starBackGroundWidth + starBackGroundHeight.toDouble()*starBackGroundHeight).toFloat()

            ViewAnimationUtils.createCircularReveal(bind.starBackground,starBackGroundWidth/2,starBackGroundHeight/2,0f,endRadius).setDuration(900L).start()
            animationFlag = false
        }

    }

}