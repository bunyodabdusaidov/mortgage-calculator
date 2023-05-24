package com.example.mortgagecalculator

import kotlin.math.pow

class AffordabilityCalculator(private val monthlyIncome: Double, private val monthlyDebt: Double) {

    // Maximum percentage of monthly income to be used for housing expenses
    private val housingExpenseRatio = 0.28

    // Maximum percentage of monthly income to be used for total debt payments
    private val debtToIncomeRatio = 0.36

    // Calculates the maximum affordable monthly mortgage payment
    fun calculateAffordableMortgagePayment(
        interestRate: Double,
        loanTermYears: Int,
        downPayment: Double
    ): Double {
        val loanAmount = calculateLoanAmount(interestRate, loanTermYears, downPayment)
        val totalHousingExpenses = monthlyIncome * housingExpenseRatio
        val totalDebtPayments = monthlyIncome * debtToIncomeRatio - monthlyDebt
        val maxHousingExpenses =
            minOf(totalHousingExpenses, totalHousingExpenses + totalDebtPayments)
        val maxMortgagePayment = (maxHousingExpenses / calculateMonthlyPayment(
            interestRate,
            loanTermYears,
            loanAmount
        )).roundToTwoDecimalPlaces()
        return maxMortgagePayment
    }

    // Calculates the maximum affordable home price
    fun calculateAffordableHomePrice(
        interestRate: Double,
        loanTermYears: Int,
        downPayment: Double
    ): Double {
        val maxMortgagePayment =
            calculateAffordableMortgagePayment(interestRate, loanTermYears, downPayment)
        val loanAmount = calculateLoanAmount(interestRate, loanTermYears, downPayment)
        val affordableHomePrice = (loanAmount + downPayment).roundToTwoDecimalPlaces()
        return affordableHomePrice
    }

    // Calculates the loan amount based on the interest rate, loan term and down payment
    private fun calculateLoanAmount(
        interestRate: Double,
        loanTermYears: Int,
        downPayment: Double
    ): Double {
        val loanTermMonths = loanTermYears * 12
        val principal = (monthlyIncome - monthlyDebt) * (debtToIncomeRatio - housingExpenseRatio)
        val monthlyInterestRate = interestRate / 12
        val numerator = monthlyInterestRate * (1 + monthlyInterestRate).pow(loanTermMonths)
        val denominator = (1 + monthlyInterestRate).pow(loanTermMonths) - 1
        val monthlyPayment = (principal * (numerator / denominator)).roundToTwoDecimalPlaces()
        val loanAmount = (monthlyPayment * loanTermMonths + downPayment).roundToTwoDecimalPlaces()
        return loanAmount
    }

    // Calculates the monthly payment based on the interest rate, loan term and loan amount
    private fun calculateMonthlyPayment(
        interestRate: Double,
        loanTermYears: Int,
        loanAmount: Double
    ): Double {
        val loanTermMonths = loanTermYears * 12
        val monthlyInterestRate = interestRate / 12
        val numerator = monthlyInterestRate * (1 + monthlyInterestRate).pow(loanTermMonths)
        val denominator = (1 + monthlyInterestRate).pow(loanTermMonths) - 1
        val monthlyPayment = (loanAmount * (numerator / denominator)).roundToTwoDecimalPlaces()
        return monthlyPayment
    }

    // Rounds a double value to two decimal places
    private fun Double.roundToTwoDecimalPlaces(): Double {
        val formattedString = "%,.2f".format(this) // format with comma separator
        val cleanString = formattedString.replace(",", "") // remove comma separator
        return cleanString.toDouble()
    }
}
