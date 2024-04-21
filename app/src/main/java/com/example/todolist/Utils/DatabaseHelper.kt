package com.example.todolist.Utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todolist.model.Task

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, "todolist", null, 1) {
    companion object {
        const val ID = "id"
        const val STATUS = "status"
        const val TASK = "task"
        const val TABLE = "todo"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE "+TABLE+"(" +
                ID+ " Integer PRIMARY KEY AUTOINCREMENT, " +
                STATUS+ " Integer, " +
                TASK + " Text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }


    @SuppressLint("Range", "Recycle")
    fun getAllTask() : ArrayList<Task>{
        val listTask : ArrayList<Task> = ArrayList()
        val db = this.readableDatabase
        val rs = db.rawQuery("SELECT * FROM $TABLE", null)

        if (rs.moveToFirst()) {
            do {
                val task = Task(0, "", 0)
                task.id = rs.getInt(rs.getColumnIndex(ID))
                task.task = rs.getString(rs.getColumnIndex(TASK))
                task.status = rs.getInt(rs.getColumnIndex(STATUS))

                listTask.add(task)
            } while (rs.moveToNext())
        }
        db.close()
        return listTask
    }

    fun addNewTask(task: Task): Long {
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(STATUS, 0)
        cv.put(TASK,task.task)
        val rs = db.insert(TABLE,null, cv)
        db.close()
        return rs
    }

    fun updateStatus(id: Int, status: Int): Int{
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(STATUS, status)
        val rs = db.update(TABLE,cv, "$ID=?", arrayOf(id.toString()))
        db.close()
        return rs
    }

    fun updateTask(id: Int, task: String): Int{
        val db = writableDatabase
        val cv = ContentValues()
        cv.put(TASK, task)
        val rs = db.update(TABLE,cv, "$ID=?", arrayOf(id.toString()))
        db.close()
        return rs
    }

    fun deleteTask(id: Int): Int{
        val db = this.writableDatabase
        val rs = db.delete(TABLE, "$ID=?", arrayOf(id.toString()))
        db.close()
        return rs
    }
}