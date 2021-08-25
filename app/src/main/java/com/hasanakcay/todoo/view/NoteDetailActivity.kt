package com.hasanakcay.todoo.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.hasanakcay.todoo.R
import com.hasanakcay.todoo.base.BaseActivity
import com.hasanakcay.todoo.databinding.ActivityNoteDetailBinding
import com.hasanakcay.todoo.model.Note
import com.hasanakcay.todoo.util.RealmHelper
import com.hasanakcay.todoo.util.SlideAnim
import io.realm.RealmList
import java.text.SimpleDateFormat
import java.util.*

class NoteDetailActivity : BaseActivity<ActivityNoteDetailBinding>() {

    private var currentCategoriesList: RealmList<String> = RealmList()
    var currentPriority: String? = null
    private var selectedItem: Note? = null
    private var currentId: Int? = null
    var tempMassage = ""

    override fun getViewBinding(): ActivityNoteDetailBinding {
        return ActivityNoteDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_detail)

        val priorityArray = resources.getStringArray(R.array.priorities)

        currentId = intent.getIntExtra("selectedNote", 0)
        currentId?.let {
            selectedItem = RealmHelper().findNote(this, it)
        }

        if (selectedItem?.id == null) {
            binding.editIcon.visibility = View.GONE
            binding.buttonDelete.visibility = View.GONE
        } else {
            binding.buttonDelete.visibility = View.GONE
            binding.buttonSave.visibility = View.GONE
            binding.etHeader.apply {
                isClickable = false
                isCursorVisible = false
                isFocusable = false
                isFocusableInTouchMode = false
            }
            binding.etNote.apply {
                isClickable = false
                isCursorVisible = false
                isFocusable = false
                isFocusableInTouchMode = false
            }
            binding.tvDate.isEnabled = false
            binding.tvCategories.isEnabled = false
            binding.spinnerPriorities.isEnabled = false

            binding.editIcon.setOnClickListener {
                binding.etHeader.apply {
                    isClickable = true
                    isCursorVisible = true
                    isFocusable = true
                    isFocusableInTouchMode = true
                }
                binding.etNote.apply {
                    isClickable = true
                    isCursorVisible = true
                    isFocusable = true
                    isFocusableInTouchMode = true
                }
                binding.tvDate.isEnabled = true
                binding.tvCategories.isEnabled = true
                binding.spinnerPriorities.isEnabled = true
                binding.buttonDelete.visibility = View.VISIBLE
                binding.buttonSave.visibility = View.VISIBLE
            }

            binding.etHeader.setText(selectedItem?.header)
            binding.tvDate.text = selectedItem?.date
            binding.etNote.setText(selectedItem?.note)
            binding.tvNoteDetailName.text = selectedItem?.header
            selectedItem?.categoriesList?.forEach {
                tempMassage += "$it "
                currentCategoriesList.add(it)
            }
            binding.tvCategories.text = tempMassage
            priorityArray.forEach {
                if (selectedItem?.priorityId.equals(it)) {
                    binding.spinnerPriorities.setSelection(priorityArray.indexOf(it))
                }
            }
        }

        binding.tvCategories.setOnClickListener {
            hideKeyboard()
            binding.glCategories.visibility = View.VISIBLE
            SlideAnim().slideUp(binding.glCategories, this)
        }

        binding.spinnerPriorities.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currentPriority = parent?.getItemAtPosition(position) as String
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        pickDate()

        binding.cbGeneral.setOnCheckedChangeListener { _, _ ->
            updateCategoriesTextView()
        }
        binding.cbArt.setOnCheckedChangeListener { _, _ ->
            updateCategoriesTextView()
        }
        binding.cbFood.setOnCheckedChangeListener { _, _ ->
            updateCategoriesTextView()
        }
        binding.cbScience.setOnCheckedChangeListener { _, _ ->
            updateCategoriesTextView()
        }
        binding.cbSoftware.setOnCheckedChangeListener { _, _ ->
            updateCategoriesTextView()
        }
        binding.cbSport.setOnCheckedChangeListener { _, _ ->
            updateCategoriesTextView()
        }
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
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

