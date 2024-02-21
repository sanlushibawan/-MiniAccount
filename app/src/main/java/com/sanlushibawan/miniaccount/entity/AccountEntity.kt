package com.sanlushibawan.miniaccount.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccountEntity(var accountName:String,
                         var balance:Float,
                         var accountType:String,
                         var accountId:String,
                         var showIndexPage:Boolean){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
