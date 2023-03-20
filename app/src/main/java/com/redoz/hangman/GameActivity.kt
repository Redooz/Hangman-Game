package com.redoz.hangman

import android.app.AlertDialog
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
    private var remainingTimeInMillis: Long = 90000 // default 90 secs
    private lateinit var countDownTimer: CountDownTimer
    private val sports = listOf(
        "swimming", "cycling", "tennis", "boxing", "shooting", "judo", "golf",
        "basketball", "football", "volleyball", "baseball", "triathalon",
        "snowboarding", "hockey", "gymnastics", "bowling", "athletics",
        "weightlifting", "archery", "badminton", "diving", "cricket"
    )
    private val carBrands = listOf(
        "tesla",
        "bmw",
        "volvo",
        "audi",
        "porsche",
        "lexus",
        "lamborghini",
        "ferrari",
        "cadillac",
        "jaguar",
        "bugatti"
    )

    override fun onCreate(savedInstanceState: Bundle?) = try {
        changeImageCounter = 0
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)

        setContentView(binding.root)
        lettersMap = mutableMapOf()

        val selectedDifficulty = intent.getStringExtra("selectedDifficulty")

        remainingTimeInMillis = when(selectedDifficulty?.lowercase()?.trim()) {
            "easy" -> 90000
            "medium" -> 60000
            "hard" -> 40000
            else -> {
                90000
            }
        }

        var word:String
        val selectedCategory = intent.getStringExtra("selectedCategory")
        word = if (selectedCategory?.lowercase()?.trim() == "sports") {
            sports[Random.nextInt(0, sports.size)]
        } else {
            carBrands[Random.nextInt(0, carBrands.size)]
        }

        loadLettersAndSpaces(word)

        binding.sendBtn.setOnClickListener { game(word) }
        binding.letterEditTxt.setOnEditorActionListener{ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                game(word)
                true
            } else {
                false
            }

        }


        countDownTimer = object : CountDownTimer(remainingTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTimeInMillis = millisUntilFinished
                binding.timeTxtView.text = formatTime(remainingTimeInMillis)
            }

            override fun onFinish() {
                // Timer has finished
                showGameResultDialog("You Lost", "Time's up!\nThe word was $word")
                binding.timeTxtView.text = "Time's up!"

            }
        }.start()
    } catch (ex: Exception) {
        error(ex)
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

    private fun game(word: String) {
        val letter = binding.letterEditTxt.text.toString().trim()
        if (letter.isNotEmpty()) {
            findLetterOnWord(letter[0], word)
            binding.letterEditTxt.text.clear()
        }

        if (changeImageCounter >= 10) {
            showGameResultDialog("You Lost", "The word was: $word")
            binding.timeTxtView.text = "00:00"
            countDownTimer.cancel()
        }

        if (allLettersFound(word)) {
            showGameResultDialog("You Won", "Congratulations!")
            countDownTimer.cancel()
        }

    }

    private fun formatTime(millis: Long): String {
        val minutes = millis / 1000 / 60
        val seconds = millis / 1000 % 60
        return String.format("%02d:%02d", minutes, seconds)
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

    private fun allLettersFound(word: String): Boolean {
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

    private fun showGameResultDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
            finish()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun loadFoundLetter(letter: Char, index: Int) {
        val textView = lettersMap[index]
        textView?.text = letter.toString()
    }

    private fun loadLettersAndSpaces(word: String) {
        var counter = 0
        var maxHelpers = word.length / 3
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