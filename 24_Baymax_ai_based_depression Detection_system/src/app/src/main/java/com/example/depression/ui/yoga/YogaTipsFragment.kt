package com.example.depression.ui.yoga

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.depression.R
import kotlinx.android.synthetic.main.fragment_yoga.*

class YogaTipsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_yoga, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url =
            "https://www.realbuzz.com/articles-interests/fitness/article/top-tips-for-yoga-beginners/"
        val webSettings = webViewYoga.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webViewYoga.webViewClient = WebViewClient()
        webViewYoga.loadUrl(url)
    }
}