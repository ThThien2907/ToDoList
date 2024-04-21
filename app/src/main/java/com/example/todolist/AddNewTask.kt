package com.example.todolist

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.todolist.Utils.DatabaseHelper
import com.example.todolist.databinding.NewTaskBinding
import com.example.todolist.model.Task
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewTask: BottomSheetDialogFragment() {
    private lateinit var binding: NewTaskBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogBottomSheet)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = NewTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor", "ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = DatabaseHelper(this.requireActivity())
        binding.edtNewTask.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("ResourceType")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnSave.isEnabled = s!!.isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        var isUpdate = false
        val bundle = arguments
        if (bundle != null){
            isUpdate = true
            val task:String = bundle.getString("task").toString()

            binding.edtNewTask.setText(task)
            if (task.length > 0)
                binding.btnSave.isEnabled = true

        }

        binding.btnSave.setOnClickListener {
            val text = binding.edtNewTask.text.toString()

            if (isUpdate){
                val id: Int? = bundle?.getInt("id")
                    val rs = db.updateTask(id!!, text)
                    if (rs >= 1)
                        Toast.makeText(activity, "Updated", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(activity, "Update Error", Toast.LENGTH_SHORT).show()
            }else{
                val task = Task(0, text, 0)
                db.addNewTask(task)

            }
            dismiss()
            reload()
        }
    }

    private fun reload() {
        val activity = activity
        if (activity is ReLoad)
            activity.handleReLoad()
    }

}