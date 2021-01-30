package com.hasanakcay.todoo.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.hasanakcay.todoo.R
import kotlinx.android.synthetic.main.activity_note_detail.*
import java.util.ArrayList

class AlertDialogg() {
    fun createAlertBox(context: Context, header: EditText, date: TextView, note: EditText, category: TextView, priority: String) {
        val alert = AlertDialog.Builder(context)
        alert.setTitle("Warning!")
        var message = ""
        if (header.text.isEmpty()) {
            message += "Please fill in the header part!\n"
        }
        if (date.text.isEmpty()) {
            message += "Please fill in the date part!\n"
        }
        if (note.text.isEmpty()) {
            message += "Please fill in the note part!\n"
        }
        if (category.text.isEmpty()){
            message += "Please fill in the category part!\n"
        }
        if (priority.equals("Please choose a priority")){
            message += "You need to choose a priority!\n"
        }
        alert.setMessage(message)
        alert.setIcon(R.drawable.ic_baseline_warning)
        alert.setPositiveButton("Okey", DialogInterface.OnClickListener { _, _ ->
            Toast.makeText(context, "Thank You!", Toast.LENGTH_LONG).show()
        })
        alert.show()
    }
}