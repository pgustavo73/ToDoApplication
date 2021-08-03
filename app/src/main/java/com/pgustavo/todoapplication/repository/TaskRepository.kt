package com.pgustavo.todoapplication.repository

import androidx.lifecycle.LiveData
import com.pgustavo.todoapplication.datasource.TaskDao
import com.pgustavo.todoapplication.model.Task

class TaskRepository(private val taskDao: TaskDao) {

    val readAllData: LiveData<List<Task>> = taskDao.getAll()


    suspend fun addTask(task: Task){
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task){
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }

}