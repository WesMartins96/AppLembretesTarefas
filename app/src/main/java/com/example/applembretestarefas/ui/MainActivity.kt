package com.example.applembretestarefas.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import com.example.applembretestarefas.databinding.ActivityMainBinding
import com.example.applembretestarefas.datasource.TaskDataSource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { ListAdapterTask() }

    /**
     * Nova maneira de iniciar uma activity.
     * Já que `startActivityForResult` foi depreciado.
     */

    private val register =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) updateList()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTasks.adapter = adapter

        updateList()
        insertListeners()

    }

    private fun insertListeners() {
        binding.btnAddTasks.setOnClickListener {
            register.launch(Intent(this, AddActivity::class.java))
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddActivity::class.java)
            intent.putExtra(AddActivity.TASK_ID, it.id)
            register.launch(intent)
        }

        adapter.listenerDelete = {
            TaskDataSource.deleteTask(it)
            updateList()
        }

    }

    private fun updateList(){
        val list = TaskDataSource.getList()
        binding.includeEmpty.emptyState.visibility = if (list.isEmpty()) View.VISIBLE
         else View.GONE

        adapter.submitList(list)
    }

}