package com.hasanakcay.todoo.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.hasanakcay.todoo.R
import com.hasanakcay.todoo.model.Note
import com.hasanakcay.todoo.util.AlertDialogg
import com.hasanakcay.todoo.util.RealmHelper
import com.hasanakcay.todoo.util.SlideAnim
import io.realm.RealmList
import kotlinx.android.synthetic.main.activity_note_detail.*
import java.text.SimpleDateFormat
import java.util.*

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var et_header: EditText
    private lateinit var tv_date: TextView
    private lateinit var et_note: EditText
    private var currentCategoriesList: RealmList<String> = RealmList()
    var currentPriority: String? = null
    private var selectedItem: Note? = null
    private var currentId: Int? = null
    var tempMassage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        val priorityArray = resources.getStringArray(R.array.priorities)

        et_header = findViewById(R.id.et_header)
        tv_date = findViewById(R.id.tv_date)
        et_note = findViewById(R.id.et_note)

        currentId = intent.getIntExtra("selectedNote", 0)
        currentId?.let {
            selectedItem = RealmHelper().findNote(this, it)
        }

        if (selectedItem?.id == null) {
            editIcon.visibility = View.GONE
            buttonDelete.visibility = View.GONE
        } else {
            buttonDelete.visibility = View.GONE
            buttonSave.visibility = View.GONE
            et_header.apply {
                isClickable = false
                isCursorVisible = false
                isFocusable = false
                isFocusableInTouchMode = false
            }
            et_note.apply {
                isClickable = false
                isCursorVisible = false
                isFocusable = false
                isFocusableInTouchMode = false
            }
            tv_date.isEnabled =false
            tv_categories.isEnabled = false
            spinner_priorities.isEnabled = false

            editIcon.setOnClickListener {
                et_header.apply {
                    isClickable = true
                    isCursorVisible = true
                    isFocusable = true
                    isFocusableInTouchMode = true
                }
                et_note.apply {
                    isClickable = true
                    isCursorVisible = true
                    isFocusable = true
                    isFocusableInTouchMode = true
                }
                tv_date.isEnabled =true
                tv_categories.isEnabled = true
                spinner_priorities.isEnabled = true
                buttonDelete.visibility = View.VISIBLE
                buttonSave.visibility = View.VISIBLE
            }

            et_header.setText(selectedItem?.header)
            tv_date.text = selectedItem?.date
            et_note.setText(selectedItem?.note)
            tv_note_detail_name.text = selectedItem?.header
            selectedItem?.categoriesList?.forEach {
                tempMassage += "$it "
                currentCategoriesList.add(it)
            }
            tv_categories.text = tempMassage
            priorityArray.forEach {
                if (selectedItem?.priorityId.equals(it)) {
                    spinner_priorities.setSelection(priorityArray.indexOf(it))
                }
            }
        }

        tv_categories.setOnClickListener {
            hideKeyboard()
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

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null){
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }

    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    fun pickDate() {
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        tv_date.setOnClickListener {
            val dialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,mYear)
                selectedDate.set(Calendar.MONTH,mMonth)
                selectedDate.set(Calendar.DAY_OF_MONTH,mDay)
                val date = formatter.format(selectedDate.time)
                tv_date.text = date }, year, month, day)
            dialog.datePicker.minDate = calendar.timeInMillis
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
            currentCategoriesList.add(cb_general.text.toString())
        }
        if (cb_art.isChecked) {
            currentCategoriesList.add(cb_art.text.toString())
        }
        if (cb_science.isChecked) {
            currentCategoriesList.add(cb_science.text.toString())
        }
        if (cb_software.isChecked) {
            currentCategoriesList.add(cb_software.text.toString())
        }
        if (cb_sport.isChecked) {
            currentCategoriesList.add(cb_sport.text.toString())
        }
        if (cb_food.isChecked) {
            currentCategoriesList.add(cb_food.text.toString())
        }
    }

    fun saveButton(view: View) {

        if(selectedItem?.id == null){
            saveCategories()
        }

        if (et_header.text.isNotEmpty() && tv_date.text.isNotEmpty() && et_note.text.isNotEmpty() && tv_categories.text.isNotEmpty() && !currentPriority.equals("Please choose a priority")) {
            val note = Note(getId(), et_header.text.toString(), tv_date.text.toString(), et_note.text.toString(), currentCategoriesList, currentPriority, "task")
            RealmHelper().saveNote(this, note)
            startActivity(Intent(this, MainActivity::class.java))
        } else{
            AlertDialogg().createAlertBox(this,et_header,tv_date,et_note,tv_categories,currentPriority!!)
        }
    }

    fun deleteButton(view: View) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Are You Sure?")
        alert.setIcon(R.drawable.ic_baseline_warning)
        alert.setMessage("Do you want to delete this note?")
        alert.setPositiveButton("YES", DialogInterface.OnClickListener { _, _ ->
            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
            currentId?.let { RealmHelper().deleteNote(this, it) }
            startActivity(Intent(this, MainActivity::class.java))
        })
        alert.setNegativeButton("NO", DialogInterface.OnClickListener { _, _ ->
            Toast.makeText(this, "Not Deleted!", Toast.LENGTH_SHORT).show()
        })
        alert.show()
    }
    fun backButton(view: View) {
        onBackPressed()
    }
}
