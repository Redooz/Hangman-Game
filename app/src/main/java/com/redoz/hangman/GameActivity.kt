package com.redoz.hangman

import android.content.Context
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.redoz.hangman.databinding.ActivityGameBinding
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val handmanImages = listOf(
        R.drawable.hangman1,
        R.drawable.hangman2,
        R.drawable.hangman3,
        R.drawable.hangman4,
        R.drawable.hangman5,
        R.drawable.hangman6,
        R.drawable.hangman7,
        R.drawable.hangman8,
        R.drawable.hangman9,
        R.drawable.hangman10
    )
    private var changeImageCounter = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)

        setContentView(binding.root)

    }

    private fun loadLettersAndSpaces(word: String) {
        var counter = 0
        var maxHelpers = word.length/3
        var character = "_"

        for (char in word) {
            val marginEnd = dpToPx(3.toFloat(), this)
            var willHelp = Random.nextBoolean()

            if (willHelp && counter < maxHelpers) {
                character = char.toString()
                counter++
            }

            val textView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, marginEnd, 0)
                }
                layoutParams = this.layoutParams
                typeface = Typeface.MONOSPACE
                text = character
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 34f)
                setTypeface(null, Typeface.BOLD)
            }

            binding.wordContainer.addView(textView)
            character = "_"
        }
    }

    private fun changeHandmanImage() {
        if (changeImageCounter >= handmanImages.size) {
            changeImageCounter = -1
        }

        binding.imgHandman.setImageResource(handmanImages[changeImageCounter])
    }


    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        )
            .toInt()
    }

}