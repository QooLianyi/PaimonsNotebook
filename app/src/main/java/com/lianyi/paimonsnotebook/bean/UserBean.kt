package com.lianyi.paimonsnotebook.bean


class UserBean(var nickName:String,
               var loginUid:String,
               var region:String,
               var regionName:String,
               var gameUid:String,
               var sToken:String,
               var lToken:String,
               var cookieToken:String,
               var gameLevel:Int
               ) {

    constructor():this("","","","","","","","",0)

    override fun toString(): String {
        return "UserBean(nickName='$nickName', loginUid='$loginUid', region='$region', regionName='$regionName', gameUid='$gameUid', sToken='$sToken', lToken='$lToken', cookieToken='$cookieToken', gameLevel=$gameLevel)"
    }

    fun isNotLogin():Boolean{
        return loginUid.isEmpty()&&region.isEmpty()&&cookieToken.isEmpty()&&lToken.isEmpty()
    }

}