package com.example.androidapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ContentInfoCompat.Flags
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.androidapp.models.UserInfo

class MainMenuActivity : AppCompatActivity() {

    @Deprecated("This is a temporary solution.")
    @Suppress("MissingSuperCall")
    override fun onBackPressed() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun getCurrentUserInfo(): UserInfo {
        val user = UserInfo()
        user.firstName = "Alexandru-Vasile"
        user.lastName = "Stelea"
        user.email = "alexregele@yahoo.com"
        user.gender = "Regele"
        user.weight = 70.0f
        user.height = 173
        return user
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main_menu_view)
        supportActionBar?.hide()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        WindowInsetsControllerCompat(window, findViewById(R.id.main)).isAppearanceLightStatusBars =
            true

        val currentUser = getCurrentUserInfo()
        val userNameTextView = findViewById<TextView>(R.id.userNameTextView)
        userNameTextView.text = currentUser.firstName + " " + currentUser.lastName
    }
}