package com.example.depression.adapter

import com.example.depression.model.Question


/**
 * To get Account Item click
 */
interface OnOptionSelectionListener {
    fun onOptionItemChangeListener(position: Int, itemSelected: Int, question: Question)
}
