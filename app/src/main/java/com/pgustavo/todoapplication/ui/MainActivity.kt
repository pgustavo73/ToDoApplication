package com.pgustavo.todoapplication.ui

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import com.pgustavo.todoapplication.databinding.ActivityMainBinding
import com.pgustavo.todoapplication.datasource.TaskDatabase
import com.pgustavo.todoapplication.model.Task
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pgustavo.todoapplication.viewmodel.TaskViewModel
import org.jetbrains.anko.internals.AnkoInternals.createAnkoContext


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var mTaskViewModel: TaskViewModel
    private val adapter by lazy { TaskListAdapter() }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter
        updateList()
        insertListerners()

    }

    private fun insertListerners() {
        binding.btNewTask.setOnClickListener {
            startActivityForResult(Intent (this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK_ID, it)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete = {
            mTaskViewModel.deleteTask(it)
            cancelAlarm()
            updateList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK)
            updateList()
        }

    private fun updateList() {

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        mTaskViewModel.readAllData.observe(this, Observer { task ->
            adapter.submitList(task)

            binding.includeEmpty.emptyState.visibility = if (task.isEmpty()) View.VISIBLE
            else View.GONE

        })

    }

    fun cancelAlarm() {

        var alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)

        var pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent, PendingIntent.FLAG_NO_CREATE
        )

        alarmManager.cancel(pendingIntent)

    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }
}

