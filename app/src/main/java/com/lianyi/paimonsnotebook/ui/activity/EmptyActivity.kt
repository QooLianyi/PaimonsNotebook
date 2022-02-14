package com.lianyi.paimonsnotebook.ui.activity

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import com.lianyi.paimonsnotebook.databinding.ActivityEmptyBinding
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.util.*
import kotlin.concurrent.thread
import kotlin.math.abs

class EmptyActivity : BaseActivity() {
    lateinit var bind :ActivityEmptyBinding

    var isTouch = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityEmptyBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val displayHeight = resources.displayMetrics.heightPixels
        val rect = Rect()

        window.decorView.getWindowVisibleDisplayFrame(rect)

    }

//    private fun initListView(){
//        bind.list.adapter = ReAdapter(CharacterBean.characterList,R.layout.item_character_icon){
//                view, avatarsBean, position ->
//            val item = ItemCharacterIconBinding.bind(view)
//            loadImage(item.icon,avatarsBean.icon)
//            item.starBackground.setImageResource(Star.getStarResourcesByStarNum(avatarsBean.star,false))
//
//            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
//            when(position){
//                0->{
//                    lp.setMargins(resources.displayMetrics.widthPixels/2 - 80.dp.toInt()/2,0,0,0)
//                }
//                CharacterBean.characterList.size-1->{
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
//
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
//                                    val distance = abs(recyclerView.width / 2 - (view.left))
//                                    if (distance < minDistance) {
//                                        minDistance = distance
//                                        left = view.left
//                                    }
//                                }
//                                val moveDistance = (recyclerView.width / 2) - (left + 40.dp.toInt())
//                                println("moveDistance = $moveDistance left = $left")
//                                thread {
//                                    Thread.sleep(500)
//                                    recyclerView.smoothScrollBy(moveDistance,0)
//                                    isTouch = false
//                                }
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
//
//        bind.list.setOnTouchListener { view, motionEvent ->
//            isTouch = true
//            false
//        }
//    }

    private fun calculateTranslate(left: Int, width: Int):Int{
        val wid = (width - 80.dp.toInt()) /2
        return (wid - abs(left - wid)) / 2
    }

}