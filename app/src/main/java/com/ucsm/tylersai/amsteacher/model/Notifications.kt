package com.ucsm.tylersai.amsteacher.model


data class Notifications (val title: String,
                          val body: String,
                          val date: String,
                          val key: String,
                          var targetUser: String
){
    constructor():this("","","","","")
}