package com.lianyi.paimonsnotebook.util

import android.animation.*
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.SwitchCompat
import androidx.cardview.widget.CardView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayout
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.account.UserBean
import com.lianyi.paimonsnotebook.bean.config.ContentMarginSettings
import com.lianyi.paimonsnotebook.databinding.*
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.information.Constants
import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.lib.listener.AnimatorFinished
import com.lianyi.paimonsnotebook.util.PaiMonsNoteBook.Companion.context
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import me.jessyan.autosize.AutoSizeConfig
import me.jessyan.autosize.unit.Subunits
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.apache.poi.ss.formula.functions.T
import org.jsoup.Jsoup
import java.util.*
import kotlin.concurrent.thread


class PaiMonsNoteBook : Application(){
    companion object{
        lateinit var context: Context
        var loadingWindow: AlertDialog? = null
        var loadingWindowAnimatorSet:AnimatorSet? = null


        val APP_VERSION_NAME: String by lazy {
            context.packageManager.getPackageInfo(context.packageName,
                PackageManager.GET_CONFIGURATIONS).versionName
        }

        val APP_VERSION_CODE by lazy {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                context.packageManager.getPackageInfo(context.packageName,PackageManager.GET_CONFIGURATIONS).longVersionCode
            } else {
                context.packageManager.getPackageInfo(context.packageName,PackageManager.GET_CONFIGURATIONS).versionCode.toLong()
            }
        }

        const val APP_NAME = "Paimons Notebook"
        const val UIGF_VERSION = "v2.2"
    }
    override fun onCreate() {
        super.onCreate()
        context = baseContext

        //??????????????????
        AutoSizeConfig.getInstance()
            .setCustomFragment(true) //?????????fragment??????
            .setExcludeFontScale(true) //??????????????????????????????
//            .setUseDeviceSize(true) //??????????????????????????????????????????, ????????? false, ??????????????? false, ??????????????????????????????????????????
//                //AutoSize ??????????????????????????????????????????????????????
            .unitsManager
            .supportSubunits = Subunits.MM

        //??????mainUser
        mainUser = GSON.fromJson(usp.getString(JsonCacheName.MAIN_USER_NAME,""), UserBean::class.java)

        if(mainUser==null){
            mainUser = UserBean()
        }
        AppCenter.start(this,"3562c119-7950-4211-ad45-b1d2546fc046",Analytics::class.java,Crashes::class.java)
    }
}

fun loadImage(imageView: ImageView, url:String?){
    Glide.with(imageView).load(url?:"").into(imageView)
}

fun loadHomeNoticeImage(imageView: ImageView, url:String?){
    try {
        Glide.with(imageView)
            .load(url?:"")
            .override(context.resources.displayMetrics.widthPixels,Target.SIZE_ORIGINAL)
            .into(imageView)
    }catch (e:Exception){
        e.printStackTrace()
        "??????${url}?????????????????????:${e}".show()
    }
}

var mainUser: UserBean? = null
    get() = field

//????????????
val sp: SharedPreferences
get() = context.getSharedPreferences(Constants.SP_NAME,Context.MODE_PRIVATE)
//??????????????????
val usp:SharedPreferences
get() = context.getSharedPreferences(Constants.USP_NAME,Context.MODE_PRIVATE)
//????????????
val csp:SharedPreferences
get() = context.getSharedPreferences(Constants.CSP_NAME,Context.MODE_PRIVATE)
//????????????
val wsp:SharedPreferences
get() = context.getSharedPreferences(Constants.WSP_NAME,Context.MODE_PRIVATE)


val Int.dp
    get() = context.resources.displayMetrics.density * this +0.5f

val Int.sp
    get() = (this * context.resources.displayMetrics.scaledDensity + 0.5f)

val Int.px2dp
    get() = (this /  context.resources.displayMetrics.density +0.5f)


