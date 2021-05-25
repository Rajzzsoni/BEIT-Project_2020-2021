package com.example.depression

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.depression.repository.MainViewModel
import com.example.depression.ui.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.tyagiabhinav.dialogflowchatlibrary.Chatbot.ChatbotBuilder
import com.tyagiabhinav.dialogflowchatlibrary.ChatbotActivity
import com.tyagiabhinav.dialogflowchatlibrary.ChatbotSettings
import com.tyagiabhinav.dialogflowchatlibrary.DialogflowCredentials
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.koin.android.ext.android.inject
import java.util.*


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by inject()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//            openChatBot()
//        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_default_home,
                R.id.nav_home,
                R.id.nav_gallery,
                R.id.nav_slideshow,
                R.id.nav_yoga,
                R.id.nav_chants,
                R.id.nav_bhagvad_gita
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        observeCommands(navView)

        mainViewModel.moveTo.observe(this, Observer { value -> when (value)
        {
            0 -> navController.navigate(R.id.nav_default_home);
            1 -> navController.navigate(R.id.nav_home);
            2 -> navController.navigate(R.id.nav_gallery)
        }})
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity2, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_settings -> {
                firebaseAuth.signOut()
                startLoginActivity()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.getUserFromFirestore(this)
    }

    private fun observeCommands(navView: NavigationView) {
        mainViewModel.currentUserLD.observe(this, Observer { user ->
            user?.let {
                navView.tvName?.text = user.name
            }
        })
    }

    private fun openChatBot() {
        DialogflowCredentials.getInstance()
            .setInputStream(resources.openRawResource(R.raw.credential_file))
        ChatbotSettings.getInstance().chatbot = ChatbotBuilder().build()
        val intent = Intent(this, ChatbotActivity::class.java)
        val bundle = Bundle()
        bundle.putString(ChatbotActivity.SESSION_ID, UUID.randomUUID().toString())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.putExtras(bundle)
        startActivity(intent)
    }


    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}