package com.redoz.hangman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
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
        val categoryRadioGroup = binding.categoryRadioGroup
        val selectedCategory = findViewById<RadioButton>(categoryRadioGroup.checkedRadioButtonId)

        val difficultiesRadioGroup = binding.difficultiesRadioGroup
        val selectedDifficulty = findViewById<RadioButton>(difficultiesRadioGroup.checkedRadioButtonId)

        val intent = Intent(this, GameActivity::class.java).apply {
            putExtra("selectedCategory", selectedCategory.text.toString())
            putExtra("selectedDifficulty", selectedDifficulty.text.toString())
        }

        startActivity(intent)
    }
}
