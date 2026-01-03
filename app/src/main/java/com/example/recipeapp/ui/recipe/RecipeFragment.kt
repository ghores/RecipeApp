package com.example.recipeapp.ui.recipe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.adapter.PopularAdapter
import com.example.recipeapp.databinding.FragmentRecipeBinding
import com.example.recipeapp.utils.NetworkRequest
import com.example.recipeapp.utils.setupRecyclerView
import com.example.recipeapp.utils.showSnackBar
import com.example.recipeapp.viewmodel.RecipeViewModel
import com.example.recipeapp.viewmodel.RegisterViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFragment : Fragment() {
    //Binding
    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var popularAdapter: PopularAdapter

    //Other
    private val recipeViewModel: RecipeViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRecipeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Show username
        lifecycleScope.launch { showUserName() }
        //Call api
        recipeViewModel.callPopularApi(recipeViewModel.popularQueries())
        //Load data
        loadPopularData()
    }

    private fun loadPopularData() {
        binding.apply {
            recipeViewModel.popularData.observe(viewLifecycleOwner) {response->
                when(response) {
                    is NetworkRequest.Loading ->{
                        setupLoading(true, popularList)
                    }
                    is NetworkRequest.Success ->{
                        setupLoading(false, popularList)
                        response.data?.let {data ->
                            if (data.results!!.isNotEmpty()) {
                                popularAdapter.setData(data.results)
                                initPopularRecycler()
                            }
                        }
                    }
                    is NetworkRequest.Error ->{
                        setupLoading(false, popularList)
                        root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }

    private fun initPopularRecycler() {
        binding.popularList.setupRecyclerView(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            popularAdapter
        )
        //Click
        popularAdapter.setOnItemClickListener {
            //Go to detail page
        }
    }

    private fun setupLoading(isShownLoading: Boolean, shimmer: ShimmerRecyclerView) {
        shimmer.apply {
            if (isShownLoading) showShimmer() else hideShimmer()
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun showUserName() {
        registerViewModel.readRegisterData.collect {
            binding.usernameTxt.text = "${getString(R.string.hello)}, ${it.username} ${getEmojiByUnicode()}"
        }
    }

    private fun getEmojiByUnicode(): String {
        return String(Character.toChars(0x1f44b))
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}