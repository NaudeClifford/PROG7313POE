package com.example.prog7313poe.ui

import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.Toast
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.prog7313poe.R

class NoSpendChallengeActivity : AppCompatActivity() {

    private var currentDays = 7
    private var currentDayProgress = 3
    private var moneySaved = 489

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_spend_challenge)


        val btn3Days = findViewById<Button>(R.id.btn3Days)
        val btn7Days = findViewById<Button>(R.id.btn7Days)
        val btn30Days = findViewById<Button>(R.id.btn30Days)
        val btnStartNew = findViewById<Button>(R.id.btnStartNew)
        val tvCurrentDay = findViewById<TextView>(R.id.tvCurrentDay)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvMoneySaved = findViewById<TextView>(R.id.tvMoneySaved)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Navigate to Home (Dashboard)
                    Toast.makeText(this, "Home clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_insights -> {
                    Toast.makeText(this, "Insights clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_awards -> {
                    Toast.makeText(this, "Awards clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, "Profile clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        btn3Days.setOnClickListener {
            currentDays = 3
            currentDayProgress = 1
            moneySaved = 150
            updateDisplay(tvCurrentDay, progressBar, tvMoneySaved)
        }

        btn7Days.setOnClickListener {
            currentDays = 7
            currentDayProgress = 3
            moneySaved = 489
            updateDisplay(tvCurrentDay, progressBar, tvMoneySaved)
        }

        btn30Days.setOnClickListener {
            currentDays = 30
            currentDayProgress = 5
            moneySaved = 1200
            updateDisplay(tvCurrentDay, progressBar, tvMoneySaved)
        }

        btnStartNew.setOnClickListener {
            currentDayProgress = 1
            moneySaved = 0
            updateDisplay(tvCurrentDay, progressBar, tvMoneySaved)
        }

        updateDisplay(tvCurrentDay, progressBar, tvMoneySaved)
    }

    private fun updateDisplay(dayText: TextView, progressBar: ProgressBar, savedText: TextView) {
        dayText.text = "🔥 Day $currentDayProgress of $currentDays"
        val progress = (currentDayProgress * 100) / currentDays
        progressBar.progress = progress
        savedText.text = "R$moneySaved saved by not spending"
    }
}