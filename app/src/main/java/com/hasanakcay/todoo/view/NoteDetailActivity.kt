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
import com.hasanakcay.todoo.util.SlideAnim
import io.realm.RealmList
import kotlinx.android.synthetic.main.activity_note_detail.*
import java.util.*

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var editText_header: EditText
    private lateinit var tv_date: TextView
    private lateinit var editText_note: EditText
    private var currentCategoriesList: RealmList<String>? = null
    var currentPriority: String? = null
    private var selectedItem: Note? = null
    private var currentId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        val priorityArray = resources.getStringArray(R.array.priorities)

        editText_header = findViewById(R.id.edittext_header)
        tv_date = findViewById(R.id.tv_date)
        editText_note = findViewById(R.id.edittext_note)

        currentId = intent.getIntExtra("selectedNote", 0)
        currentId?.let {
            selectedItem = RealmHelper().findNote(this, it)
        }

        if (selectedItem?.id == null) {
            editIcon.visibility = View.GONE
            buttonDelete.visibility = View.GONE
        } else {
            editText_header.setText(selectedItem?.header)
            tv_date.text = selectedItem?.date
            editText_note.setText(selectedItem?.note)
            tv_note_detail_name.text = selectedItem?.header
            var tempMassage = ""
            selectedItem?.categoriesList?.forEach { tempMassage += it }
            tv_categories.text = tempMassage
            priorityArray.forEach {
                if (selectedItem?.priorityId.equals(it)) {
                    spinner_priorities.setSelection(priorityArray.indexOf(it))
                }
            }
        }

        tv_categories.setOnClickListener {
            gl_categories.visibility = View.VISIBLE
            SlideAnim().slideUp(gl_categories, this)
        }

        spinner_priorities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentPriority = parent?.getItemAtPosition(position) as String
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        pickDate()

        cb_general.setOnCheckedChangeListener { _, _ ->
            updateCategoriesTextView()
        }
        cb_art.setOnCheckedChangeListener { _, _ ->
            updateCategoriesTextView()
        }
        cb_food.setOnCheckedChangeListener { _, _ ->
            updateCategoriesTextView()
        }
        cb_science.setOnCheckedChangeListener { _, _ ->
            updateCategoriesTextView()
        }
        cb_software.setOnCheckedChangeListener { _, _ ->
            updateCategoriesTextView()
        }
        cb_sport.setOnCheckedChangeListener { _, _ ->
            updateCategoriesTextView()
        }
    }

    @SuppressLint("SetTextI18n")
    fun pickDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        tv_date.setOnClickListener {
            val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
            tv_date.text = "" + mDay + "/" + (mMonth + 1) + "/" + mYear }, year, month, day)
            dialog.datePicker.minDate = System.currentTimeMillis() - 1000
            dialog.show()
        }
    }

    private fun getId(): Int {
        if (currentId != 0) {
            return currentId!!
        }
        val currentIdNum = RealmHelper().getId(this)
        var nextId = 0
        if (currentIdNum == null) {
            nextId = 1
        } else {
            nextId = currentIdNum.toInt() + 1
        }
        return nextId
    }

    private fun updateCategoriesTextView() {
        var categoriesTextView = ""
        if (cb_general.isChecked) {
            categoriesTextView += "${cb_general.text} "
        }
        if (cb_art.isChecked) {
            categoriesTextView += "${cb_art.text} "
        }
        if (cb_science.isChecked) {
            categoriesTextView += "${cb_science.text} "
        }
        if (cb_software.isChecked) {
            categoriesTextView += "${cb_software.text} "
        }
        if (cb_sport.isChecked) {
            categoriesTextView += "${cb_sport.text} "
        }
        if (cb_food.isChecked) {
            categoriesTextView += "${cb_food.text} "
        }
        tv_categories.text = categoriesTextView
    }

    private fun saveCategories() {
        if (cb_general.isChecked) {
            currentCategoriesList?.add(cb_general.text.toString())
        }
        if (cb_art.isChecked) {
            currentCategoriesList?.add(cb_art.text.toString())
        }
        if (cb_science.isChecked) {
            currentCategoriesList?.add(cb_science.text.toString())
        }
        if (cb_software.isChecked) {
            currentCategoriesList?.add(cb_software.text.toString())
        }
        if (cb_sport.isChecked) {
            currentCategoriesList?.add(cb_sport.text.toString())
        }
        if (cb_food.isChecked) {
            currentCategoriesList?.add(cb_food.text.toString())
        }
    }

    fun saveButton(view: View) {
        val alert = android.app.AlertDialog.Builder(this)
        AlertDialogg().createAlertBox(this,editText_header,tv_date,editText_note,tv_categories,currentPriority!!)
        alert.show()

        saveCategories()

        if (editText_header.text.isNotEmpty() && tv_date.text.isNotEmpty() && editText_note.text.isNotEmpty() && tv_categories.text.isNotEmpty() && !currentPriority.equals("Please choose a priority")) {
            val note = Note(getId(), editText_header.text.toString(), tv_date.text.toString(), editText_note.text.toString(), currentCategoriesList, currentPriority, "task")
            RealmHelper().saveNote(this, note)
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun deleteButton(view: View) {
        currentId?.let { RealmHelper().deleteNote(this, it) }
        startActivity(Intent(this, MainActivity::class.java))
    }
    fun backButton(view: View) {
        onBackPressed()
    }
}
