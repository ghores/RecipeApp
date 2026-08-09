package com.example.recipeapp.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import com.example.recipeapp.R
import com.example.recipeapp.adapter.InstructionsAdapter
import com.example.recipeapp.adapter.StepsAdapter
import com.example.recipeapp.databinding.FragmentDetailBinding
import com.example.recipeapp.models.detail.ResponseDetail
import com.example.recipeapp.utils.Constants
import com.example.recipeapp.utils.NetworkRequest
import com.example.recipeapp.utils.isVisible
import com.example.recipeapp.utils.minToHour
import com.example.recipeapp.utils.setDynamicallyColor
import com.example.recipeapp.utils.setupRecyclerView
import com.example.recipeapp.utils.showSnackBar
import com.example.recipeapp.viewmodel.DetailViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {
    //Binding
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var instructionsAdapter: InstructionsAdapter

    @Inject
    lateinit var stepsAdapter: StepsAdapter

    private val detailViewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    private var recipeId = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Args
        args.let {
            recipeId = args.recipeID
            //Call Api
            if (recipeId > 0) {
                detailViewModel.callDetailApi(recipeId, Constants.MY_API_KEY)
            }
        }
        //InitView
        binding.apply {
            //Back
            backImg.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        //Load data
        loadDataFromApi()
    }

    private fun loadDataFromApi() {
        binding.apply {
            detailViewModel.detailData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> { loading.isVisible(true, contentLay) }
                    is NetworkRequest.Success -> {
                        loading.isVisible(false, contentLay)
                        response.data?.let { data ->
                            initViewsWithData(data)
                        }
                    }
                    is NetworkRequest.Error -> {
                        loading.isVisible(false, contentLay)
                        binding.root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViewsWithData(data: ResponseDetail) {
        binding.apply {
            //Image
            val imageSplit = data.image!!.split("-")
            val imageSize = imageSplit[1].replace(Constants.OLD_IMAGE_SIZE, Constants.NEW_IMAGE_SIZE)
            coverImg.load("${imageSplit[0]}-$imageSize") {
                crossfade(true)
                crossfade(800)
                memoryCachePolicy(CachePolicy.ENABLED)
                error(R.drawable.ic_placeholder)
            }
            //Source
            data.sourceUrl?.let { source ->
                sourceImg.isVisible = true
                sourceImg.setOnClickListener {

                }
            }
            //Text
            timeTxt.text = data.readyInMinutes!!.minToHour()
            nameTxt.text = data.title
            //Desc
            val summary = HtmlCompat.fromHtml(data.summary!!, HtmlCompat.FROM_HTML_MODE_COMPACT)
            descTxt.text = summary
            //Toggle
            if (data.cheap!!) cheapTxt.setDynamicallyColor(R.color.caribbean_green)
            if (data.veryPopular!!) popularTxt.setDynamicallyColor(R.color.tart_orange)
            if (data.vegan!!) veganTxt.setDynamicallyColor(R.color.caribbean_green)
            if (data.dairyFree!!) dairyTxt.setDynamicallyColor(R.color.caribbean_green)
            //Like
            likeTxt.text = data.aggregateLikes.toString()
            //price
            priceTxt.text = "${data.pricePerServing} $"
            //Healthy
            healthyTxt.text = data.healthScore.toString()
            when (data.healthScore) {
                in 90..100 -> healthyTxt.setDynamicallyColor(R.color.caribbean_green)
                in 60..89 -> healthyTxt.setDynamicallyColor(R.color.chineseYellow)
                in 0..59 -> healthyTxt.setDynamicallyColor(R.color.tart_orange)
            }
            //Instructions
            instructionsCount.text = "${data.extendedIngredients!!.size} ${getString(R.string.items)}"
            val instructions = HtmlCompat.fromHtml(data.instructions!!, HtmlCompat.FROM_HTML_MODE_COMPACT)
            instructionsDesc.text = instructions
            initInstructionsList(data.extendedIngredients.toMutableList())
            //Steps
            if (!data.analyzedInstructions.isNullOrEmpty()) {
                initStepsList(data.analyzedInstructions[0].steps?.toMutableList() ?: mutableListOf())
                stepsShowMore.setOnClickListener {
                    val direction = DetailFragmentDirections.actionDetailToStep(data.analyzedInstructions[0])
                    findNavController().navigate(direction)
                }
            } else {
                initStepsList(mutableListOf())
            }
            //Diets
            setupChip(data.diets!!.toMutableList(), dietsChipGroup)
        }
    }

    private fun initInstructionsList(list: MutableList<ResponseDetail.ExtendedIngredient>) {
        if (list.isNotEmpty()) {
            instructionsAdapter.setData(list)
            binding.instructionsList.setupRecyclerView(
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
                instructionsAdapter
            )
        }
    }

    private fun initStepsList(list: MutableList<ResponseDetail.AnalyzedInstruction.Step>) {
        if (list.isNotEmpty()) {
            Constants.STEPS_COUNT = if (list.size < 3) {
                list.size
            } else {
                3
            }
            stepsAdapter.setData(list)
            binding.apply {
                stepsList.setupRecyclerView(LinearLayoutManager(requireContext()), stepsAdapter)
                //Show more
                if (list.size > 3) {
                    stepsShowMore.isVisible = true
                }
            }
        }
    }

    private fun setupChip(list: MutableList<String>, chipGroup: ChipGroup) {
        list.forEach {
            val chip = Chip(requireContext())
            val drawable = ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.DietsChip)
            chip.setChipDrawable(drawable)
            chip.setTextColor(ContextCompat.getColor(requireContext(), R.color.darkGray))
            chip.text = it
            chipGroup.addView(chip)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}