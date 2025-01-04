package com.example.midtermproject

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UnitConversionActivity : AppCompatActivity() {

    private lateinit var unitInput: EditText
    private lateinit var unitOutput: TextView
    private lateinit var buttonConvert: Button
    private lateinit var categorySpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.conversion_layout)

        unitInput = findViewById(R.id.unitInput)
        unitOutput = findViewById(R.id.unitOutput)
        buttonConvert = findViewById(R.id.buttonConvert)
        categorySpinner = findViewById(R.id.categorySpinner)

        // Set up Spinner (dropdown) with categories
        val categorySpinner: Spinner = findViewById(R.id.categorySpinner)
        val categories = arrayOf( "Miles to Kilometers", "Pounds to Kilograms", "Celsius to Fahrenheit")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter

        buttonConvert.setOnClickListener {
            val selectedCategory = categorySpinner.selectedItem.toString()

            if (selectedCategory.isEmpty() || selectedCategory == "Select a category") {
                Toast.makeText(this, "Please select a valid category", Toast.LENGTH_SHORT).show()
            } else {
                performUnitConversion(selectedCategory)
            }
        }

    }

    private fun performUnitConversion(category: String) {
        val inputValue = unitInput.text.toString()

        if (inputValue.isEmpty()) {
            Toast.makeText(this, "Please enter a value to convert", Toast.LENGTH_SHORT).show()
            return
        }

        val value = inputValue.toDoubleOrNull()
        if (value == null) {
            Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            return
        }

        when (category) {
            "Miles to Kilometers" -> {
                unitOutput.text = "${value * 1.60934} Kilometers"
            }

            "Pounds to Kilograms" -> {
                unitOutput.text = "${value * 0.453592} Kilograms"
            }

            "Celsius to Fahrenheit" -> {
                unitOutput.text = "${(value * 9 / 5) + 32} Fahrenheit"
            }

            else -> {
                unitOutput.text = "Invalid category selected"
            }

        }
    }
}
