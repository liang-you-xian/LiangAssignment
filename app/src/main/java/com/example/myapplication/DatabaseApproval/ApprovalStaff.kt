package com.example.myapplication.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Approval(
    val appreason: String? ="",
    val leaveandlate : String? ="",
    val stateinfo: String? ="",
    val apptime:String? ="",
    val appdate:String? ="",
    @PrimaryKey(autoGenerate = false)
    val staffid: String ="None"

)

