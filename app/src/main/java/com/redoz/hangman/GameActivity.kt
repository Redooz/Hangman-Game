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

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    private fun loadLettersSpace(word: String) {
        for (i in word.indices) {
            val marginEnd = dpToPx(3.toFloat(), this)
            val textView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, marginEnd, 0)
                }
                layoutParams = this.layoutParams
                typeface = Typeface.MONOSPACE
                text = "_"
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 34f)
                setTypeface(null, Typeface.BOLD)
            }

            binding.wordContainer.addView(textView)
        }
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
            .toInt()
    }

}