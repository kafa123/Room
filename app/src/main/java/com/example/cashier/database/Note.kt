package com.example.cashier.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity(tableName = "Note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Long=0,
    @ColumnInfo(name="Title")
    val Title:String,
    @ColumnInfo(name = "Content")
    val Content: String,
    @ColumnInfo(name="Date")
    val Date:String
):Serializable {
    override fun toString(): String {
        return "Title:$Title\nContent:$Content\nDate:$Date"
    }

}
