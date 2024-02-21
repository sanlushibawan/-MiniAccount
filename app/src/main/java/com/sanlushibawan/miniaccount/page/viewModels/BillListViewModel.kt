package com.sanlushibawan.miniaccount.page.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sanlushibawan.miniaccount.entity.BillEntity
import com.sanlushibawan.miniaccount.utils.BillRepository
import com.sanlushibawan.miniaccount.utils.MiniDatabase
import kotlinx.coroutines.launch

class BillListViewModel(application: Application) : AndroidViewModel(application) {
    private val billRepository:BillRepository
    val billList = mutableStateListOf<BillEntity>()
    var showDetailDialogFlag = mutableStateOf(false)
    init {
        val billDao = MiniDatabase.getDatabase(application).billDao()
        billRepository = BillRepository(billDao)
        viewModelScope.launch {
            billList.addAll(billRepository.loadAllBills())
        }
    }

    fun removeBill(billEntity: BillEntity)=viewModelScope.launch{
        billRepository.deleteBill(billEntity)
        billList.remove(billEntity)
        showDetailDialogFlag.value = false
    }
}