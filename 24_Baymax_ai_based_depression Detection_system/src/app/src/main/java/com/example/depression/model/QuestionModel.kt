package com.example.depression.model

class QuestionModel {
    var question: String? = null
    var seleectedAnswerPosition = 0

    // To make sure only one option is selected at a time
    var isOp1Sel = false
        set(op1Sel) {
            field = op1Sel
            if (op1Sel) { // To make sure only one option is selected at a time
                isOp2Sel = false
                isOp3Sel = false
            }
        }
    var isOp2Sel = false
        set(op2Sel) {
            field = op2Sel
            if (op2Sel) {
                isOp1Sel = false
                isOp3Sel = false
            }
        }
    var isOp3Sel // options 
            = false
        set(op3Sel) {
            field = op3Sel
            if (op3Sel) {
                isOp2Sel = false
                isOp1Sel = false
            }
        }
}