package com.example.depression.ui.geeta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.depression.R
import kotlinx.android.synthetic.main.fragment_bhagvad_geeta.*
import kotlinx.android.synthetic.main.fragment_yoga.*
import kotlinx.android.synthetic.main.fragment_yoga.webViewYoga

class BhagvadGeetaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_bhagvad_geeta, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val url =
            "https://iskcondwarka.org/blogs/the-eight-steps-of-hatha-yoga-and-bhagavad-gita/"
        val webSettings = webViewBhagvadGeeta.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webViewBhagvadGeeta.webViewClient = WebViewClient()
        webViewBhagvadGeeta.loadUrl(url)
    }
}