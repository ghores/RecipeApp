package com.example.recipeapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.adapter.FavoriteAdapter
import com.example.recipeapp.databinding.FragmentFavoriteBinding
import com.example.recipeapp.ui.recipe.RecipeFragmentDirections
import com.example.recipeapp.utils.isVisible
import com.example.recipeapp.utils.setupRecyclerView
import com.example.recipeapp.viewmodel.FavoriteViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    //Binding
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

    //Other
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //InitViews
        binding.apply {
            //Load favorite
            favoriteViewModel.readFavoriteData.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    emptyTxt.isVisible(false, favoriteList)
                    //Data
                    favoriteAdapter.setData(it)
                    favoriteList.setupRecyclerView(LinearLayoutManager(requireContext()), favoriteAdapter)
                    //Click
                    favoriteAdapter.setOnItemClickListener { id ->
                        val action = RecipeFragmentDirections.actionToDetail(id)
                        findNavController().navigate(action)
                    }
                } else {
                    emptyTxt.isVisible(true, favoriteList)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}