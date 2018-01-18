package com.yemyat.zopa.challenge;

import com.yemyat.zopa.challenge.error.EmptyDataSourceException;
import com.yemyat.zopa.challenge.error.InsufficientFundInSystemException;
import com.yemyat.zopa.challenge.error.UnsupportedMoneyDenominationException;
import com.yemyat.zopa.challenge.input.DataSource;
import com.yemyat.zopa.challenge.interest.EffectiveInterestRateFunction;
import com.yemyat.zopa.challenge.model.LenderInfo;
import com.yemyat.zopa.challenge.model.RepaymentInfo;
import org.junit.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoadPaymentCalculatorTest {

    @Test
    public void testRepaymentCalculationIsCorrect() {
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getData()).thenReturn(new LenderInfo[] {
                new LenderInfo(0.075, 640),
                new LenderInfo(0.069, 480),
                new LenderInfo(0.071, 520),
                new LenderInfo(0.104, 170),
                new LenderInfo(0.081, 320),
                new LenderInfo(0.074, 140),
                new LenderInfo(0.071, 60)
        });

        LoanRepaymentCalculator calculator = new LoanRepaymentCalculator(
                dataSource, new EffectiveInterestRateFunction(), 36);

        try {
            RepaymentInfo repaymentInfo = calculator.getRate(1000);
            assertEquals(repaymentInfo.getMonthlyInterestRate(), 0.07003999999999999);
            assertEquals(repaymentInfo.getMonthlyRepaymentAmount(), 30.780594385542404);
            assertEquals(repaymentInfo.getTotalRepayment(), 1108.1013978795265);
        } catch (Exception e) {
            Assert.fail("Should not throw any exception");
        }
    }

    @Test
    public void testEmptyDatasourceThrowException() {
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getData()).thenReturn(new LenderInfo[0]);

        try {
            new LoanRepaymentCalculator(dataSource, new EffectiveInterestRateFunction(), 36);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof EmptyDataSourceException);
        }
    }

    @Test
    public void testInsufficientFundThrowException() {
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getData()).thenReturn(new LenderInfo[] {new LenderInfo(0.05, 1000)});

        LoanRepaymentCalculator calculator = new LoanRepaymentCalculator(
                dataSource, new EffectiveInterestRateFunction(), 36);

        try {
            calculator.getRate(99999);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof InsufficientFundInSystemException);
        }
    }

    @Test
    public void testNotDivisibleByHundredThrowException() {
        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getData()).thenReturn(new LenderInfo[] {new LenderInfo(0.05, 1000000)});

        LoanRepaymentCalculator calculator = new LoanRepaymentCalculator(
                dataSource, new EffectiveInterestRateFunction(), 36);

        try {
            calculator.getRate(4444);
            Assert.fail("Expected exception to be thrown");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedMoneyDenominationException);
        }
    }

}
