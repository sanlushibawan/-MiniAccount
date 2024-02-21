package com.sanlushibawan.miniaccount.utils

import androidx.lifecycle.LiveData
import com.sanlushibawan.miniaccount.entity.AccountDao
import com.sanlushibawan.miniaccount.entity.AccountEntity

class AccountRepository(private val accountDao: AccountDao) {
    //val allAccount:LiveData<List<AccountEntity>> = accountDao.loadAllAccount()
    //val showIndexAccount:LiveData<List<AccountEntity>> = accountDao.loadShowAccount()
    suspend fun loadAccountList():List<AccountEntity>{
        return accountDao.loadAllAccount()
    }
    suspend fun loadIndexAccounts():List<AccountEntity>{
        return accountDao.loadShowAccount()
    }
    suspend fun addAccount(accountEntity: AccountEntity):Long{
        return accountDao.insertAccount(accountEntity)
    }
    suspend fun updateAccount(accountEntity: AccountEntity){
        accountDao.updateAccount(accountEntity)
    }
    suspend fun deleteAccount(accountEntity: AccountEntity){
        accountDao.deleteAccount(accountEntity)
    }
}