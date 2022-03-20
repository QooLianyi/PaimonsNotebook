package com.lianyi.paimonsnotebook.ui.activity

import android.os.Bundle
import android.text.Html
import androidx.cardview.widget.CardView
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.PlayerCharacterInformationBean
import com.lianyi.paimonsnotebook.databinding.*
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.lib.information.Star
import com.lianyi.paimonsnotebook.util.*
import java.lang.StringBuilder
import kotlin.math.abs

class SearchResultCharacterDetailActivity : BaseActivity() {
    companion object{
        lateinit var avatarsBean:PlayerCharacterInformationBean.AvatarsBean
        lateinit var avatarsList:List<PlayerCharacterInformationBean.AvatarsBean>
    }

    lateinit var bind: ActivitySearchResultCharacterDetailBinding
    var isTouch = false

    private lateinit var selectItem:CardView
    private val reliquaries = mutableListOf<PlayerCharacterInformationBean.AvatarsBean.ReliquariesBean>()
    private val constellations = mutableListOf<PlayerCharacterInformationBean.AvatarsBean.ConstellationsBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivitySearchResultCharacterDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initView()
    }

    private fun initView(){

        with(bind){
            setConstellationsClickEvent(constellation1,constellation2,constellation3,constellation4,constellation5,constellation6)
        }

        with(bind){
            setItemClickEvent(weapon,reliquary1,reliquary2,reliquary3,reliquary4,reliquary5)
        }

//        bind.list.adapter = ReAdapter(avatarsList,R.layout.item_character_icon){
//            view, avatarsBean, position ->
//            val item = ItemCharacterIconBinding.bind(view)
//            loadImage(item.icon,avatarsBean.icon)
//            item.value.text = avatarsBean.level.toString()
//            item.starBackground.setImageResource(Star.getStarResourcesByStarNum(avatarsBean.rarity,false))
//
//            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT)
//            when(position){
//                0->{
//                    lp.setMargins(resources.displayMetrics.widthPixels/2 - 80.dp.toInt()/2,0,0,0)
//                }
//                avatarsList.size-1->{
//                    lp.setMargins(10.dp.toInt(),0,resources.displayMetrics.widthPixels/2 - 80.dp.toInt()/2,0)
//                }
//                else->{
//                    lp.setMargins(10.dp.toInt(),0,0,0)
//                }
//            }
//            item.root.layoutParams = lp
//
//            item.root.scaleX = 0.6f
//            item.root.scaleY = 0.6f
//        }

//        bind.list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                if (isTouch) {
//                    when (newState) {
//                        RecyclerView.SCROLL_STATE_DRAGGING -> {
//                            (0 until recyclerView.childCount).forEach {
//                            }
//                        }
//                        RecyclerView.SCROLL_STATE_IDLE -> {
//                            if (isTouch) {
//                                var minDistance = abs(recyclerView.width / 2 - recyclerView.getChildAt(0).left)
//                                var left = 0
//                                recyclerView.forEach {view ->
//                                    val distance = abs(recyclerView.width / 2 - (view.left + 40.dp.toInt()))
//                                    if (distance < minDistance) {
//                                        minDistance = distance
//                                        left = view.left
//                                    }
//                                }
//                                val moveDistance = (recyclerView.width / 2) - (left + 40.dp.toInt())
//                                println("moveDistance = $moveDistance left = $left")
//                                recyclerView.smoothScrollBy(moveDistance,0)
//                                isTouch = false
//                            }
//                        }
//                    }
//                }
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                (0 until recyclerView.childCount).forEach {
//                    val bottom =
//                        calculateTranslate(recyclerView.getChildAt(it).left, recyclerView.width)
//                    recyclerView.getChildAt(it).setPadding(0, 0, 0, bottom)
//                }
//            }
//        })

//        bind.list.setOnTouchListener{ view: View, motionEvent: MotionEvent ->
//            isTouch = true
//            false
//        }

        avatarsBean.reliquaries.forEach {
            reliquaries+=it
            when(it.pos){
                1->{
                    loadImage(bind.reliquary1.icon, it.icon)
                }
                2->{
                    loadImage(bind.reliquary2.icon, it.icon)
                }
                3->{
                    loadImage(bind.reliquary3.icon, it.icon)
                }
                4->{
                    loadImage(bind.reliquary4.icon, it.icon)
                }
                5->{
                    loadImage(bind.reliquary5.icon, it.icon)
                }
            }
        }

        avatarsBean.constellations.forEach {
            constellations+=it
            when(it.pos){
                1->{
                    loadImage(bind.constellation1.icon, it.icon)
                    if(it.isIs_actived){
                        bind.constellation1.lock.gone()
                    }
                }
                2->{
                    loadImage(bind.constellation2.icon, it.icon)
                    if(it.isIs_actived){
                        bind.constellation2.lock.gone()
                    }
                }
                3->{
                    loadImage(bind.constellation3.icon, it.icon)
                    if(it.isIs_actived){
                        bind.constellation3.lock.gone()
                    }
                }
                4->{
                    loadImage(bind.constellation4.icon, it.icon)
                    if(it.isIs_actived){
                        bind.constellation4.lock.gone()
                    }
                }
                5->{
                    loadImage(bind.constellation5.icon, it.icon)
                    if(it.isIs_actived){
                        bind.constellation5.lock.gone()
                    }
                }
                6->{
                    loadImage(bind.constellation6.icon, it.icon)
                    if(it.isIs_actived){
                        bind.constellation6.lock.gone()
                    }
                }
            }
        }

        bind.weapon.select.setCardBackgroundColor(getColor(R.color.star_color))
        setContentMargin(bind.root)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        bind.motion.transitionToEnd()

    }

    private fun calculateTranslate(left:Int,width:Int):Int{
        val wid = (width - 80.dp.toInt()) /2
        return (wid - abs(left - wid)) / 3
    }

    private fun setConstellationsClickEvent(vararg items: ItemConstellationsBinding){
        items.forEachIndexed { index,item->
            item.root.setOnClickListener {
                bind.itemInformation.gone()
                bind.constellationsInformation.show()
                loadImage(bind.constellationsShow.icon,constellations[index].icon)
                bind.constellationsShow.select.cardElevation = 0f
                bind.constellationsName.text = constellations[index].name
                bind.constellationsLevel.text = "第${constellations[index].pos}层"
                if(constellations[index].isIs_actived){
                    bind.constellationsShow.lock.gone()
                }else{
                    bind.constellationsShow.lock.show()
                }

                bind.constellationsDetail.text = Html.fromHtml(constellations[index].effect,Html.FROM_HTML_MODE_LEGACY).toString().replace("\\n","\n")
                onSelectItem(item.select)
            }
        }
    }

    private fun setItemClickEvent(vararg items: ItemReliquaryBinding){
        items.forEachIndexed { index,item->
            item.root.setOnClickListener {
                bind.itemInformation.show()
                bind.constellationsInformation.gone()
                when(index){
                    0->{
                        bind.itemType.text = "武器"
                        loadImage(bind.show.icon, avatarsBean.weapon.icon)
                        bind.itemName.text = avatarsBean.weapon.name
                        bind.itemLevel.text = "LV${avatarsBean.weapon.level}"
                        bind.itemAffixLevel.show()
                        bind.itemAffixLevel.text = "精炼${avatarsBean.weapon.affix_level}级"
                        bind.itemStar.text = Star.getStarSymbolByStarNum(avatarsBean.weapon.rarity)
                        bind.itemDetailName.gone()
                        bind.itemDetail.text = avatarsBean.weapon.desc
                    }
                    else->{
                        reliquaries.forEach {
                            if(it.pos==index){
                                bind.itemType.text = "圣遗物"
                                loadImage(bind.show.icon, it.icon)
                                bind.itemName.text = it.name
                                bind.itemLevel.text = "+${it.level}"
                                bind.itemAffixLevel.gone()
                                bind.itemStar.text = Star.getStarSymbolByStarNum(it.rarity)
                                bind.itemDetailName.show()
                                bind.itemDetailName.text = it.set.name

                                val sb = StringBuilder()
                                it.set.affixes.forEach {
                                    sb.append("${it.activation_number}件套:")
                                    sb.append("${it.effect}\n")
                                }
                                bind.itemDetail.text = sb.toString()
                            }
                        }
                    }
                }
                onSelectItem(item.select)
            }
        }
        loadImage(bind.icon, avatarsBean.image)
        loadImage(bind.weapon.icon, avatarsBean.weapon.icon)
        selectItem = bind.weapon.select
        bind.weapon.root.performClick()
    }

    private fun onSelectItem(cardView: CardView){
        cardView.setCardBackgroundColor(getColor(R.color.star_color))
        selectItem.setCardBackgroundColor(getColor(R.color.white))
        selectItem = cardView
    }

}