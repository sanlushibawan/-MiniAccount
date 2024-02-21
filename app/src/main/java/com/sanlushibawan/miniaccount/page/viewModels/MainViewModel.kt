package com.sanlushibawan.miniaccount.page.viewModels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanlushibawan.miniaccount.entity.AccountEntity
import com.sanlushibawan.miniaccount.entity.BillEntity
import com.sanlushibawan.miniaccount.utils.AccountRepository
import com.sanlushibawan.miniaccount.utils.BillRepository
import com.sanlushibawan.miniaccount.utils.MiniDatabase
import kotlinx.coroutines.launch

class MainViewModel(application: Application) :AndroidViewModel(application){
    private val accountRepository: AccountRepository
    private val billRepository:BillRepository
    //val allAccount : LiveData<List<AccountEntity>>
    val indexAccountList = mutableStateListOf<AccountEntity>()
    val indexBillList = mutableStateListOf<BillEntity>()
    val selectAccountList = mutableStateListOf<AccountEntity>()
    var showNewBill = mutableStateOf(false)
    var showDetailDialogFlag = mutableStateOf(false)
    init {
        val db = MiniDatabase.getDatabase(application)
        accountRepository = AccountRepository(db.accountDao())
        billRepository = BillRepository(db.billDao())
        //allAccount = accountRepository.allAccount
        viewModelScope.launch {
            indexAccountList.addAll(accountRepository.loadIndexAccounts())
            indexBillList.addAll(billRepository.loadIndexBills())
            selectAccountList.addAll(accountRepository.loadAccountList())
        }
    }
    fun refreshCard()=viewModelScope.launch {
        indexAccountList.clear()
        indexAccountList.addAll(accountRepository.loadIndexAccounts())
        indexBillList.clear()
        indexBillList.addAll(billRepository.loadIndexBills())
        selectAccountList.clear()
        selectAccountList.addAll(accountRepository.loadAccountList())
    }
    fun addNewBill(billEntity: BillEntity,accountEntity: AccountEntity)=viewModelScope.launch {
        if(billEntity.billType) accountEntity.balance -= billEntity.billAmount
        else accountEntity.balance += billEntity.billAmount
        billRepository.addBill(billEntity)
        indexBillList.add(0,billEntity)
        accountRepository.updateAccount(accountEntity)
        selectAccountList.clear()
        selectAccountList.addAll(accountRepository.loadAccountList())
        indexAccountList.clear()
        indexAccountList.addAll(accountRepository.loadIndexAccounts())
        showNewBill.value = false
    }

    fun removeBill(billEntity: BillEntity)=viewModelScope.launch{
        billRepository.deleteBill(billEntity)
        indexBillList.remove(billEntity)
        showDetailDialogFlag.value = false
    }
}