package com.example.cashier.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Note)

    @Update
    fun update(note: Note)

    @Delete
    fun delete(note: Note)

    @get:Query("SELECT * from Note_table ORDER BY id DESC")
    val allNotes: LiveData<List<Note>>

    @Query("SELECT * from Note_table Where id= :NoteId")
    fun getNoteById(NoteId:Long):Note?
}
