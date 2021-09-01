package com.hasanakcay.todoo.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hasanakcay.todoo.R
import com.hasanakcay.todoo.data.RealmDbHelper
import com.hasanakcay.todoo.data.db.Note
import com.hasanakcay.todoo.databinding.FragmentNoteDetailBinding
import com.hasanakcay.todoo.ui.base.BaseFragment
import com.hasanakcay.todoo.util.ActivityButtonListener
import com.hasanakcay.todoo.util.CustomAlertDialog
import com.hasanakcay.todoo.util.FragmentListener
import com.hasanakcay.todoo.util.SlideAnim
import io.realm.Realm
import io.realm.RealmList
import java.text.SimpleDateFormat
import java.util.*

class NoteDetailFragment : BaseFragment<FragmentNoteDetailBinding>(), ActivityButtonListener {

    private var currentCategoriesList: RealmList<String> = RealmList()
    var currentPriority: String? = null
    private var selectedItem: Note? = null
    private var currentId: Int? = null
    var tempMassage = ""
    lateinit var realm: Realm
    lateinit var navController: NavController
    lateinit var fragmentListener: FragmentListener
    private val checkboxGroup = mutableListOf(
        binding.cbArt,
        binding.cbFood,
        binding.cbGeneral,
        binding.cbScience,
        binding.cbSoftware,
        binding.cbSport
    )

    override fun getViewBinding(): FragmentNoteDetailBinding {
        return FragmentNoteDetailBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setOnActivityButtonListener(this)

        fragmentListener = activity as FragmentListener
        fragmentListener.whichFragment(R.id.noteDetailFragment)



        Realm.init(binding.root.context)
        realm = Realm.getDefaultInstance()

        navController = findNavController()

        actions()
        val priorityArray = resources.getStringArray(R.array.priorities)

        val args: NoteDetailFragmentArgs by navArgs()
        currentId = args.selectedNote
        currentId?.let {
            selectedItem = RealmDbHelper().findNote(realm, it)
        }


        if (selectedItem?.id == null) {
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

            binding.etHeader.setText(selectedItem?.header)
            binding.tvDate.text = selectedItem?.date
            binding.etNote.setText(selectedItem?.note)
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
            SlideAnim().slideUp(binding.glCategories, binding.root.context)
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

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        pickDate()

        for (checkbox in checkboxGroup) {
            checkbox.setOnCheckedChangeListener { _, _ ->
                updateCategoriesTextView()
            }
        }
    }

    override fun clickEditIcon(isClicked: Boolean) {
        if (isClicked) {
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
    }

    override fun clickBackIcon(isClicked: Boolean) {
        if (isClicked) {
            if (!navController.popBackStack()) {
                //finish
            }
        }
    }


    private fun hideKeyboard() {
        activity?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
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
                binding.root.context,
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

    private fun getIdNum(): Int {
        if (currentId != 0) {
            return currentId!!
        }
        val currentIdNum = RealmDbHelper().getId(realm)
        val nextId: Int = if (currentIdNum == null) {
            1
        } else {
            currentIdNum.toInt() + 1
        }
        return nextId
    }

    private fun updateCategoriesTextView() {
        var categoriesTextView = ""
        for (checkbox in checkboxGroup){
            if (checkbox.isChecked){
                categoriesTextView += "${checkbox.text}"
            }
        }
        binding.tvCategories.text = categoriesTextView
    }

    private fun saveCategories() {
        for (checkbox in checkboxGroup){
            if (checkbox.isChecked){
                currentCategoriesList.add(checkbox.text.toString())
            }
        }
    }

    private fun actions() {
        binding.buttonSave.setOnClickListener {
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
                    getIdNum(),
                    currentCategoriesList,
                    binding.etHeader.text.toString(),
                    binding.tvDate.text.toString(),
                    binding.etNote.text.toString(),
                    currentPriority,
                    "task"
                )
                RealmDbHelper().saveNote(realm, note)
                val action = NoteDetailFragmentDirections.noteDetailFragmentToNotesFragment()
                navController.navigate(action)
            } else {
                CustomAlertDialog.createAlertBox(
                    binding.root.context,
                    binding.etHeader,
                    binding.tvDate,
                    binding.etNote,
                    binding.tvCategories,
                    currentPriority!!
                )
            }
        }

        binding.buttonDelete.setOnClickListener {
            val alert = AlertDialog.Builder(binding.root.context)
            alert.setTitle("Are You Sure?")
            alert.setIcon(R.drawable.ic_baseline_warning)
            alert.setMessage("Do you want to delete this note?")
            alert.setPositiveButton("YES") { _, _ ->
                Toast.makeText(binding.root.context, "Deleted!", Toast.LENGTH_SHORT).show()
                currentId?.let { RealmDbHelper().deleteNote(realm, it) }
                val action = NoteDetailFragmentDirections.noteDetailFragmentToNotesFragment()
                navController.navigate(action)
            }
            alert.setNegativeButton("NO") { _, _ ->
                Toast.makeText(binding.root.context, "Not Deleted!", Toast.LENGTH_SHORT).show()
            }
            alert.show()
        }
    }

    override fun onStop() {
        super.onStop()
        realm.close()
    }


}