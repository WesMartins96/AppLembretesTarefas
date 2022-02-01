package com.example.applembretestarefas.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.applembretestarefas.databinding.ActivityAddBinding
import com.example.applembretestarefas.datasource.TaskDataSource
import com.example.applembretestarefas.extensions.format
import com.example.applembretestarefas.extensions.text
import com.example.applembretestarefas.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.title.text = it.title
                binding.date.text = it.date
                binding.hour.text = it.hour
            }
        }

        insertListeners()

    }

    private fun insertListeners() {
        binding.date.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offSet = timeZone.getOffset(Date().time) * -1
                binding.date.text = Date(it + offSet).format()
            }
            datePicker.show(supportFragmentManager, "DAT_PICKER_TAG")
        }

        binding.hour.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.hour.text = "${hour}:${minute}"

            }
            timePicker.show(supportFragmentManager, null)

        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnCreate.setOnClickListener {
            val task = Task(
                title = binding.title.text,
                date = binding.date.text,
                hour = binding.hour.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)


            setResult(Activity.RESULT_OK)
            finish()
        }

    }

    companion object {
        const val TASK_ID = "task_id"
    }


}