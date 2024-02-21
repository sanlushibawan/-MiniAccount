package com.sanlushibawan.miniaccount.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BillDao {
    @Insert
    suspend fun insertBill(bill: BillEntity):Long
    @Query("SELECT * from billentity  ORDER BY yearStr DESC,monthStr DESC,dayStr DESC,billId DESC")
    suspend fun loadAllBills():List<BillEntity>
    @Query("SELECT * from billentity  ORDER BY yearStr DESC,monthStr DESC,dayStr DESC,billId DESC LIMIT 100")
    suspend fun loadIndexBills():List<BillEntity>
    @Update
    suspend fun updateBill(bill: BillEntity)
    @Delete
    suspend fun deleteBill(billEntity: BillEntity)
}