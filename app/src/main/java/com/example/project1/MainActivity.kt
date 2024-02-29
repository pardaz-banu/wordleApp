package com.example.project1

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
class MainActivity : AppCompatActivity() {
    private lateinit var adapter: Adapter
    private lateinit var adapterWithoutCorrectness : Adapter
    private var remainingGuesses: Int = 3
    private var targetWord: String = ""
    private lateinit var wordList: FourLetterWordList
    private lateinit var edittext: EditText
    private lateinit var mainTextView: TextView
    private lateinit var guessbutton: Button
    private lateinit var mainRecyclerView: RecyclerView
    private lateinit var mainRecyclerViewWithoutCorrectness: RecyclerView
    private lateinit var resetButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainRecyclerView = findViewById(R.id.recyle)
        mainRecyclerViewWithoutCorrectness = findViewById(R.id.recyle)

        edittext = findViewById(R.id.editText)
        guessbutton = findViewById(R.id.guessButton)
        mainTextView = findViewById(R.id.mainTextView)
        resetButton = findViewById(R.id.resetButton)

        val datalistWithoutCorrectness = mutableListOf<Todo>()
        adapterWithoutCorrectness = Adapter(datalistWithoutCorrectness, false)
        mainRecyclerViewWithoutCorrectness.layoutManager = LinearLayoutManager(this)
        mainRecyclerViewWithoutCorrectness.adapter = adapterWithoutCorrectness

        val datalist = mutableListOf<Todo>()
        adapter = Adapter(datalist, true)
        mainRecyclerView.layoutManager = LinearLayoutManager(this)
        mainRecyclerView.adapter = adapter

        wordList = FourLetterWordList

        resetGame()

        guessbutton.setOnClickListener {
            if (remainingGuesses > 0) {
                val userInput = edittext.text.toString().toUpperCase()
                val correctness = checkGuess(userInput, targetWord)
                if (isValidGuess(userInput)) {
                    val correctness = checkGuess(userInput, targetWord)

                    // Update UI based on correctness
                    mainTextView.text = "Correctness: $correctness"

                    val guessTodo = Todo("Guess #${adapter.itemCount + 1}", correctness)

                    adapterWithoutCorrectness.addTodo(guessTodo)
                    adapterWithoutCorrectness.notifyItemInserted(adapterWithoutCorrectness.itemCount - 1)

                    adapter.addTodo(guessTodo)
                    adapter.notifyItemInserted(adapter.itemCount - 1)

                    // Decrease remaining guesses
                    remainingGuesses--

                    if (correctness == "OOOO") {
                        // Correct word guessed, change background color
                        changeBackgroundColor(R.color.correctBackground)
                    }

                    if (remainingGuesses == 0) {
                        disableGuessButton()
                        mainTextView.text = "Game over! The target word was: $targetWord"
                        changeBackgroundColor(R.color.gameOverBackground)

                    }

                    edittext.text.clear()
                } else {
                    // Show an error message for invalid guess
                    Toast.makeText(this, "Invalid guess. Please enter a 4-letter word.", Toast.LENGTH_SHORT).show()
                }

                edittext.text.clear()
            }
        }

        resetButton.setOnClickListener {
            resetGame()
            enableGuessButton()
            mainTextView.text = "Guess the 4-letter word!"

            adapterWithoutCorrectness.clearTodos()
            adapter.clearTodos()
            changeBackgroundColor(android.R.color.white)

        }

    }


    private fun changeBackgroundColor(colorResId: Int) {
        // Change background color of relevant UI elements
        mainRecyclerView.setBackgroundColor(ContextCompat.getColor(this, colorResId))
        mainTextView.setBackgroundColor(ContextCompat.getColor(this, colorResId))
    }

    private fun resetGame() {
        // Get a new random 4-letter word
        targetWord = wordList.getRandomFourLetterWord().toUpperCase()
        remainingGuesses = 3

        // Clear UI elements
        mainTextView.text = "Guess the 4-letter word!"
        edittext.text.clear()
        guessbutton.isEnabled = true
    }

    private fun checkGuess(guess: String, targetWord: String): String {
        var result = ""
        for (i in 0..3) {
            if (guess[i] == targetWord[i]) {
                result += "O"
            } else if (guess[i] in targetWord) {
                result += "+"
            } else {
                result += "X"
            }
        }
        return result
    }

    private fun enableGuessButton() {
        guessbutton.isEnabled = true
    }
    private fun disableGuessButton() {
        guessbutton.isEnabled = false
    }

    private fun isValidGuess(guess: String): Boolean {
        // Validate that the guess is a 4-letter word
        return guess.length == 4 && guess.all { it.isLetter() }
    }
}
