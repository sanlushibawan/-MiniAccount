package com.sanlushibawan.miniaccount.utils

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sanlushibawan.miniaccount.entity.AccountDao
import com.sanlushibawan.miniaccount.entity.AccountEntity
import com.sanlushibawan.miniaccount.entity.BillDao
import com.sanlushibawan.miniaccount.entity.BillEntity

@Database(version = 1, entities = [
    AccountEntity::class,BillEntity::class])
abstract class MiniDatabase:RoomDatabase(){

    abstract fun accountDao():AccountDao
    abstract fun billDao():BillDao

    companion object{
        @Volatile
        private var instance : MiniDatabase? = null

        @Synchronized
        fun getDatabase(context:Context):MiniDatabase{
            instance?.let {
                return it
            }
            return Room.databaseBuilder(context.applicationContext,
                MiniDatabase::class.java,"mini_database").build()
                .apply { instance = this }
        }
    }

}