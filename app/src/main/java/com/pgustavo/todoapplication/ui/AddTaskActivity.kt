package com.pgustavo.todoapplication.ui


import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.pgustavo.todoapplication.databinding.ActivityAddTaskBinding
import com.pgustavo.todoapplication.extensions.format
import com.pgustavo.todoapplication.extensions.text
import com.pgustavo.todoapplication.model.Task
import com.pgustavo.todoapplication.viewmodel.TaskViewModel
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var mTaskViewModel: TaskViewModel
    private lateinit var binding: ActivityAddTaskBinding
    private val notificationId = 1
    var index: Int = 0
    var notification : String = "N"
    var dateSet: Int = 0
    var minuteSet: Int = 0
    var hourSet: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)


        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        if (intent.hasExtra(TASK_ID)) {
             intent.extras?.getParcelable<Task>(TASK_ID)
                ?.let {
                binding.txtTitle.text = it.title
                binding.impData.text = it.date
                binding.impHora.text = it.hour
                index = it.id
                notification = it.title
                }

        }
        insertListeners()
    }

    private fun insertListeners() {
        binding.impData.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.impData.text = Date(it + offset).format()
                dateSet = Date(it + offset).format().take(2).toInt()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.impHora.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.impHora.text = "${hour}:${minute}"
                minuteSet = timePicker.minute
                hourSet = timePicker.hour

            }
            timePicker.show(supportFragmentManager, null)

        }

        binding.btCriar.setOnClickListener {
            val task = Task (
                title = binding.txtTitle.text,
                date = binding.impData.text,
                hour = binding.impHora.text,
                id = index
                        )
            insertTask(task)
            setResult(Activity.RESULT_OK)
            setAlarm()
            finish()
        }

        binding.btCancel.setOnClickListener {
            finish()

    }

    }
    fun setAlarm() {

        val intent = Intent(this@AddTaskActivity, AlarmReceiver::class.java)
        intent.putExtra("notificationId", notificationId)
        if (notification == "N") {
            notification = binding.txtTitle.text
        }
        intent.putExtra("message", notification)

        var pendingIntent = PendingIntent.getBroadcast(
            this@AddTaskActivity, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT
        )

        var alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val startTime = Calendar.getInstance()
        startTime[Calendar.HOUR_OF_DAY] = hourSet
        startTime[Calendar.MINUTE] = minuteSet
        startTime[Calendar.SECOND] = 0
        startTime[Calendar.DATE] = dateSet

        val alarmStartTime = startTime.timeInMillis

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmStartTime, pendingIntent)

    }

    fun insertTask(task: Task) {

        if (task.id == 0 ) {
            mTaskViewModel.addTask(task)
        }else {
            mTaskViewModel.updateTask(task.id, task.title, task.hour, task.date)
        }

    }

    companion object {
        const val TASK_ID = "task_id"
    }
}