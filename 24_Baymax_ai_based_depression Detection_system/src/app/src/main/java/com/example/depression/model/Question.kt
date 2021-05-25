package com.example.depression.model

class Question{
    var questionNo: String = ""
    var optionA: String = ""
    var optionB: String = ""
    var optionC: String = ""
    var optionD: String = ""

    var optionPointA: String = ""
    var optionPointB: String = ""
    var optionPointC: String = ""
    var optionPointD: String = ""

    var selectedAnswerPosition: Int = -1

    var isOp1Sel = false
        set(op1Sel) {
            field = op1Sel
            if (op1Sel) { // To make sure only one option is selected at a time
                isOp2Sel = false
                isOp3Sel = false
                isOp4Sel = false
            }
        }
    var isOp2Sel = false
        set(op2Sel) {
            field = op2Sel
            if (op2Sel) {
                isOp1Sel = false
                isOp3Sel = false
                isOp4Sel = false
            }
        }
    var isOp3Sel // options
            = false
        set(op3Sel) {
            field = op3Sel
            if (op3Sel) {
                isOp2Sel = false
                isOp1Sel = false
                isOp4Sel = false
            }
        }

    var isOp4Sel // options
            = false
        set(op4Sel) {
            field = op4Sel
            if (op4Sel) {
                isOp2Sel = false
                isOp1Sel = false
                isOp3Sel = false
            }
        }

}



