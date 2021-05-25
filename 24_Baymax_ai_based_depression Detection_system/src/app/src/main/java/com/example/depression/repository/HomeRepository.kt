package com.example.depression.repository

import android.content.Context
import com.example.depression.model.Question


interface HomeRepository {

    suspend fun populateQuestionList(context: Context): ArrayList<Question>
}