package com.example.myapplication.Database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Approval::class],
    version = 1
)


abstract  class ApprovalDatabase : RoomDatabase(){
    abstract val dao: ApprovalStaffDao
}