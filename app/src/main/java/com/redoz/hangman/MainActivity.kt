package com.redoz.hangman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.redoz.hangman.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.startBtn.setOnClickListener { startGame() }

    }

    private fun startGame() {
        var intent = Intent(this,GameActivity::class.java).apply {  }
        startActivity(intent)
    }
}