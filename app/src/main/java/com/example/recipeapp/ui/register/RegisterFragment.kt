package com.example.recipeapp.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.example.recipeapp.R
import com.example.recipeapp.databinding.FragmentRegisterBinding
import com.example.recipeapp.models.register.BodyRegister
import com.example.recipeapp.utils.Constants.MY_API_KEY
import com.example.recipeapp.utils.NetworkChecker
import com.example.recipeapp.utils.NetworkRequest
import com.example.recipeapp.utils.showSnackBar
import com.example.recipeapp.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    //Binding
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var body: BodyRegister

    @Inject
    lateinit var networkChecker: NetworkChecker

    //Other
    private val viewModel: RegisterViewModel by viewModels()
    private var email = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            coverImg.load(R.drawable.register_logo)
            //Email
            emailEdt.addTextChangedListener {
                if (it.toString().contains("@")) {
                    email = it.toString()
                    emailTxtLay.error = ""
                } else {
                    emailTxtLay.error = getString(R.string.emailNotValid)
                }
            }
            //Click
            submitBtn.setOnClickListener {
                val firstName = nameEdt.text.toString()
                val lastName = lastNameEdt.text.toString()
                val userName = userNameEdt.text.toString()
                //Body
                body.email = email
                body.firstName = firstName
                body.lastName = lastName
                body.username = userName
                //Check network
                lifecycleScope.launch {
                    networkChecker.checkNetworkAvailability().collect { state ->
                        if (state) {
                            //Call api
                            viewModel.callRegisterApi(MY_API_KEY, body)
                        } else {
                            root.showSnackBar(getString(R.string.checkConnection))
                        }
                    }
                }
            }
            //Load data
            loadRegisterData()
        }
    }

    private fun loadRegisterData() {
        viewModel.registerData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkRequest.Loading -> {

                }

                is NetworkRequest.Success -> {
                    response.data?.let {
                        viewModel.saveRegisterData(it.username.toString(), it.hash.toString())
                    }
                }

                is NetworkRequest.Error -> {
                    binding.root.showSnackBar(response.message!!)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
