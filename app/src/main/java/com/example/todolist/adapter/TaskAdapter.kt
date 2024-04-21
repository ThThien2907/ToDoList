package com.example.todolist.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.*
import com.example.todolist.Utils.DatabaseHelper
import com.example.todolist.databinding.ItemTaskBinding
import com.example.todolist.model.Task

class TaskAdapter(private var activity: MainActivity, private var db: DatabaseHelper): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    private lateinit var list: ArrayList<Task>
    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root){ }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = list[position]
        holder.binding.chkTodo.isChecked = if (task.status == 0) false else true
        holder.binding.chkTodo.text = task.task

        holder.binding.btnMore.setOnClickListener {
            val popupMenu = PopupMenu(activity, holder.binding.btnMore)
            popupMenu.menuInflater.inflate(R.menu.context_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.mnu_edit -> {
                        editTask(position)
                        true
                    }
                    R.id.mnu_delete -> {
                        deleteItem(position)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            popupMenu.show()
        }
        holder.binding.chkTodo.setOnCheckedChangeListener { buttonView, isChecked ->
            buttonView.setOnClickListener {
                if (isChecked){
                    db.updateStatus(task.id, 1)

                }
                else{
                    db.updateStatus(task.id, 0)
                }
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteItem(position: Int) {
        val task = list[position]
        val rs = db.deleteTask(task.id)
        if (rs >= 1){
            reload()
        }
    }

    private fun editTask(position: Int){
        val task = list[position]
        val bundle = Bundle()
        bundle.putInt("id", task.id)
        bundle.putString("task", task.task)
        val addNewTask = AddNewTask()
        addNewTask.arguments = bundle
        val manager = activity.supportFragmentManager
        addNewTask.show(manager, addNewTask.tag)
    }

    private fun reload() {
        activity.handleReLoad()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: ArrayList<Task>){
        this.list = list
        notifyDataSetChanged()
    }
}