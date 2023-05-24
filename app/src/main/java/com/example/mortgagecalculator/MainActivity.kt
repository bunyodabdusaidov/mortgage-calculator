package com.example.mortgagecalculator

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etLoanAmount: EditText
    private lateinit var etInterestRate: EditText
    private lateinit var etLoanTerm: EditText
    private lateinit var btnCalculatePayment: Button
    private lateinit var btnRefinanceCalculator: Button
    private lateinit var btnAffordabilityCalculator: Button
    private lateinit var tvMonthlyPayment: TextView
    private lateinit var tvTotalPayment: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etLoanAmount = findViewById(R.id.et_loan_amount)
        etInterestRate = findViewById(R.id.et_interest_rate)
        etLoanTerm = findViewById(R.id.et_loan_term)
        btnCalculatePayment = findViewById(R.id.btn_calculate_payment)
        btnRefinanceCalculator = findViewById(R.id.btn_refinance_calculator)
        btnAffordabilityCalculator = findViewById(R.id.btn_affordability_calculator)
        tvMonthlyPayment = findViewById(R.id.tv_monthly_payment)
        tvTotalPayment = findViewById(R.id.tv_total_payment)

        btnCalculatePayment.setOnClickListener {
            if (validateInput()) {
                val loanAmount = etLoanAmount.text.toString().toDouble()
                val interestRate = etInterestRate.text.toString().toDouble()
                val loanTerm = etLoanTerm.text.toString().toInt()

                val monthlyPayment = calculateMonthlyPayment(loanAmount, interestRate, loanTerm)
                val totalPayment = monthlyPayment * loanTerm * 12

                tvMonthlyPayment.text = String.format("Monthly Payment: $%.2f", monthlyPayment)
                tvTotalPayment.text = String.format("Total Payment: $%.2f", totalPayment)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        btnRefinanceCalculator.setOnClickListener {
            val intent = Intent(this, RefinanceCalculatorActivity::class.java)
            startActivity(intent)
        }

        btnAffordabilityCalculator.setOnClickListener {
            val intent = Intent(this, AffordabilityCalculatorActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInput(): Boolean {
        return !TextUtils.isEmpty(etLoanAmount.text) &&
                !TextUtils.isEmpty(etInterestRate.text) &&
                !TextUtils.isEmpty(etLoanTerm.text)
    }

    private fun calculateMonthlyPayment(loanAmount: Double, interestRate: Double, loanTerm: Int): Double {
        val monthlyInterestRate = interestRate / 100.0 / 12.0
        val numberOfPayments = loanTerm * 12

        return if (monthlyInterestRate == 0.0) {
            loanAmount / numberOfPayments
        } else {
            val temp = Math.pow(1 + monthlyInterestRate, numberOfPayments.toDouble())
            loanAmount * monthlyInterestRate * temp / (temp - 1)
        }
    }
}
