package com.ucsm.tylersai.amsteacher.model

data class Attendance (
    var date: String,
    var time: String,
    var mkpt: String,
    var subjectcode: String

){
    constructor() : this("","","","")
}