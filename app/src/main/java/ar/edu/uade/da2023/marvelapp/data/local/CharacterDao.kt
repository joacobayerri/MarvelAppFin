package ar.edu.uade.da2023.marvelapp.data.local

import androidx.room.*

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters")
    fun getAll() : List<CharacterLocal>

    @Query("SELECT * FROM characters WHERE id = :id LIMIT 1")
    fun getById(id: Int) : CharacterLocal

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg character: CharacterLocal)

    @Delete
    fun delete(character: CharacterLocal)

}