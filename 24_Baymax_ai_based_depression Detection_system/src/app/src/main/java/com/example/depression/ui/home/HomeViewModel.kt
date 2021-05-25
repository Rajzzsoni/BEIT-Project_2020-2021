package com.example.depression.ui.home

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.depression.model.Question
import com.example.depression.repository.HomeRepository
import com.example.depression.utils.JsonReadFromAsset.getJsonDataFromAsset
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class HomeViewModel(var homeRepository: HomeRepository) : ViewModel() {

    var userLiveData = MutableLiveData<ArrayList<Question>>()

    fun populateQuestionList(context: Context) {
       viewModelScope.launch {
           userLiveData.value = homeRepository.populateQuestionList(context)
       }
    }

}