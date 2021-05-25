package com.example.depression.repository.implementation

import android.content.Context
import com.example.depression.model.Question
import com.example.depression.repository.HomeRepository
import com.example.depression.utils.JsonReadFromAsset
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


private val TAG = "HomeRepositoryImpl"

class HomeRepositoryImpl : HomeRepository {

    val random = Random()
    var randomInt: Int = 0
    var pickedInt: MutableSet<Int> = mutableSetOf()

    override suspend fun populateQuestionList(context: Context): ArrayList<Question> {
        val jsonFileString =
            JsonReadFromAsset.getJsonDataFromAsset(context, "DepressionDataSet.json")
        val gson = Gson()
        val json = JSONObject(jsonFileString)
        val jsonArray = json.getJSONArray("Questions")
        val listPersonType = object : TypeToken<List<Question>>() {}.type
        return getRandomQuestionSets(gson.fromJson(jsonArray.toString(), listPersonType))
    }

    private fun getRandomQuestionSets(questionList: ArrayList<Question>): ArrayList<Question> {
        val tempList = ArrayList<Question>()
        var tempObject: Question

        for (i in 1..10) {
            val randomNumber = rand(0, 20)
            tempObject = questionList[randomNumber]
            tempList.add(tempObject)
        }
        pickedInt.clear()
        return tempList
    }

    private fun rand(from: Int, to: Int): Int{
        do{
            randomInt = random.nextInt(to - from) + from
        }while(pickedInt.contains(randomInt))

        pickedInt.add(randomInt)
        return randomInt
    }

}