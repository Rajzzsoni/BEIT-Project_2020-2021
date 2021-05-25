package com.example.depression.ui.chants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.depression.R
import kotlinx.android.synthetic.main.fragment_chants.*
import kotlinx.android.synthetic.main.fragment_yoga.*
import kotlinx.android.synthetic.main.fragment_yoga.webViewYoga

class ChantsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_chants, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url =
            "https://wanderlust.com/journal/6-step-guide-start-chanting-practice/"
        val webSettings = webViewChants.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webViewChants.webViewClient = WebViewClient()
        webViewChants.loadUrl(url)
    }
}