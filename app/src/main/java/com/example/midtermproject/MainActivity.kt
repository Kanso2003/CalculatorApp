package com.example.midtermproject

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var display: TextView
    private lateinit var expression: TextView
    private var currentNumber = ""
    private var previousNumber = ""
    private var operation = ""
    private var result = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)
        expression = findViewById(R.id.expression)

        // Set up button click listeners
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
}