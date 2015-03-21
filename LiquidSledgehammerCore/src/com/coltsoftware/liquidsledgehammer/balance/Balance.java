package com.coltsoftware.liquidsledgehammer.balance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.joda.time.LocalDate;

import com.coltsoftware.liquidsledgehammer.model.FinancialTransaction;
import com.coltsoftware.liquidsledgehammer.model.Money;
import com.coltsoftware.liquidsledgehammer.sources.FinancialTransactionSource;

public final class Balance {

	private final HashMap<LocalDate, Money> dailyBalances = new HashMap<LocalDate, Money>();
	private final Money latestBalance;
	private final LocalDate minDate;
	private final LocalDate maxDate;

	public Balance(FinancialTransactionSource source) {
		List<FinancialTransaction> transactions = new ArrayList<FinancialTransaction>();
		for (FinancialTransaction transaction : source)
			transactions.add(transaction);

		Collections.sort(transactions, new Comparator<FinancialTransaction>() {

			@Override
			public int compare(FinancialTransaction o1, FinancialTransaction o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});

		if (!transactions.isEmpty()) {
			minDate = transactions.get(0).getDate();
			maxDate = transactions.get(transactions.size() - 1).getDate();
		} else {
			minDate = null;
			maxDate = null;
		}

		Money result = Money.Zero;
		for (FinancialTransaction transaction : transactions) {
			result = result.add(transaction.getValue());
			dailyBalances.put(transaction.getDate(), result);
		}
		latestBalance = result;
	}

	public static Balance fromTransactionSource(
			FinancialTransactionSource source) {
		return new Balance(source);
	}

	public Money getBalance() {
		return latestBalance;
	}

	public Money getBalance(int year, int month, int day) {
		return getBalance(new LocalDate(year, month, day));
	}

	public Money getBalance(LocalDate localDate) {
		Money money = dailyBalances.get(localDate);
		if (money == null)
			return closestBalance(localDate);
		return money;
	}

	private Money closestBalance(LocalDate localDate) {
		LocalDate closest = null;
		for (LocalDate balanceDate : dailyBalances.keySet()) {
			if (balanceDate.isBefore(localDate)
					&& (closest == null || balanceDate.isAfter(closest))) {
				closest = balanceDate;
			}
		}
		if (closest == null)
			return Money.Zero;
		return dailyBalances.get(closest);
	}

	public LocalDate getMinDate() {
		if (minDate == null)
			return defaultMinMaxDate();
		return minDate;
	}

	public LocalDate getMaxDate() {
		if (maxDate == null)
			return defaultMinMaxDate();
		return maxDate;
	}

	protected LocalDate defaultMinMaxDate() {
		return new LocalDate();
	}
}
