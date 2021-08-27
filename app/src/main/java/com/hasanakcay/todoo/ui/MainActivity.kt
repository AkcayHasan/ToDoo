package com.hasanakcay.todoo.ui

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import com.hasanakcay.todoo.R
import com.hasanakcay.todoo.ui.base.BaseActivity
import com.hasanakcay.todoo.databinding.ActivityMainBinding
import com.hasanakcay.todoo.util.ActivityButtonListener
import com.hasanakcay.todoo.util.FragmentListener

class MainActivity : BaseActivity<ActivityMainBinding>(), FragmentListener {

    lateinit var toolbarIconsListener: ActivityButtonListener

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        actions()

    }

    override fun whichFragment(fragment : Int) {
        when(fragment){
            R.id.notesFragment -> {
                binding.backIcon.visibility = View.GONE
                binding.editIcon.visibility = View.GONE
                binding.tvFragmentName.text = resources.getString(R.string.app_name)
            }
            R.id.noteDetailFragment -> {
                binding.backIcon.visibility = View.VISIBLE
                binding.editIcon.visibility = View.VISIBLE
                binding.tvFragmentName.text = resources.getString(R.string.title_add_note)
            }
        }
    }

    fun setOnActivityButtonListener(listener: ActivityButtonListener) {
        this.toolbarIconsListener = listener
    }

    private fun actions() {
        binding.editIcon.setOnClickListener {
            toolbarIconsListener.clickEditIcon(true)
        }

        binding.backIcon.setOnClickListener {
            toolbarIconsListener.clickBackIcon(true)
        }
    }

}