package com.yemyat.zopa.challenge;

import com.yemyat.zopa.challenge.error.InsufficientFundInSystemException;
import com.yemyat.zopa.challenge.error.UnsupportedMoneyDenominationException;
import com.yemyat.zopa.challenge.interest.EffectiveInterestRateFunction;
import com.yemyat.zopa.challenge.interest.InterestRateFunction;
import com.yemyat.zopa.challenge.model.RepaymentInfo;

public class Main {
    public static void main(String[] args) {

        if (args.length < 2) {
            System.err.println("Expected arguments not found. Usage: ./<program> <csv-data-file> <desired-amount>");
            System.exit(1);
        }

        try {
            int desiredAmount = Integer.parseInt(args[1]);
            DataSource dataSource = new CSVDataSource(args[0]);
            InterestRateFunction interestRateFunction = new EffectiveInterestRateFunction();
            LoanRepaymentCalculator calculator = new LoanRepaymentCalculator(dataSource, interestRateFunction, 36);
            RepaymentInfo repaymentInfo = calculator.getRate(desiredAmount);

            System.out.println(String.format("Requested amount: £%d", desiredAmount));
            System.out.println(String.format("Rate: %.1f%%", repaymentInfo.getMonthlyInterestRate() * 100));
            System.out.println(String.format("Monthly repayment: %.2f", repaymentInfo.getMonthlyRepaymentAmount()));
            System.out.println(String.format("Total repayment: %.2f", repaymentInfo.getTotalRepayment()));
        }
        catch (NumberFormatException e) {
            System.err.println("Requested amount should be an integer");
            System.exit(1);
        } catch (InsufficientFundInSystemException e) {
            System.err.println("Not possible to quote at the moment");
            System.exit(1);
        } catch (UnsupportedMoneyDenominationException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
