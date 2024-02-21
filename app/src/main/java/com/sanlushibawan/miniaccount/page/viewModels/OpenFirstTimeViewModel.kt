package com.sanlushibawan.miniaccount.page.viewModels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sanlushibawan.miniaccount.entity.AccountEntity
import com.sanlushibawan.miniaccount.utils.AccountRepository
import com.sanlushibawan.miniaccount.utils.MiniDatabase
import kotlinx.coroutines.launch

class OpenFirstTimeViewModel(application: Application) :AndroidViewModel(application) {
    private val accountRepository: AccountRepository
    val insertResLong = MutableLiveData(0L)
    val inputUserName = mutableStateOf("")

    init {
        val accountDao = MiniDatabase.getDatabase(application).accountDao()
        accountRepository = AccountRepository(accountDao)
    }

    fun addAccount(accountEntity: AccountEntity)= viewModelScope.launch {
        insertResLong.value =  accountRepository.addAccount(accountEntity)
    }

}