package com.hasanakcay.todoo.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import com.hasanakcay.todoo.R

class CustomAlertDialog {
    companion object{
        fun createAlertBox(context: Context, header: EditText, date: TextView, note: EditText, category: TextView, priority: String) {
            val layoutBuilder = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null)
            val builder = AlertDialog.Builder(context).setView(layoutBuilder)
            val alertDialog : AlertDialog = builder.show()

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
            /*
            alert.setMessage(message)

            alert.setIcon(R.drawable.ic_baseline_warning)

            alert.setPositiveButton("Okey") { _, _ ->
                Toast.makeText(context, "Thank You!", Toast.LENGTH_LONG).show()
            }
            alert.show()

             */

        }
    }

}