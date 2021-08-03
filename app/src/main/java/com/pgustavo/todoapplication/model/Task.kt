package com.pgustavo.todoapplication.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "task_table")
data class Task(
    val title: String,
    val hour: String,
    val date: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int
): Parcelable


