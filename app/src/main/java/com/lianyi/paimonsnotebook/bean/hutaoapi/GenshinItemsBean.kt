package com.lianyi.paimonsnotebook.bean.hutaoapi

data class GenshinItemsBean(val avatars:List<Avatars>,val weapons:List<Weapons>,val reliquaries:List<Reliquaries>){

    data class Avatars(val id:Int,val name:String,val url:String)
    data class Weapons(val id:Int,val name:String,val url:String)
    data class Reliquaries(val id:Int,val name:String,val url:String)
}
