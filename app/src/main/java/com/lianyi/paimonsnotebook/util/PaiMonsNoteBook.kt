package com.lianyi.paimonsnotebook.util

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.UserBean
import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.config.Settings
import com.lianyi.paimonsnotebook.databinding.PopLoadingBinding
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.unit.Subunits

class PaiMonsNoteBook :Application(){
    companion object{
        lateinit var context:Context
        lateinit var loadingWindow:androidx.appcompat.app.AlertDialog
    }
    override fun onCreate() {
        super.onCreate()
        context = baseContext

        //初始化自适应
        AutoSizeConfig.getInstance()
            .setCustomFragment(true)
            .unitsManager
            .supportSubunits = Subunits.MM

        //设置mainUser
        mainUser = GSON.fromJson(usp.getString(Settings.USP_MAIN_USER_NAME,""),UserBean::class.java)

        if(mainUser ==null) mainUser = UserBean()
    }
}

fun loadImage(imageView: ImageView,url:String){
    Glide.with(imageView).load(url).into(imageView)
}

var mainUser:UserBean? = null
    get() = field

//数据缓存
val sp:SharedPreferences
get() = PaiMonsNoteBook.context.getSharedPreferences(Settings.SP_NAME,Context.MODE_PRIVATE)
//用户信息缓存
val usp:SharedPreferences
get() = PaiMonsNoteBook.context.getSharedPreferences(Settings.USP_NAME,Context.MODE_PRIVATE)
//角色缓存
val csp:SharedPreferences
get() = PaiMonsNoteBook.context.getSharedPreferences(Settings.CSP_NAME,Context.MODE_PRIVATE)
//武器缓存
val wsp:SharedPreferences
get() = PaiMonsNoteBook.context.getSharedPreferences(Settings.WSP_NAME,Context.MODE_PRIVATE)


val Int.dp
    get() = PaiMonsNoteBook.context.resources.displayMetrics.density * this +0.5f

fun String.show(){
    Toast.makeText(PaiMonsNoteBook.context,this,Toast.LENGTH_SHORT).show()
}

fun String.showLong(){
    Toast.makeText(PaiMonsNoteBook.context,this,Toast.LENGTH_LONG).show()
}

inline fun <reified T>goA(){
    val intent = Intent(PaiMonsNoteBook.context,T::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    PaiMonsNoteBook.context.startActivity(intent)
}

fun View.gone(){
    this.visibility = View.GONE
}
fun View.show(){
    this.visibility = View.VISIBLE
}

//tablayout选中回调
fun TabLayout.tab(block:(Int)->Unit){
    this.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
        override fun onTabSelected(p0: TabLayout.Tab?) {
            block(p0!!.position)
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }

        override fun onTabReselected(p0: TabLayout.Tab?) {
        }
    })
}


fun Animation.onFinished(block: () -> Unit){
    this.setAnimationListener(object :Animation.AnimationListener{
        override fun onAnimationStart(p0: Animation?) {
        }
        override fun onAnimationEnd(p0: Animation?) {
            block()
        }

        override fun onAnimationRepeat(p0: Animation?) {
        }
    })
}

//checkbox选中时回调
fun AppCompatCheckBox.select(block: (Boolean) -> Unit){
    this.setOnCheckedChangeListener { p0, p1 -> block(p1) }
}

//关闭加载窗口
fun loadingWindowDismiss(){
    PaiMonsNoteBook.loadingWindow.dismiss()
}

//加载时弹窗
fun showLoading(context: Context){

    println("弹窗")

    //加载布局
    val layout = LayoutInflater.from(context).inflate(R.layout.pop_loading,null)
    val item = PopLoadingBinding.bind(layout)
    val card = CardView(context)
    val win = androidx.appcompat.app.AlertDialog.Builder(context).setView(card).create()
    card.addView(layout)
    card.cardElevation = 0f
    card.radius = 10.dp

    //禁止通过点击返回关闭
    win.setOnKeyListener { p0, p1, p2 ->
        when (p1) {
            KeyEvent.KEYCODE_BACK -> true
            else -> false
        }
    }

    //禁止通过点击window外面的区域关闭
    win.setCancelable(false)
    PaiMonsNoteBook.loadingWindow = win

    //设置弹窗的内部布局
    win.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    win.window?.decorView?.setPadding(10.dp.toInt(),0,10.dp.toInt(),0)
    win.window?.setLayout(PaiMonsNoteBook.context.resources.displayMetrics.widthPixels-20.dp.toInt(),ViewGroup.LayoutParams.WRAP_CONTENT)

    win.show()

    //设置动画
    val loadingWidth = PaiMonsNoteBook.context.resources.displayMetrics.widthPixels - 123.dp

    val animGo = ObjectAnimator.ofFloat(item.iconKlee,"translationX",10f,loadingWidth)
    animGo.duration = 1500L

    val animBack = ObjectAnimator.ofFloat(item.iconKlee,"translationX",loadingWidth,10f)
    animBack.duration = 1500L

    val rotateBack =  ObjectAnimator.ofFloat(item.iconKlee,"rotationY",0f,180f)
    rotateBack.duration = 500L

    val rotateGo =  ObjectAnimator.ofFloat(item.iconKlee,"rotationY",180f,0f)
    rotateGo.duration = 500L

    val animSet = AnimatorSet()
    animSet.play(animGo).before(rotateBack)
    animSet.play(rotateBack).before(animBack)
    animSet.play(animBack).before(rotateGo)
    animSet.start()

    animSet.addListener(AnimatorFinished{
        if(PaiMonsNoteBook.loadingWindow.isShowing){
            animSet.start()
        }
    })

}

//设置组件宽度 期间动画
fun openAndCloseAnimationHor(target:View, start:Int, end:Int, time:Long){
    val anim = ValueAnimator.ofInt(start.dp.toInt(), end.dp.toInt())
    anim.duration = time
    anim.addUpdateListener {
        target.layoutParams.width = it.animatedValue as Int
        target.requestLayout()
    }
    anim.start()
}
fun openAndCloseAnimationVer(target:View, start:Int, end:Int, time:Long){
    val anim = ValueAnimator.ofInt(start.dp.toInt(), end.dp.toInt())
    anim.duration = time
    anim.addUpdateListener {
        target.layoutParams.height = it.animatedValue as Int
        target.requestLayout()
    }
    anim.start()
}


//recyclerview通用适配器
class ReAdapter<T>(val data:List<T>,val item:Int,val block:(RecyclerView.Adapter<RecyclerView.ViewHolder>.(view: View,T,position:Int)->Unit)):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object :RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(item,parent,false)){}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        block(holder.itemView,data[position],position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

}

//viewpager通用适配器
class PagerAdapter(val pages:List<View>,val titles:List<String>):PagerAdapter(){
    override fun getCount(): Int {
        return pages.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(pages[position])
        return pages[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(pages[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}

//animation监听器
class AnimatorFinished(val block: () -> Unit): Animator.AnimatorListener{
    override fun onAnimationStart(p0: Animator?) {
    }

    override fun onAnimationEnd(p0: Animator?) {
        block()
    }

    override fun onAnimationCancel(p0: Animator?) {
    }

    override fun onAnimationRepeat(p0: Animator?) {
    }
}


