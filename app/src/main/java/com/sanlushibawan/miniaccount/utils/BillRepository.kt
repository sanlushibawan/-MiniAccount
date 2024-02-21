package com.sanlushibawan.miniaccount.utils

import com.sanlushibawan.miniaccount.entity.AccountEntity
import com.sanlushibawan.miniaccount.entity.BillDao
import com.sanlushibawan.miniaccount.entity.BillEntity

class BillRepository(private val billDao: BillDao) {
    suspend fun loadAllBills():List<BillEntity>{
        return billDao.loadAllBills()
    }
    suspend fun loadIndexBills():List<BillEntity>{
        return billDao.loadIndexBills()
    }
    suspend fun addBill(billEntity: BillEntity):Long{
        return billDao.insertBill(billEntity)
    }
    suspend fun updateBill(billEntity: BillEntity){
        billDao.updateBill(billEntity)
    }
    suspend fun deleteBill(billEntity: BillEntity){
        billDao.deleteBill(billEntity)
    }
}