package com.example.recipeapp.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.recipeapp.BuildConfig
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentSplashBinding
import com.example.recipeapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class SplashFragment : Fragment() {
    //Binding
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    //Other
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Dynamically background
            bgImg.load(R.drawable.bg_splash)
            //Application version
            versionTxt.text = "${getString(R.string.version)} : ${BuildConfig.VERSION_NAME}"
            //Auto navigate
            lifecycleScope.launch {
                delay(2500.milliseconds)
                //Check user info
                registerViewModel.readRegisterData.first().let { user ->
                    findNavController().popBackStack(R.id.splashFragment, true)
                    if (user.username.isNotEmpty()) {
                        //Navigate to main page
                        findNavController().navigate(R.id.actionToRecipe)
                    } else {
                        //Navigate to register page
                        findNavController().navigate(R.id.actionToRegister)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}