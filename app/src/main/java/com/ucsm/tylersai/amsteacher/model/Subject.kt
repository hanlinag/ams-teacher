package com.ucsm.tylersai.amsteacher.model

data class Subject (
    var day: String,
    var name : String,
    var room : String,
    var subjectCode: String,
    var teacherId: String,
    var time: String
){
    constructor():this("","","","","","")
}