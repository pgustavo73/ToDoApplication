package com.pgustavo.todoapplication.ui


import android.app.Activity
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
    var index: Int = 0

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
                binding.impHora.text = "${hour} : ${minute}"

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
            finish()
        }

        binding.btCancel.setOnClickListener {
            finish()
        }

    }

    fun insertTask(task: Task) {

        if (task.id == 0 ) {
            mTaskViewModel.addTask(task)
        }else {
            mTaskViewModel.updateTask(task.id, task.title, task.hour, task.date, )
        }

    }

    companion object {
        const val TASK_ID = "task_id"
    }
}

