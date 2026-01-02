package com.example.recipeapp

import android.content.Context
import  android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.recipeapp.databinding.ActivityMainBinding
import com.example.recipeapp.utils.MyApp
import dagger.hilt.android.AndroidEntryPoint
import io.github.inflationx.viewpump.ViewPumpContextWrapper

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    //Binding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    //Other
    private lateinit var navHost: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Setup nav host
        navHost = supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        binding.mainBottomNavigation.background = null
        binding.mainBottomNavigation.setupWithNavController(navHost.navController)
        //Gone bottom menu
        navHost.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment -> visibilityBottomMenu(false)
                R.id.registerFragment -> visibilityBottomMenu(false)
                else -> visibilityBottomMenu(true)
            }
        }
    }

    private fun visibilityBottomMenu(isMenuVisible: Boolean) {
        binding.apply {
            if (isMenuVisible) {
                mainBottomAppBar.isVisible = true
                mainFabMenu.isVisible = true
            } else {
                mainBottomAppBar.isVisible = false
                mainFabMenu.isVisible = false
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        val app = newBase?.applicationContext as MyApp
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase, app.viewPump))
    }

    override fun onNavigateUp(): Boolean {
        return navHost.navController.navigateUp() || super.onNavigateUp()
    }

    override fun onStop() {
        super.onStop()
        _binding = null
    }
}