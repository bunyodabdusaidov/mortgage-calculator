package com.example.mortgagecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class AffordabilityCalculatorActivity : AppCompatActivity() {

    private lateinit var incomeEditText: EditText
    private lateinit var debtEditText: EditText
    private lateinit var interestEditText: EditText
    private lateinit var termEditText: EditText
    private lateinit var downPaymentEditText: EditText
    private lateinit var maxPaymentTextView: TextView
    private lateinit var maxHomePriceTextView: TextView
    private lateinit var calculateBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_affordability_calculator)

        // Initialize layout components
        incomeEditText = findViewById(R.id.income_edit_text)
        debtEditText = findViewById(R.id.debt_edit_text)
        interestEditText = findViewById(R.id.interest_edit_text)
        termEditText = findViewById(R.id.term_edit_text)
        downPaymentEditText = findViewById(R.id.down_payment_edit_text)
        maxPaymentTextView = findViewById(R.id.max_payment_text_view)
        maxHomePriceTextView = findViewById(R.id.max_home_price_text_view)
        calculateBtn = findViewById(R.id.calculate_button)

        calculateBtn.setOnClickListener {
            if (validateInput()) {
                // Get user inputs
                val monthlyIncome = incomeEditText.text.toString().toDouble()
                val monthlyDebt = debtEditText.text.toString().toDouble()
                val interestRate = interestEditText.text.toString().toDouble() / 100
                val loanTermYears = termEditText.text.toString().toInt()
                val downPayment = downPaymentEditText.text.toString().toDouble()

                // Calculate maximum affordable monthly mortgage payment and maximum affordable home price
                val calculator = AffordabilityCalculator(monthlyIncome, monthlyDebt)
                val maxPayment = calculator.calculateAffordableMortgagePayment(
                    interestRate,
                    loanTermYears,
                    downPayment
                )
                val maxHomePrice = calculator.calculateAffordableHomePrice(
                    interestRate,
                    loanTermYears,
                    downPayment
                )

                // Display results
                maxPaymentTextView.text =
                    String.format("Max affordable monthly payment: $%.2f", maxPayment)
                maxHomePriceTextView.text =
                    String.format("Max affordable home price: $%.2f", maxHomePrice)
            }
        }
    }

    private fun validateInput(): Boolean {
        return !TextUtils.isEmpty(incomeEditText.text) &&
                !TextUtils.isEmpty(debtEditText.text) &&
                !TextUtils.isEmpty(interestEditText.text) &&
                !TextUtils.isEmpty(termEditText.text) &&
                !TextUtils.isEmpty(downPaymentEditText.text)
    }
}
