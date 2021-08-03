package com.pgustavo.todoapplication.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.pgustavo.todoapplication.datasource.TaskDatabase
import com.pgustavo.todoapplication.model.Task
import com.pgustavo.todoapplication.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TaskViewModel (application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Task>>
    private val repository: TaskRepository

    init {
        val taskDao = TaskDatabase.getDatabase(
            application
        ).taskDao()
        repository = TaskRepository(taskDao)
        readAllData = repository.readAllData
    }

        fun addTask(task: Task) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.addTask(task)
            }
        }

        fun updateTask(id: Int, title: String, hour: String,date: String) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.updateTask(Task(id = id, title = title,date =  date,hour =  hour))
            }
        }

        fun deleteTask(task: Task) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.deleteTask(task)
            }
        }

    }