//???????????????????????????
val navigationBarHeight:Int
        by lazy {
            val resourceId = context.resources.getIdentifier("navigation_bar_height","dimen", "android")
            var height = 0
            //??????????????????????????????
            if(resourceId>0){
                //??????????????????????????????
                val configShowNav = context.resources.getIdentifier("config_showNavigationBar", "bool", "android")
                var isHave = false

                if(configShowNav!=0){
                    isHave = context.resources.getBoolean(configShowNav)
                }

                if(isHave){
                    height = context.resources.getDimensionPixelSize(resourceId)
                }
            }
            height
        }

//??????????????????????????????????????????????????????
fun setViewMarginBottomByNavigationBarHeight(vararg views:View){
    views.forEach {view->
        val lp = view.layoutParams as ViewGroup.MarginLayoutParams
        lp.setMargins(view.marginLeft,view.marginTop,view.marginRight,view.marginBottom+ navigationBarHeight)
        view.layoutParams = lp
    }
}

fun setListItemMargin(view:View,position: Int,topMargin:Int){
    when(position){
        0->{
            val lp = view.layoutParams as ViewGroup.MarginLayoutParams
            lp.setMargins(view.marginLeft,view.marginTop+topMargin.dp.toInt(),view.marginRight,view.marginBottom)
            view.layoutParams = lp
        }
    }
}

//??????????????????
fun setContentMargin(view:View){
   if(ContentMarginSettings.instance.enable){
       val lp = view.layoutParams as ViewGroup.MarginLayoutParams
       val horizontal = ContentMarginSettings.instance.horizontalProgress.dp.toInt()
       val top = ContentMarginSettings.instance.topMargin.dp.toInt()
       lp.setMargins(horizontal,top,horizontal,0)
   }
}

fun String.show(){
    Toast.makeText(context,this,Toast.LENGTH_SHORT).show()
}

fun String.showLong(){
    Toast.makeText(context,this,Toast.LENGTH_LONG).show()
}

inline fun <reified T>goA(){
    val intent = Intent(context,T::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}

fun View.gone(){
    this.visibility = View.GONE
}
fun View.show(){
    this.visibility = View.VISIBLE
}

fun getSP(sp:SharedPreferences,name:String):String{
    return sp.getString(name,"")!!
}

fun getListSP(sp:SharedPreferences,name:String):String{
    return sp.getString(name,"[]")!!
}


//tab layout????????????
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

//??????????????????
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

//checkbox???????????????
fun AppCompatCheckBox.select(block: (Boolean) -> Unit){
    this.setOnCheckedChangeListener { p0, p1 -> block(p1) }
}

fun SeekBar.onChange(block: (Int) -> Unit){
    this.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            block(p1)
        }

        override fun onStartTrackingTouch(p0: SeekBar?) {
        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
        }
    })
}


inline fun <reified VB:ViewBinding> Activity.las() = lazy {
    (VB::class.java.getMethod("inflate",LayoutInflater::class.java).invoke(null,layoutInflater) as VB).apply {
        setContentView(root)
    }
}

inline fun <reified VB:ViewBinding>View.las():VB{
    return (VB::class.java.getMethod("bind",View::class.java).invoke(null,this) as VB)
}

//??????????????????
fun dismissLoadingWindow(){
    PaiMonsNoteBook.loadingWindowAnimatorSet?.cancel()
    PaiMonsNoteBook.loadingWindowAnimatorSet?.removeAllListeners()
    PaiMonsNoteBook.loadingWindow?.dismiss()
}

//???????????????
fun showLoading(context: Context){
    //????????????
    val layout = LayoutInflater.from(context).inflate(R.layout.pop_loading,null)
    val item = PopLoadingBinding.bind(layout)
    val card = CardView(context)
    val win = AlertDialog.Builder(context).setView(card).create()
    card.addView(layout)
    card.cardElevation = 0f
    card.radius = 10.dp

    //??????????????????????????????
    win.setOnKeyListener { p0, p1, p2 ->
        when (p1) {
            KeyEvent.KEYCODE_BACK -> true
            else -> false
        }
    }

    //??????????????????window?????????????????????
    win.setCancelable(false)

    PaiMonsNoteBook.loadingWindow = win
    dismissLoadingWindow()

    //???????????????????????????
    win.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    win.window?.decorView?.setPadding(10.dp.toInt(),0,10.dp.toInt(),0)
    win.window?.setLayout(PaiMonsNoteBook.context.resources.displayMetrics.widthPixels-20.dp.toInt(),
        ViewGroup.LayoutParams.WRAP_CONTENT)

    win.show()

    //????????????
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
        if(PaiMonsNoteBook.loadingWindow?.isShowing==true){
            animSet.start()
        }
    })

