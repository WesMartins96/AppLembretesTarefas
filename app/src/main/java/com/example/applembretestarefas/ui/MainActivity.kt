package com.example.applembretestarefas.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.applembretestarefas.databinding.ActivityMainBinding
import com.example.applembretestarefas.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { ListAdapterTask() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        updateList()
        insertListeners()

    }

    private fun insertListeners() {
        binding.btnAddTasks.setOnClickListener {
            startActivityForResult(Intent(this, AddActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra(AddActivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }

        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK){
            binding.rvTasks.adapter = adapter
            adapter.submitList(TaskDataSource.getList())
        }
            updateList()

    }

    private fun updateList(){
        val  list = TaskDataSource.getList()
        binding.includeEmpty.emptyState.visibility = if (list.isEmpty()) View.VISIBLE
         else View.GONE

        adapter.submitList(list)
    }

    companion object {
        private const val CREATE_NEW_TASK = 1000
    }

}