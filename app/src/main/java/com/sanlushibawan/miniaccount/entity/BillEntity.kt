package com.sanlushibawan.miniaccount.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(foreignKeys =[ForeignKey(entity = AccountEntity::class,
    parentColumns = ["id"],
    childColumns = ["payAccount"],
    onDelete = CASCADE)])
data class BillEntity(
    var yearStr:Int,
    var monthStr: Int,
    var dayStr:Int,
    var weekStr:String,
    var billType:Boolean,
    var payAccount:Long,
    var payAccountName: String,
    var billAmount:Float,
    var remarks:String
){
    @PrimaryKey(autoGenerate = true)
    var billId:Long = 0L
}