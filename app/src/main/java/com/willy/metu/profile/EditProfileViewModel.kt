package com.willy.metu.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.willy.metu.data.source.MeTuRepository

class EditProfileViewModel (private val repository: MeTuRepository) : ViewModel() {

    var selectedGender = MutableLiveData<String>()
    var selectedIdentity = MutableLiveData<String>()
    var selectedCity = MutableLiveData<String>()
    var selectedDistrict = MutableLiveData<String>()
    var selectedTags = MutableLiveData<List<String>>()
    val itemList:MutableList<String> = ArrayList()
    var introduction = MutableLiveData<String>()
}