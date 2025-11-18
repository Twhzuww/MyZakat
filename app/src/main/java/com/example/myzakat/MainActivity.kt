package com.example.myzakat

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private lateinit var editWeight: TextInputEditText
    private lateinit var editValue: TextInputEditText
    private lateinit var spinnerType: AutoCompleteTextView
    private lateinit var textTotalValue: TextView
    private lateinit var textZakatPayable: TextView
    private lateinit var btnCalculate: Button
    private lateinit var btnAbout: Button

    private val df = DecimalFormat("#,##0.00")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        editWeight = findViewById(R.id.editWeight)
        editValue = findViewById(R.id.editValue)
        spinnerType = findViewById(R.id.spinnerType)
        textTotalValue = findViewById(R.id.textTotalValue)
        textZakatPayable = findViewById(R.id.textZakatPayable)
        btnCalculate = findViewById(R.id.btnCalculate)
        btnAbout = findViewById(R.id.btnAbout)

        // Load spinner items
        val goldTypes = resources.getStringArray(R.array.gold_types)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, goldTypes)
        spinnerType.setAdapter(adapter)

        // Calculate button
        btnCalculate.setOnClickListener {
            calculateZakat()
        }

        // About button
        btnAbout.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun calculateZakat() {
        val sWeight = editWeight.text.toString().trim()
        val sValue = editValue.text.toString().trim()
        val type = spinnerType.text.toString()

        if (sWeight.isEmpty() || sValue.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val weight = sWeight.toDoubleOrNull()
        val value = sValue.toDoubleOrNull()

        if (weight == null || value == null) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show()
            return
        }

        val uruf = if (type.equals("Keep", ignoreCase = true)) 85.0 else 200.0

        val totalValue = weight * value
        var zakatBase = weight - uruf
        if (zakatBase < 0) zakatBase = 0.0

        val zakatValue = zakatBase * value
        val totalZakat = zakatValue * 0.025

        textTotalValue.text = "Total gold value: RM ${df.format(totalValue)}"
        textZakatPayable.text = "Zakat payable: RM ${df.format(totalZakat)}"
    }

    // Share button menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                val url = "https://github.com/Twhzuww/ZakatCalculator"
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Zakat Calculator App")
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this Zakat Calculator: $url")
                startActivity(Intent.createChooser(shareIntent, "Share using"))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
