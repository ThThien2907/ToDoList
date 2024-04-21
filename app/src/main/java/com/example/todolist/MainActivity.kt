package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.Utils.DatabaseHelper
import com.example.todolist.adapter.TaskAdapter
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.model.Task

class MainActivity : AppCompatActivity(), ReLoad{
    private lateinit var binding: ActivityMainBinding
    private lateinit var listTask: ArrayList<Task>
    private lateinit var db: DatabaseHelper
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)
        listTask = db.getAllTask()


        binding.layoutTask.layoutManager = LinearLayoutManager(this)
        adapter = TaskAdapter(this@MainActivity, db)
        adapter.setList(listTask)
        binding.layoutTask.adapter = adapter



        binding.btnNewTask.setOnClickListener {
            val addNewTask = AddNewTask()
            addNewTask.show(supportFragmentManager, addNewTask.tag)
        }
    }

    override fun handleReLoad() {
        listTask = db.getAllTask()
        adapter.setList(listTask)
    }
}