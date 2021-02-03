package com.hasanakcay.todoo.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.*
import com.hasanakcay.todoo.R

class AlertDialogg() {
    fun createAlertBox(context: Context, header: EditText, date: TextView, note: EditText, category: TextView, priority: String) {
        val alert = AlertDialog.Builder(context)
/*
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.alert_dialog)
        val titleAlertText = dialog.findViewById<TextView>(R.id.tvTitleAlert)
        titleAlertText.text = "Warning!"
*/

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
/*
        val msgAlertText = dialog.findViewById<TextView>(R.id.tvMsgAlert)
        msgAlertText.text = message
*/
        alert.setMessage(message)

/*
        val alertIcon = dialog.findViewById<ImageView>(R.id.ivIconAlert)
        alertIcon.setImageResource(R.drawable.ic_baseline_warning)
*/

        alert.setIcon(R.drawable.ic_baseline_warning)

/*
        val positiveButton = dialog.findViewById<Button>(R.id.btnPositiveAlert)
        positiveButton.setOnClickListener {
            Toast.makeText(context, "Thank You!", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
*/
        alert.setPositiveButton("Okey", DialogInterface.OnClickListener { _, _ ->
            Toast.makeText(context, "Thank You!", Toast.LENGTH_LONG).show()
        })
        alert.show()

        //dialog.show()
    }
}