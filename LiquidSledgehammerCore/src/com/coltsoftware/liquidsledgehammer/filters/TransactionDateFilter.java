package com.coltsoftware.liquidsledgehammer.filters;

import org.joda.time.LocalDate;

import com.coltsoftware.liquidsledgehammer.model.FinancialTransaction;

public final class TransactionDateFilter {

	public static class Builder {
		private LocalDate minimumDate;
		private LocalDate maximumDate;

		public TransactionFilter build() {
			if (maximumDate != null && minimumDate != null)
				return NullTransactionFilter.INSTANCE;
			if (maximumDate != null)
				return new TransactionDateFilter.MaxDateFilter(maximumDate);
			if (minimumDate != null)
				return new TransactionDateFilter.MinDateFilter(minimumDate);
			return NullTransactionFilter.INSTANCE;
		}

		public Builder setMinimumDate(int year, int month, int day) {
			return setMinimumDate(ymdToLocalDate(year, month, day));
		}

		private LocalDate ymdToLocalDate(int year, int month, int day) {
			return new LocalDate(year, month, day);
		}

		public Builder setMinimumDate(LocalDate date) {
			this.minimumDate = date;
			return this;
		}

		public Builder setMaximumDate(int year, int month, int day) {
			return setMaximumDate(ymdToLocalDate(year, month, day));
		}

		public Builder setMaximumDate(LocalDate date) {
			this.maximumDate = date;
			return this;
		}
	}

	public static abstract class SingleDateFilter implements TransactionFilter {

		protected final LocalDate date;

		private SingleDateFilter(LocalDate date) {
			this.date = date;
		}
	}

	public static class MinDateFilter extends SingleDateFilter {

		private MinDateFilter(LocalDate minimumDate) {
			super(minimumDate);
		}

		@Override
		public boolean allow(FinancialTransaction transaction) {
			return transaction.getDate().compareTo(date) >= 0;
		}
	}

	public static class MaxDateFilter extends SingleDateFilter {

		private MaxDateFilter(LocalDate maximumDate) {
			super(maximumDate);
		}

		@Override
		public boolean allow(FinancialTransaction transaction) {
			return transaction.getDate().compareTo(date) <= 0;
		}
	}

}