package com.pgustavo.todoapplication.datasource

import androidx.lifecycle.LiveData
import androidx.room.*
import com.pgustavo.todoapplication.model.Task


@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task_table")
    fun getAll(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE id = :key")
    fun get(key: Int) : Task

}