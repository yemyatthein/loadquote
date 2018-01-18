package com.yemyat.zopa.challenge.model;

public final class RepaymentInfo {
    private double monthlyInterestRate;
    private double monthlyRepaymentAmount;
    private double totalRepayment;

    public RepaymentInfo(double monthlyInterestRate, double monthlyRepaymentAmount, double totalRepayment) {
        this.monthlyInterestRate = monthlyInterestRate;
        this.monthlyRepaymentAmount = monthlyRepaymentAmount;
        this.totalRepayment = totalRepayment;
    }

    public double getMonthlyInterestRate() {
        return monthlyInterestRate;
    }

    public double getMonthlyRepaymentAmount() {
        return monthlyRepaymentAmount;
    }

    public double getTotalRepayment() {
        return totalRepayment;
    }
}
