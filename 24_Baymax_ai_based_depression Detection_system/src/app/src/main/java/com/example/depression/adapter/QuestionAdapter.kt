package com.example.depression.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.depression.R
import com.example.depression.model.Question


class QuestionAdapter(
    private val questionList: ArrayList<Question>,
    private val context: Context?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var clickListener: OnOptionSelectionListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rootView: View =
            LayoutInflater.from(context).inflate(R.layout.depressionitem, parent, false)
        return RecyclerViewViewHolder(rootView, clickListener, questionList)
    }

    fun updateList(questions: ArrayList<Question>) {
        questionList.clear()
        questionList.addAll(questions)
    }

    fun getList(): ArrayList<Question> {
        return questionList
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val questionSelected = questionList[position]
        val viewHolder = holder as RecyclerViewViewHolder

        viewHolder.titleNumber.text = (position + 1).toString()
        viewHolder.radioButtonA.text = questionSelected.optionA
        viewHolder.radioButtonB.text = questionSelected.optionB
        viewHolder.radioButtonC.text = questionSelected.optionC
        viewHolder.radioButtonD.text = questionSelected.optionD

        viewHolder.radioGroup.clearCheck()
        viewHolder.radioButtonA.isChecked = questionList[position].isOp1Sel
        viewHolder.radioButtonB.isChecked = questionList[position].isOp2Sel
        viewHolder.radioButtonC.isChecked = questionList[position].isOp3Sel
        viewHolder.radioButtonD.isChecked = questionList[position].isOp4Sel


    }

    /**
     * To set OnClaimItemClickListener in adapter
     * @param clickListener of type OnClaimItemClickListener
     */
    fun setItemClickListener(clickListener: OnOptionSelectionListener) {
        this.clickListener = clickListener
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    class RecyclerViewViewHolder(itemView: View, clickListener: OnOptionSelectionListener, questionList: ArrayList<Question>) :
        RecyclerView.ViewHolder(itemView) {
        var titleNumber: TextView = itemView.findViewById(R.id.titleNo)
        var radioButtonA: RadioButton = itemView.findViewById(R.id.radioOption1)
        var radioButtonB: RadioButton = itemView.findViewById(R.id.radioOption2)
        var radioButtonC: RadioButton = itemView.findViewById(R.id.radioOption3)
        var radioButtonD: RadioButton = itemView.findViewById(R.id.radioOption4)
        var radioGroup: RadioGroup = itemView.findViewById(R.id.optionGroup)


        init {
            radioButtonA.setOnClickListener {
                clickListener.onOptionItemChangeListener(adapterPosition, 0, questionList[adapterPosition])
            }
            radioButtonB.setOnClickListener {
                clickListener.onOptionItemChangeListener(adapterPosition, 1, questionList[adapterPosition])
            }
            radioButtonC.setOnClickListener {
                clickListener.onOptionItemChangeListener(adapterPosition, 2, questionList[adapterPosition])
            }
            radioButtonD.setOnClickListener {
                clickListener.onOptionItemChangeListener(adapterPosition, 3, questionList[adapterPosition])
            }

//            radioGroup.setOnCheckedChangeListener { group, checkedId ->
//                if (checkedId == R.id.radioOption1) {
//                    clickListener.onOptionItemChangeListener(adapterPosition, 0)
////                clickListener.onOptionItemChangeListener(
////                    questionSelected,
////                    questionSelected.optionPointA,
////                    position,
////                    checkedId
////                )
//                } else if (checkedId == R.id.radioOption2) {
//                    clickListener.onOptionItemChangeListener(adapterPosition, 1)
////                clickListener.onOptionItemChangeListener(
////                    questionSelected,
////                    questionSelected.optionPointB,
////                    position,
////                    checkedId
////                )
//                } else if (checkedId == R.id.radioOption3) {
//                    clickListener.onOptionItemChangeListener(adapterPosition, 2)
////                clickListener.onOptionItemChangeListener(
////                    questionSelected,
////                    questionSelected.optionPointC,
////                    position,
////                    checkedId
////                )
//                } else {
//                    clickListener.onOptionItemChangeListener(adapterPosition, 3)
////                clickListener.onOptionItemChangeListener(
////                    questionSelected,
////                    questionSelected.optionPointA,
////                    position,
////                    checkedId
////                )
//                }
//            }
        }
    }
}