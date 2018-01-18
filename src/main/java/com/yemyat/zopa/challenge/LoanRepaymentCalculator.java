package com.yemyat.zopa.challenge;

import com.yemyat.zopa.challenge.error.EmptyDataSourceException;
import com.yemyat.zopa.challenge.error.InsufficientFundInSystemException;
import com.yemyat.zopa.challenge.error.UnsupportedMoneyDenominationException;
import com.yemyat.zopa.challenge.interest.InterestRateFunction;
import com.yemyat.zopa.challenge.model.LenderInfo;
import com.yemyat.zopa.challenge.model.RepaymentInfo;

import java.util.Arrays;
import java.util.Comparator;

public class LoanRepaymentCalculator {
    private LenderInfo[] lenders;
    private int availableAmount;
    private int duration;
    private InterestRateFunction interestRateFunction;

    public LoanRepaymentCalculator(DataSource dataSource, InterestRateFunction interestRateFunction, int duration) {
        this.duration = duration;
        this.interestRateFunction = interestRateFunction;
        lenders = dataSource.getData();

        if (lenders.length == 0) {
            throw new EmptyDataSourceException("Data source does not have any data");
        }

        Arrays.sort(lenders, Comparator.comparingDouble(LenderInfo::getRate));

        for (LenderInfo lender : lenders) {
            availableAmount += lender.getAmount();
        }
    }

    public RepaymentInfo getRate(int desiredAmount)
            throws InsufficientFundInSystemException, UnsupportedMoneyDenominationException {

        if (!canSatisfyDemand(desiredAmount)) {
            throw new InsufficientFundInSystemException(
                String.format("System does not have a sufficient amount to satisfy the desired amount - %d", desiredAmount));
        }

        if (!validDenomination(desiredAmount)) {
            throw new UnsupportedMoneyDenominationException("System allows only money amount divisible by 100");
        }

        double totalInterest = 0.0;
        int remainingAmountToSatisfy = desiredAmount;

        for (LenderInfo lender : this.lenders) {
            int borrowedAmount = Math.min(remainingAmountToSatisfy, lender.getAmount());
            totalInterest += borrowedAmount * lender.getRate();
            remainingAmountToSatisfy -= borrowedAmount;
            if (remainingAmountToSatisfy == 0) {
                break;
            }
        }

        double annualInterestRate = totalInterest / desiredAmount;
        double effectiveInterestRatePerMonth = interestRateFunction.getMonthlyInterestRate(annualInterestRate);

        // Reference: http://www.financeformulas.net/Loan_Payment_Formula.html
        double monthlyPayment = (effectiveInterestRatePerMonth * desiredAmount) /
                (1 - (Math.pow(1 + effectiveInterestRatePerMonth, - duration)));

        return new RepaymentInfo(annualInterestRate, monthlyPayment, monthlyPayment * duration);
    }

    private boolean validDenomination(int desiredAmount) {
        return desiredAmount % 100 == 0;
    }

    private boolean canSatisfyDemand(int desiredAmount) {
        return desiredAmount <= availableAmount && desiredAmount >= 1000 && desiredAmount <= 15000;
    }
}
