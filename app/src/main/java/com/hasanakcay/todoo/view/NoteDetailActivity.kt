package com.hasanakcay.todoo.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.hasanakcay.todoo.R
import com.hasanakcay.todoo.model.Note
import com.hasanakcay.todoo.util.AlertDialogg
import com.hasanakcay.todoo.util.RealmHelper
import kotlinx.android.synthetic.main.activity_note_detail.*
import java.util.*
import kotlin.collections.ArrayList

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var edittext_header: EditText
    private lateinit var tv_date: TextView
    private lateinit var edittext_note: EditText
    var currentPriority: String? = null
    private var selectedItem: Note? = null
    private var currentId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        val priorityArray = resources.getStringArray(R.array.priorities)

        edittext_header = findViewById(R.id.edittext_header)
        tv_date = findViewById(R.id.tv_date)
        edittext_note = findViewById(R.id.edittext_note)

        currentId = intent.getIntExtra("selectedNote", 0)
        currentId?.let {
            selectedItem = RealmHelper().findNote(this, it)
        }

        if (selectedItem?.id == null) {
            editIcon.visibility = View.GONE
            buttonDelete.visibility = View.GONE
        } else {
            edittext_header.setText(selectedItem?.header)
            tv_date.setText(selectedItem?.date)
            edittext_note.setText(selectedItem?.note)
            tv_note_detail_name.setText(selectedItem?.header)
            tv_categories.setText(selectedItem?.categoriesName)
            priorityArray.forEach {
                if (selectedItem?.priorityId.equals(it)){
                    spinner_priorities.setSelection(priorityArray.indexOf(it))
                }
            }
        }

        spinner_priorities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentPriority = parent?.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        pickDate()
    }

    @SuppressLint("SetTextI18n")
    fun pickDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        tv_date.setOnClickListener {
            val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    tv_date.setText("" + mDay + "/" + (mMonth + 1) + "/" + mYear)
                }, year, month, day)
            dialog.datePicker.minDate = calendar.timeInMillis
            dialog.show()
        }
    }

    fun backButton(view: View) {
        onBackPressed()
    }

    fun getId(): Int {
        if (currentId != 0) {
            return currentId!!
        }
        val currentIdNum = RealmHelper().getId(this)
        var nextId = 0
        if (currentIdNum == null) {
            nextId = 1
        }else {
            nextId = currentIdNum.toInt() + 1
        }
        return nextId
    }

    fun saveButton(view: View) {
        val alert = android.app.AlertDialog.Builder(this)
        AlertDialogg().createAlertBox(this, edittext_header,tv_date,edittext_note,tv_categories,currentPriority!!)
        alert.show()

        var categoriesTextView = ""
        if (cb_general.isChecked){
            categoriesTextView += "${cb_general.text} "
        }
        if (cb_art.isChecked){
            categoriesTextView += "${cb_art.text} "
        }
        if (cb_science.isChecked){
            categoriesTextView += "${cb_science.text} "
        }
        if (cb_software.isChecked){
            categoriesTextView += "${cb_software.text} "
        }
        if (cb_sport.isChecked){
            categoriesTextView += "${cb_sport.text} "
        }
        if (cb_food.isChecked){
            categoriesTextView += "${cb_food.text} "
        }
        tv_categories.setText(categoriesTextView)
        

        if (edittext_header.text.isNotEmpty() && tv_date.text.isNotEmpty() && edittext_note.text.isNotEmpty() && tv_categories.text.isNotEmpty() && !currentPriority.equals("Please choose a priority")) {
            val note = Note(getId(), edittext_header.text.toString(), tv_date.text.toString(), edittext_note.text.toString(), tv_categories.text.toString(), currentPriority, "task")
            RealmHelper().saveNote(this, note)
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun deleteButton(view: View) {
        currentId?.let { RealmHelper().deleteNote(this, it) }
        startActivity(Intent(this, MainActivity::class.java))
    }
}
