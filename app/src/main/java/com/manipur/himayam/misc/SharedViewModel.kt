package com.manipur.himayam.misc

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manipur.himayam.dto.Item

class SharedViewModel : ViewModel() {

    val htmlTextForPost = MutableLiveData<String>()
    val itemSaved = MutableLiveData<Item>()
}