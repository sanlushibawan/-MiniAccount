package com.sanlushibawan.miniaccount.entity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AccountDao {
    @Insert
    suspend fun insertAccount(account:AccountEntity): Long
    @Update
    suspend fun updateAccount(newAccount:AccountEntity)
    @Query("Select * from AccountEntity")
    suspend fun loadAllAccount():List<AccountEntity>
    @Query("Select * from AccountEntity where showIndexPage = 1")
    suspend fun loadShowAccount():List<AccountEntity>
    @Delete
    suspend fun deleteAccount(account: AccountEntity)
}