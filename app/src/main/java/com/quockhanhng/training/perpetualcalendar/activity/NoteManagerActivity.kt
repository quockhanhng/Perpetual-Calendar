package com.quockhanhng.training.perpetualcalendar.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quockhanhng.training.perpetualcalendar.R
import com.quockhanhng.training.perpetualcalendar.SharedPrefUtil
import com.quockhanhng.training.perpetualcalendar.adapter.NoteAdapter
import com.quockhanhng.training.perpetualcalendar.model.Note
import kotlinx.android.synthetic.main.activity_note_manager.*

class NoteManagerActivity : AppCompatActivity() {

    private lateinit var noteList: ArrayList<Note>
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var dialog: AlertDialog
    private var order = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_manager)
    }

    override fun onResume() {
        super.onResume()
        initComponents()
    }

    private fun initComponents() {
        val noteListString = SharedPrefUtil.getData(SharedPrefUtil.KEY_LIST_NOTE)
        noteList = ArrayList()
        if (!noteListString.equals(""))
            noteList = Gson().fromJson(noteListString, object : TypeToken<List<Note>>() {}.type)

        noteAdapter = NoteAdapter(noteList) { note, index -> onClickNote(note, index) }

        noteRecyclerView.layoutManager = LinearLayoutManager(this)
        noteRecyclerView.adapter = noteAdapter
    }

    private fun onClickNote(note: Note, index: Int) {
        val intent = Intent(applicationContext, AddOrEditNoteActivity::class.java)
        intent.putExtra("Selected Note", note)
        intent.putExtra("Index", index)

        startActivity(intent)
    }

    fun addNewNote(view: View) {
        startActivity(Intent(applicationContext, AddOrEditNoteActivity::class.java))
    }

    fun changeOrder(view: View) {
        showChangeOrderDialog()
    }

    private fun showChangeOrderDialog() {
        val array = arrayOf(
            "Thứ tự nhập tăng dần",
            "Thứ tự nhập giảm dần",
            "Ngày sự kiện tăng dần",
            "Ngày sự kiện giảm dần"
        )
        val builder = AlertDialog.Builder(this@NoteManagerActivity).apply {
            setTitle("Chọn cách sắp xếp theo")
            setSingleChoiceItems(array, order) { _, index ->
                when (index) {
                    0 -> noteAdapter.sortByTimeCreated()
                    1 -> noteAdapter.sortByTimeCreatedDescending()
                    2 -> noteAdapter.sortByDate()
                    3 -> noteAdapter.sortByDateDescending()
                }
                order = index
                dialog.dismiss()
            }
        }
        dialog = builder.create()
        dialog.show()
    }
}
