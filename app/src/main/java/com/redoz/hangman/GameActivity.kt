package com.redoz.hangman

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.redoz.hangman.databinding.ActivityGameBinding
import kotlin.random.Random

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private lateinit var lettersMap: MutableMap<Int, TextView>
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
    private var changeImageCounter = 0
    private val sports = listOf(
        "swimming", "cycling", "tennis", "boxing", "shooting", "judo", "golf",
        "basketball", "football", "volleyball", "baseball", "triathalon",
        "snowboarding", "hockey", "gymnastics", "bowling", "athletics",
        "weightlifting", "archery", "badminton", "diving", "cricket"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        changeImageCounter = 0
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)

        setContentView(binding.root)
        lettersMap = mutableMapOf()

        val word = sports[Random.nextInt(0,sports.size)]

        loadLettersAndSpaces(word)

        binding.sendBtn.setOnClickListener { game(word) }
    }

    private fun game(word: String) {
        val letter = binding.letterEditTxt.text.toString().trim()
        if (letter.isNotEmpty()) {
            findLetterOnWord(letter[0], word)
            binding.letterEditTxt.text.clear()
        }

        if (changeImageCounter >= 10) {
            showGameResultDialog("You Lost, the word was $word")
        }

        if (allLettersFound(word)) {
            showGameResultDialog("You Won")
        }

    }


    private fun findLetterOnWord(letter: Char, word: String) {
        var found = false
        for (i in word.indices) {
            if (word[i] == letter) {
                loadFoundLetter(letter, i)
                found = true
            }
        }

        if (!found) {
            changeHandmanImage()
            changeImageCounter++
        }

    }

    private fun allLettersFound(word: String): Boolean{
        var allLettersFound = true
        for (i in word.indices) {
            val textView = lettersMap[i]
            if (textView?.text.toString() != word[i].toString()) {
                allLettersFound = false
                break
            }
        }

        return allLettersFound
    }

    private fun showGameResultDialog(message: String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_game_result)

        val tvResultMessage = dialog.findViewById<TextView>(R.id.tv_result_message)
        val btnCloseDialog = dialog.findViewById<Button>(R.id.btn_close_dialog)

        tvResultMessage.text = message

        btnCloseDialog.setOnClickListener {
            dialog.dismiss()
            finish() // Close the activity after the dialog is dismissed
        }

        dialog.show()
    }

    private fun loadFoundLetter(letter: Char, index: Int) {
        val textView = lettersMap[index]
        textView?.text = letter.toString()
    }

    private fun loadLettersAndSpaces(word: String) {
        var counter = 0
        var maxHelpers = word.length/3
        var character = "_"

        for (i in word.indices) {
            val marginEnd = dpToPx(3.toFloat(), this)
            val willHelp = Random.nextBoolean()

            if (willHelp && counter < maxHelpers) {
                character = word[i].toString()
                counter++
            }

            val textView = TextView(this).apply {
                id = i
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
            lettersMap[textView.id] = textView
            character = "_"
        }
    }

    private fun changeHandmanImage() {
        if (changeImageCounter >= handmanImages.size) {
            changeImageCounter = 0
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