package com.willy.metu.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.willy.metu.MainViewModel
import com.willy.metu.MeTuApplication
import com.willy.metu.R
import com.willy.metu.databinding.DialogPostArticleBinding
import com.willy.metu.ext.getVmFactory
import com.willy.metu.network.LoadApiStatus
import com.willy.metu.pair.QuestionSpinnerAdapter
import com.willy.metu.util.Logger

class ArticleDialogFragment : AppCompatDialogFragment() {

    private val viewModel by viewModels<ArticleDialogViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AddEventDialog)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val binding = DialogPostArticleBinding.inflate(inflater,container,false)

        binding.viewModel = viewModel

        binding.buttonPost.setOnClickListener {
            if(viewModel.checkIfComplete()) {
                viewModel.postArticle(viewModel.getArticle())
            } else {
                Toast.makeText(requireContext(),"Please finish the required information",Toast.LENGTH_SHORT).show()
            }
        }

       //Content and Indicator setup for spinner
        val majorIndicator = MeTuApplication.instance.resources.getString(R.string.spinner_select_category)
        val minorIndicator = MeTuApplication.instance.resources.getString(R.string.spinner_select_subject)
        val typeIndicator = MeTuApplication.instance.resources.getString(R.string.spinner_select_type)
        val locationIndicator = MeTuApplication.instance.resources.getString(R.string.spinner_select_city)
        val typeContent = MeTuApplication.instance.resources.getStringArray(R.array.article_array)
        val defaultContent = MeTuApplication.instance.resources.getStringArray(R.array.default_array)
        val majorContent = MeTuApplication.instance.resources.getStringArray(R.array.major_subject_array)
        val locationContent = MeTuApplication.instance.resources.getStringArray(R.array.city_array)

        binding.spinnerSubjectMajor.adapter = QuestionSpinnerAdapter(majorContent, majorIndicator)
        binding.spinnerSubjectMinor.adapter = QuestionSpinnerAdapter(defaultContent, minorIndicator)
        binding.spinnerType.adapter = QuestionSpinnerAdapter(typeContent,typeIndicator)
        binding.spinnerLocation.adapter = QuestionSpinnerAdapter(locationContent,locationIndicator)

        binding.spinnerType.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            pos: Int,
                            id: Long
                    ) {
                        if (parent != null && pos != 0) {
                            viewModel.articleType.value = parent.selectedItem.toString()
                        }
                    }
                }

        //When main category is selected, change the related content of subject
        binding.spinnerSubjectMajor.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            pos: Int,
                            id: Long
                    ) {
                        when (pos) {
                            1 -> binding.spinnerSubjectMinor.adapter = setSpinnerContent(R.array.language_array,minorIndicator)
                            2 -> binding.spinnerSubjectMinor.adapter = setSpinnerContent(R.array.curriculum_array,minorIndicator)
                            3 -> binding.spinnerSubjectMinor.adapter = setSpinnerContent(R.array.music_array,minorIndicator)
                            4 -> binding.spinnerSubjectMinor.adapter = setSpinnerContent(R.array.art_array,minorIndicator)
                            5 -> binding.spinnerSubjectMinor.adapter = setSpinnerContent(R.array.sport_array,minorIndicator)
                            6 -> binding.spinnerSubjectMinor.adapter = setSpinnerContent(R.array.exam_array,minorIndicator)
                        }
                        if (parent != null && pos != 0) {
                            viewModel.articleSubject.value = parent.selectedItem.toString()
                        }

                    }
                }

        //Passing Value to Subject livedata
        binding.spinnerSubjectMinor.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            pos: Int,
                            id: Long
                    ) {
                        if (parent != null && pos != 0) {
                            viewModel.articleSubject.value = parent.selectedItem.toString()
                        }
                    }
                }

        //Passing Value to City livedata
        binding.spinnerLocation.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                    override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            pos: Int,
                            id: Long
                    ) {
                        if (parent != null && pos != 0) {
                            viewModel.articleCity.value = parent.selectedItem.toString()
                        }
                    }
                }



        //Observers for entered values
        viewModel.articleType.observe(viewLifecycleOwner, Observer {
            Logger.d(it)
        })

        viewModel.articleTitle.observe(viewLifecycleOwner, Observer {
            Logger.d(it)
        })

        viewModel.articleCity.observe(viewLifecycleOwner, Observer {
            Logger.d(it)
        })

        viewModel.articleLocation.observe(viewLifecycleOwner, Observer {
            Logger.d(it)
        })

        viewModel.articleSubject.observe(viewLifecycleOwner, Observer {
            Logger.d(it)
        })

        viewModel.articleDetail.observe(viewLifecycleOwner, Observer {
            Logger.d(it)
        })


        // Boolean for cancel button
        viewModel.leave.observe(viewLifecycleOwner, Observer {
            it?.let { needRefresh ->
                if (needRefresh) {
                    ViewModelProvider(requireActivity()).get(MainViewModel::class.java).apply {
                        refresh()
                    }
                }
                findNavController().navigateUp()
                viewModel.onLeft()
            }
        })

        // Progress Bar
        viewModel.status.observe(viewLifecycleOwner, Observer {
            Logger.d("viewModel.test.observe=LoadApiStatus.LOADING")
            when (it) {
                LoadApiStatus.LOADING -> {
                    Logger.d("viewModel.test.observe=LoadApiStatus.LOADING")
                    binding.progress.visibility = View.VISIBLE

                }
                LoadApiStatus.DONE, LoadApiStatus.ERROR -> {
                    Logger.d("viewModel.test.observe=LoadApiStatus.DONE")
                    binding.progress.visibility = View.GONE
                    viewModel.leave()
                }}
        })



        return binding.root
    }

    fun setSpinnerContent(array: Int, indicator: String): SpinnerAdapter {
        val spinner = QuestionSpinnerAdapter(MeTuApplication.instance.resources.getStringArray(array),indicator)
        return spinner
    }

}