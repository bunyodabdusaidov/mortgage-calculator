package com.example.mortgagecalculator

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RefinanceCalculatorActivity : AppCompatActivity() {

    private lateinit var etCurrentLoanBalance: EditText
    private lateinit var etCurrentInterestRate: EditText
    private lateinit var etCurrentLoanTerm: EditText
    private lateinit var etNewInterestRate: EditText
    private lateinit var etNewLoanTerm: EditText
    private lateinit var etClosingCosts: EditText
    private lateinit var btnCalculateRefinance: Button
    private lateinit var tvMonthlySavings: TextView
    private lateinit var tvBreakEvenPoint: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refinance_calculator)

        etCurrentLoanBalance = findViewById(R.id.et_current_loan_balance)
        etCurrentInterestRate = findViewById(R.id.et_current_interest_rate)
        etCurrentLoanTerm = findViewById(R.id.et_current_loan_term)
        etNewInterestRate = findViewById(R.id.et_new_interest_rate)
        etNewLoanTerm = findViewById(R.id.et_new_loan_term)
        etClosingCosts = findViewById(R.id.et_closing_costs)
        btnCalculateRefinance = findViewById(R.id.btn_calculate_refinance)
        tvMonthlySavings = findViewById(R.id.tv_monthly_savings)
        tvBreakEvenPoint = findViewById(R.id.tv_break_even_point)

        btnCalculateRefinance.setOnClickListener {
            if (validateInput()) {
                val currentLoanBalance = etCurrentLoanBalance.text.toString().toDouble()
                val currentInterestRate = etCurrentInterestRate.text.toString().toDouble()
                val currentLoanTerm = etCurrentLoanTerm.text.toString().toInt()
                val newInterestRate = etNewInterestRate.text.toString().toDouble()
                val newLoanTerm = etNewLoanTerm.text.toString().toInt()
                val closingCosts = etClosingCosts.text.toString().toDouble()

                val currentMonthlyPayment = calculateMonthlyPayment(currentLoanBalance, currentInterestRate, currentLoanTerm)
                val newMonthlyPayment = calculateMonthlyPayment(currentLoanBalance, newInterestRate, newLoanTerm)
                val monthlySavings = currentMonthlyPayment - newMonthlyPayment
                val breakEvenPoint = closingCosts / monthlySavings

                tvMonthlySavings.text = String.format("Monthly Savings: $%.2f", monthlySavings)
                tvBreakEvenPoint.text = String.format("Break-Even Point: %.1f months", breakEvenPoint)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(): Boolean {
        return !TextUtils.isEmpty(etCurrentLoanBalance.text) &&
                !TextUtils.isEmpty(etCurrentInterestRate.text) &&
                !TextUtils.isEmpty(etCurrentLoanTerm.text) &&
                !TextUtils.isEmpty(etNewInterestRate.text) &&
                !TextUtils.isEmpty(etNewLoanTerm.text) &&
                !TextUtils.isEmpty(etClosingCosts.text)
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