        binding.tvDate.setOnClickListener {
            val dialog = DatePickerDialog(
                this,
                { _, mYear, mMonth, mDay ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR, mYear)
                    selectedDate.set(Calendar.MONTH, mMonth)
                    selectedDate.set(Calendar.DAY_OF_MONTH, mDay)
                    val date = formatter.format(selectedDate.time)
                    binding.tvDate.text = date
                },
                year,
                month,
                day
            )
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
        nextId = if (currentIdNum == null) {
            1
        } else {
            currentIdNum.toInt() + 1
        }
        return nextId
    }

    private fun updateCategoriesTextView() {
        var categoriesTextView = ""
        if (binding.cbGeneral.isChecked) {
            categoriesTextView += "${binding.cbGeneral.text} "
        }
        if (binding.cbArt.isChecked) {
            categoriesTextView += "${binding.cbArt.text} "
        }
        if (binding.cbScience.isChecked) {
            categoriesTextView += "${binding.cbScience.text} "
        }
        if (binding.cbSoftware.isChecked) {
            categoriesTextView += "${binding.cbSoftware.text} "
        }
        if (binding.cbSport.isChecked) {
            categoriesTextView += "${binding.cbSport.text} "
        }
        if (binding.cbFood.isChecked) {
            categoriesTextView += "${binding.cbFood.text} "
        }
        binding.tvCategories.text = categoriesTextView
    }

    private fun saveCategories() {
        if (binding.cbGeneral.isChecked) {
            currentCategoriesList.add(binding.cbGeneral.text.toString())
        }
        if (binding.cbArt.isChecked) {
            currentCategoriesList.add(binding.cbArt.text.toString())
        }
        if (binding.cbScience.isChecked) {
            currentCategoriesList.add(binding.cbScience.text.toString())
        }
        if (binding.cbSoftware.isChecked) {
            currentCategoriesList.add(binding.cbSoftware.text.toString())
        }
        if (binding.cbSport.isChecked) {
            currentCategoriesList.add(binding.cbSport.text.toString())
        }
        if (binding.cbFood.isChecked) {
            currentCategoriesList.add(binding.cbFood.text.toString())
        }
    }

    fun saveButton(view: View) {

        if (selectedItem?.id == null) {
            saveCategories()
        }

        if (binding.etHeader.text.isNotEmpty() &&
            binding.tvDate.text.isNotEmpty() &&
            binding.etNote.text.isNotEmpty() &&
            binding.tvCategories.text.isNotEmpty() &&
            !currentPriority.equals(
                "Please choose a priority"
            )
        ) {
            val note = Note(
                getId(),
                binding.etHeader.text.toString(),
                binding.tvDate.text.toString(),
                binding.etNote.text.toString(),
                currentCategoriesList,
                currentPriority,
                "task"
            )
            RealmHelper().saveNote(this, note)
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            com.hasanakcay.todoo.util.CustomAlertDialog()
                .createAlertBox(
                    this,
                    binding.etHeader,
                    binding.tvDate,
                    binding.etNote,
                    binding.tvCategories,
                    currentPriority!!
                )
        }
    }

    fun deleteButton(view: View) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Are You Sure?")
        alert.setIcon(R.drawable.ic_baseline_warning)
        alert.setMessage("Do you want to delete this note?")
        alert.setPositiveButton("YES") { _, _ ->
            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
            currentId?.let { RealmHelper().deleteNote(this, it) }
            startActivity(Intent(this, MainActivity::class.java))
        }
        alert.setNegativeButton("NO") { _, _ ->
            Toast.makeText(this, "Not Deleted!", Toast.LENGTH_SHORT).show()
        }
        alert.show()
    }

    fun backButton(view: View) {
        onBackPressed()
    }


}
