package com.yemyat.zopa.challenge.interest;

public class EffectiveInterestRateFunction implements InterestRateFunction {

    @Override
    public double getMonthlyInterestRate(double annualInterestRate) {
        // References:
        //  - https://www.experiglot.com/2006/06/07/how-to-convert-from-an-annual-rate-to-an-effective-periodic-rate-javascript-calculator/
        //  - https://www.engineeringtoolbox.com/effective-nominal-interest-rates-d_1468.html
        return Math.pow((1 + annualInterestRate), (1.0 / 12)) - 1;
    }

}
