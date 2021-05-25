package com.example.depression.ui.selection_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.depression.R
import com.example.depression.repository.MainViewModel
import com.example.depression.utils.FuzzyLogic
import kotlinx.android.synthetic.main.default_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.ext.android.inject

class SelectionFragment : Fragment()
{
    private val mainViewModel: MainViewModel by inject()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.default_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonClick();
    }



    override fun onStart() {
        super.onStart()

    }

    private fun buttonClick() {
        btnDepTest.setOnClickListener {
            mainViewModel.currentSelection(1);
        }
        btnChatBot.setOnClickListener {
            mainViewModel.currentSelection(2);
        }
    }
}