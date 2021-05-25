package com.example.depression.utils

object FuzzyLogic {

    fun getOutPutUsingFuzzyLogic(answersHashMap: HashMap<String, Int>): String {
        var tempResultPoints = 0

        answersHashMap.forEach { t, u ->
            tempResultPoints += u
        }

        return when {
            tempResultPoints  >= 30 -> {
                "30 Extreme Depression"
            }
            tempResultPoints in 25..29 -> {
                "25-29 Severe Depression"
            }
            tempResultPoints in 20..24 -> {
                "20-24 Moderate Depression"
            }
            tempResultPoints in 15..19 -> {
                "15-19 Borderline clinical  Depression"
            }
            tempResultPoints in 10..14 -> {
                "10-14 Mild mood disturbance"
            }
            else -> {
                "0-9 These ups and downs are considered normal"
            }
        }
    }

    fun getTotalScoreResult(answersHashMap: HashMap<String, Int>): String {
        var tempTotalPoints = 0
        answersHashMap.forEach { t, u ->
            tempTotalPoints += u
        }
        return "Total Score $tempTotalPoints"
    }

    fun isAllQuestionAnswered(answersHashMap: HashMap<String, Int>): Boolean {
        return answersHashMap.size == 10
    }
}