package ar.edu.uade.da2023.marvelapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CharacterLocal::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun charactersDao() : CharacterDao

    companion object{
        @Volatile
        private var _instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = _instance ?: synchronized(this){
            _instance ?: buildDatabase(context)
        }





        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java,"app_database")
                .fallbackToDestructiveMigration()
                .build()
    }
}