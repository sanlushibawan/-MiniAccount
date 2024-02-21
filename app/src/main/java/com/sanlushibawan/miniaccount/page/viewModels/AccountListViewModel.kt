package com.sanlushibawan.miniaccount.page.viewModels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sanlushibawan.miniaccount.entity.AccountEntity
import com.sanlushibawan.miniaccount.utils.AccountRepository
import com.sanlushibawan.miniaccount.utils.MiniDatabase
import kotlinx.coroutines.launch

class AccountListViewModel(application: Application):AndroidViewModel(application) {
    private val accountRepository: AccountRepository
    //val allAccount : LiveData<List<AccountEntity>>
    val insertResLong = MutableLiveData(0L)
    val allAccountList = mutableStateListOf<AccountEntity>()
    val modifyAccount = MutableLiveData(0)
    val accountDetailDBDone = mutableStateOf(false)
    init {
        val accountDao = MiniDatabase.getDatabase(application).accountDao()
        accountRepository = AccountRepository(accountDao)
        //allAccount = accountRepository.allAccount
    }

    fun loadAccountList()=viewModelScope.launch {
        allAccountList.addAll(accountRepository.loadAccountList())
    }
    fun addAccount(accountEntity: AccountEntity)= viewModelScope.launch {
        insertResLong.value =  accountRepository.addAccount(accountEntity)
        if (insertResLong.value!=0L) allAccountList.add(accountEntity) }
    fun updateAccount(accountEntity: AccountEntity)=viewModelScope.launch {
        accountRepository.updateAccount(accountEntity)
        modifyAccount.value?.let {
            allAccountList.removeAt(it)
            allAccountList.add(it,accountEntity)
        }
        accountDetailDBDone.value = false
    }
    fun deleteAccount(accountEntity: AccountEntity)=viewModelScope.launch {
        accountRepository.deleteAccount(accountEntity)
        modifyAccount.value?.let {
            allAccountList.removeAt(it)
        }
        accountDetailDBDone.value = false
    }
}