//    animSet.addListener(object :AnimatorListenerAdapter(){
//        var isShowing = false
//        override fun onAnimationStart(animation: Animator?) {
//            isShowing = true
//        }
//
//        override fun onAnimationCancel(animation: Animator?) {
//            isShowing = false
//        }
//
//        override fun onAnimationEnd(animation: Animator?) {
//            if(isShowing){
//                animSet.start()
//            }
//        }
//    })
    PaiMonsNoteBook.loadingWindowAnimatorSet = animSet
}

//?????????????????? ????????????
fun openAndCloseAnimationHor(target:View, start:Int, end:Int, time:Long,block: () -> Unit = object:() -> Unit{
    override fun invoke() {
    }
}){
    val anim = ValueAnimator.ofInt(start.dp.toInt(), end.dp.toInt())
    anim.duration = time
    anim.addUpdateListener {
        target.layoutParams.width = it.animatedValue as Int
        target.requestLayout()
    }
    anim.start()
    anim.addListener(AnimatorFinished{
        block()
    })
}

fun ViewPager2.onPageChange(block: (Int) -> Unit){
    this.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            block(position)
        }
    })
}

fun openAndCloseAnimationVer(target:View, start:Int, end:Int, time:Long,block: () -> Unit = object:() -> Unit{
    override fun invoke() {
    }
}){
    target.clearAnimation()
    val anim = ValueAnimator.ofInt(start, end)
    anim.duration = time
    anim.addUpdateListener {
        target.layoutParams.height = it.animatedValue as Int
        target.requestLayout()
    }
    anim.start()
}

fun String.toMyRequestBody(): RequestBody {
    return this.toRequestBody("application/json;charset=utf-8".toMediaType())
}

fun String.substring(start:String, end:String):String{
    val from = if(start.isEmpty()){
        0
    }else{
        val index = this.indexOf(start)
        if(index==-1) 0 else index
    }
    val to = if(end.isEmpty()){
        0
    }else{
        val index = this.indexOf(end)
        if(index==-1) 0 else index
    }

    return this.substring(from,to)
}

fun <T>List<T>.copy(list: MutableList<T>){
    list.clear()
    this.forEach {
        list += it
    }
}

fun <T>MutableList<T>.addFromList(list: List<T>){
    list.forEach {
        this+=it
    }
}

fun Spinner.select(block: (position:Int,id:Long) -> Unit){
    this.onItemSelectedListener = null
    this.onItemSelectedListener = object :OnItemSelectedListener{
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            block(p2,p3)
        }
        override fun onNothingSelected(p0: AdapterView<*>?) {
        }
    }
}

fun SwitchCompat.click(block: (Boolean) -> Unit){
    this.setOnCheckedChangeListener { compoundButton, b ->
        block(b)
    }
}

fun Fragment.setOnHandleBackPressed(block:(() -> Unit)?=null){
    requireActivity().onBackPressedDispatcher.addCallback {
        block?.invoke()
    }
}

fun MotionLayout.onFinished(block: () -> Unit){
    this.setTransitionListener(object :MotionLayout.TransitionListener{
        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
        }

        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
        ) {
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
            block()
        }

        override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) {
        }
    })
}


fun MotionLayout.onPlaying(block: (progress:Float) -> Unit){
    this.setTransitionListener(object :MotionLayout.TransitionListener{
        override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
        }

        override fun onTransitionChange(
            motionLayout: MotionLayout?,
            startId: Int,
            endId: Int,
            progress: Float
        ) {
            block(progress)
        }

        override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
        }

        override fun onTransitionTrigger(
            motionLayout: MotionLayout?,
            triggerId: Int,
            positive: Boolean,
            progress: Float
        ) {
        }
    })
}
