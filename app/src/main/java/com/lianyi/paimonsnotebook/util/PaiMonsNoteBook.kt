package com.lianyi.paimonsnotebook.util

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.lianyi.paimonsnotebook.config.Settings

class PaiMonsNoteBook :Application(){
    companion object{
        lateinit var context:Context
    }
    override fun onCreate() {
        super.onCreate()
        context = baseContext
    }
}

fun loadImage(imageView: ImageView,url:String){
    Glide.with(imageView).load(url).into(imageView)
}

val sp:SharedPreferences
get() = PaiMonsNoteBook.context.getSharedPreferences(Settings.SP_NAME,Context.MODE_PRIVATE)

val usp:SharedPreferences
get() = PaiMonsNoteBook.context.getSharedPreferences(Settings.USP_NAME,Context.MODE_PRIVATE)

val csp:SharedPreferences
get() = PaiMonsNoteBook.context.getSharedPreferences(Settings.CSP_NAME,Context.MODE_PRIVATE)

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

fun AppCompatCheckBox.select(block: (Boolean) -> Unit){
    this.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
            block(p1)
        }
    })
}

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


