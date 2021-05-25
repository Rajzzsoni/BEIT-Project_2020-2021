package com.example.depression.ui.home

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.depression.R
import com.example.depression.adapter.OnOptionSelectionListener
import com.example.depression.adapter.QuestionAdapter
import com.example.depression.model.Question
import com.example.depression.repository.FirebaseViewModel
import com.example.depression.repository.MainViewModel
import com.example.depression.utils.FuzzyLogic
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class HomeFragment : Fragment(), OnOptionSelectionListener {

    val homeViewModel: HomeViewModel by inject()
    private val mainViewModel: MainViewModel by inject()
    private lateinit var questionAdapter: QuestionAdapter
    private var answersHashMap = HashMap<String, Int>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        observeCommands()
        onSubmitButtonClick()
    }

    private fun observeCommands() {
        homeViewModel.userLiveData.observe(viewLifecycleOwner, Observer { questions ->
            questions?.let {
                Log.d("Test", "OnResponse")
                progressBar.visibility = View.GONE
                questionAdapter.updateList(questions)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        context?.let { homeViewModel.populateQuestionList(it) }
    }

    /**
     * Set all UI component of Account Fragment screen
     */
    private fun setUpUI() {
        questionAdapter = QuestionAdapter(ArrayList(), context)
        rv_main.adapter = questionAdapter
        questionAdapter.setItemClickListener(this)
    }

    override fun onOptionItemChangeListener(position: Int, itemSelected: Int, question: Question) {
        val tempList = ArrayList<Question>()
        val copyList = questionAdapter.getList()
        copyList[position].selectedAnswerPosition = itemSelected
        when (itemSelected) {
            0 -> copyList[position].isOp1Sel = true
            1 -> copyList[position].isOp2Sel = true
            2 -> copyList[position].isOp3Sel = true
            3 -> copyList[position].isOp4Sel = true
        }
        tempList.addAll(copyList)
        questionAdapter.updateList(tempList)
        questionAdapter.notifyDataSetChanged()
        answersHashMap[question.questionNo] = itemSelected
    }

    private fun onSubmitButtonClick() {
        btnSubmit.setOnClickListener {
            if (FuzzyLogic.isAllQuestionAnswered(answersHashMap)) {
                val totalScore = FuzzyLogic.getTotalScoreResult(answersHashMap)
                val result = FuzzyLogic.getOutPutUsingFuzzyLogic(answersHashMap)
                OpenCustomPopupDialog(totalScore, result)
            } else {
                Toast.makeText(context, "Answer All Questions please!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun OpenCustomPopupDialog(totalScore: String, result: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Back To Questions")
        val customLayout: View = layoutInflater
            .inflate(
                R.layout.custom_layout,
                null
            )
        val textScore: TextView = customLayout.findViewById(R.id.textScore)
        val textResult: TextView = customLayout.findViewById(R.id.textResult)
        val textDate: TextView = customLayout.findViewById(R.id.textDate)
        val textWebUrl: TextView = customLayout.findViewById(R.id.textWebUrl)
        val btnOk: Button = customLayout.findViewById(R.id.btnOk)


        textScore.text = totalScore
        textResult.text = result
        textDate.text = "Exam Date: " + getTodayDate()

        builder.setView(customLayout)
        val dialog = builder.create()
        dialog.show()

        btnOk.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            answersHashMap.clear()
            this.context?.let { it1 -> homeViewModel.populateQuestionList(it1) }
            questionAdapter.notifyDataSetChanged()
            dialog.dismiss()
            mainViewModel?.currentSelection(0);
        }

        textWebUrl.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(textWebUrl.text.toString()))
            startActivity(browserIntent)
        }
    }

    private fun getTodayDate(): String {
        val c: Date = Calendar.getInstance().time

        val df = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault())
        return df.format(c)
    }
}