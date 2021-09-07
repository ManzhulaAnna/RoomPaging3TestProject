package com.test.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.test.R
import com.test.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        initItemListener(navController)
    }

    private fun initItemListener(navController: NavController) {
        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_events -> {
                    navController.navigateIfOther(R.id.navigation_events)
                }
                R.id.navigation_favorites -> {
                    navController.navigateIfOther(R.id.navigation_favorites)
                }
                else -> false
            }
        }
    }

    private fun NavController.navigateIfOther(itemId: Int): Boolean {
        return if (itemId != binding.navView.selectedItemId) {
            navigate(itemId)
            true
        } else {
            false
        }
    }
}
