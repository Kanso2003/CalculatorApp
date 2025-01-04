package com.example.midtermproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton  // Import AppCompatImageButton

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private lateinit var expression: TextView
    private lateinit var buttonTemp: Button
    private var currentNumber = ""
    private var previousNumber = ""
    private var operation = ""
    private var result = ""

    // List to store calculation history in the current session
    private val calculationHistory = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)
        expression = findViewById(R.id.expression)

        buttonTemp = findViewById(R.id.buttonTemp)
        buttonTemp.setOnClickListener {
            // Open Unit Conversion Activity
            val intent = Intent(this, UnitConversionActivity::class.java)
            startActivity(intent)
        }

        // Set up the menu button with the correct AppCompatImageButton
        val menuButton = findViewById<AppCompatImageButton>(R.id.menuButton)
        menuButton.setOnClickListener {
            showCalculationHistory()
        }

        // Set up button click listeners for the calculator buttons
        val buttons = listOf(
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9,
            R.id.buttonDot, R.id.buttonPlus, R.id.buttonMinus,
            R.id.buttonMultiply, R.id.buttonDivide, R.id.buttonEquals,
            R.id.buttonClear, R.id.buttonPercentage, R.id.buttonPlusMinus
        )

        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener { onButtonClick(it) }
        }
    }

    private fun onButtonClick(view: View) {
        val button = view as Button
        val buttonText = button.text.toString()

        when (buttonText) {
            "AC" -> clear()
            "+", "-", "×", "÷" -> setOperation(buttonText)
            "=" -> calculateResult()
            "." -> appendDecimal()
            "%" -> calculatePercentage()
            "+/-" -> toggleSign()
            else -> appendNumber(buttonText)
        }
    }

    private fun clear() {
        currentNumber = ""
        previousNumber = ""
        operation = ""
        result = ""
        display.text = "0"
        expression.text = ""
    }

    private fun appendNumber(number: String) {
        if (result.isNotEmpty() && operation.isEmpty()) {
            // Start a new calculation after showing a result
            clear()
        }
        currentNumber += number
        display.text = currentNumber
    }

    private fun appendDecimal() {
        if (!currentNumber.contains(".")) {
            currentNumber += "."
            display.text = currentNumber
        }
    }

    private fun setOperation(op: String) {
        if (currentNumber.isNotEmpty()) {
            if (previousNumber.isNotEmpty()) {
                // Calculate the intermediate result for cumulative operations
                calculateIntermediateResult()
            } else {
                previousNumber = currentNumber
            }
            operation = op
            currentNumber = ""
            expression.text = "$previousNumber $operation"
        } else if (result.isNotEmpty()) {
            previousNumber = result
            operation = op
            expression.text = "$previousNumber $operation"
        }
    }

    private fun calculateIntermediateResult() {
        if (previousNumber.isNotEmpty() && currentNumber.isNotEmpty()) {
            val num1 = previousNumber.toDouble()
            val num2 = currentNumber.toDouble()

            val intermediateResult = when (operation) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "×" -> num1 * num2
                "÷" -> if (num2 != 0.0) num1 / num2 else Double.NaN
                else -> 0.0
            }

            previousNumber = intermediateResult.toString()
            display.text = previousNumber
        }
    }

    private fun calculateResult() {
        if (previousNumber.isNotEmpty() && currentNumber.isNotEmpty()) {
            val num1 = previousNumber.toDouble()
            val num2 = currentNumber.toDouble()

            val finalResult = when (operation) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "×" -> num1 * num2
                "÷" -> if (num2 != 0.0) num1 / num2 else Double.NaN
                else -> 0.0
            }

            result = finalResult.toString()
            expression.text = "$previousNumber $operation $currentNumber"
            display.text = result
            previousNumber = result
            currentNumber = ""
            operation = ""

            // Save the calculation in the history
            saveCalculationToHistory(expression.text.toString(), result)
        }
    }

    private fun calculatePercentage() {
        if (currentNumber.isNotEmpty()) {
            val number = currentNumber.toDouble()
            currentNumber = (number / 100).toString()
            display.text = currentNumber
        }
    }

    private fun toggleSign() {
        if (currentNumber.isNotEmpty()) {
            val number = currentNumber.toDouble()
            currentNumber = (-number).toString()
            display.text = currentNumber
        }
    }

    private fun saveCalculationToHistory(calc: String, result: String) {
        calculationHistory.add("$calc = $result")
    }


    private fun showCalculationHistory() {
        if (calculationHistory.isEmpty()) {
            Toast.makeText(this, "No calculation history", Toast.LENGTH_SHORT).show()
            return
        }

        // Show calculation history in a dialog
        val historyDialog = AlertDialog.Builder(this)
            .setTitle("Calculation History")
            .setItems(calculationHistory.toTypedArray()) { _, which ->
                val selectedCalculation = calculationHistory[which]
                val result = selectedCalculation.split("=")[1].trim()

                // Set the selected result to the calculator display
                display.text = result
                currentNumber = result
                previousNumber = ""
                operation = ""
                expression.text = selectedCalculation.split("=")[0]
            }
            .setNegativeButton("Cancel", null)
            .create()

        historyDialog.show()
    }
}

