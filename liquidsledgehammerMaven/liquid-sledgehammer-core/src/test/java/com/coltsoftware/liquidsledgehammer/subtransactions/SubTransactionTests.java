package com.coltsoftware.liquidsledgehammer.subtransactions;

import static org.junit.Assert.*;

import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;

import com.coltsoftware.liquidsledgehammer.MoneyTestBase;
import com.coltsoftware.liquidsledgehammer.model.FinancialTransaction;
import com.coltsoftware.liquidsledgehammer.model.Money;
import com.coltsoftware.liquidsledgehammer.model.NullFinancialTransactionSourceInformation;
import com.coltsoftware.liquidsledgehammer.model.SubTransaction;
import com.coltsoftware.liquidsledgehammer.model.FinancialTransaction.Builder;
import com.coltsoftware.liquidsledgehammer.subtransactions.SubTransactionFactory;

public final class SubTransactionTests extends MoneyTestBase {

	private Builder builder;

	@Before
	public void setup() {
		builder = new FinancialTransaction.Builder().source(
				NullFinancialTransactionSourceInformation.INSTANCE).date(2014,
				5, 1);
	}

	private Iterable<SubTransaction> getSubTransactions(
			FinancialTransaction transaction) {
		SubTransactionFactory factory = new SubTransactionFactory();
		return factory.getSubTransactions(transaction);
	}

	@Test
	public void can_get_single_sub_transaction() {
		FinancialTransaction transaction = builder.value(usd(10000)).build();
		assertEquals(1, count(getSubTransactions(transaction)));
	}

	@Test
	public void can_get_single_sub_transaction_and_has_same_value() {
		FinancialTransaction transaction = builder.value(usd(10000)).build();
		SubTransaction subTransaction = getSubTransactions(transaction)
				.iterator().next();
		assertSame(transaction.getValue(), subTransaction.getValue());
	}

	@Test
	public void can_get_single_sub_transaction_and_group_is_empty_string() {
		FinancialTransaction transaction = builder.value(usd(10000)).build();
		SubTransaction subTransaction = getSubTransactions(transaction)
				.iterator().next();
		assertEquals("", subTransaction.getGroup());
	}

	@Test
	public void transaction_with_one_group_has_one_sub_transaction_with_same_group() {
		FinancialTransaction transaction = builder.value(usd(10000))
				.groupPattern("one").build();
		assertEquals(1, count(getSubTransactions(transaction)));
		SubTransaction subTransaction = getSubTransactions(transaction)
				.iterator().next();
		assertEquals("one", subTransaction.getGroup());
	}

	@Test
	public void transaction_with_two_group_pattern_has_two_sub_transactions() {
		FinancialTransaction transaction = builder.value(usd(10000))
				.groupPattern("one=10,two").build();
		assertEquals(2, count(getSubTransactions(transaction)));
	}

	@Test
	public void transaction_with_two_group_pattern_has_sub_transactions_with_correct_group_names() {
		FinancialTransaction transaction = builder.value(usd(10000))
				.groupPattern("one=10,two").build();
		Iterator<SubTransaction> iterator = getSubTransactions(transaction)
				.iterator();
		assertEquals("one", iterator.next().getGroup());
		assertEquals("two", iterator.next().getGroup());
	}

	@Test
	public void transaction_with_one_group_with_remainer() {
		FinancialTransaction transaction = builder.value(usd(10000))
				.groupPattern("one=10").build();
		Iterator<SubTransaction> iterator = getSubTransactions(transaction)
				.iterator();
		assertEquals("one", iterator.next().getGroup());
		assertEquals("", iterator.next().getGroup());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void transaction_with_two_groups_values() {
		FinancialTransaction transaction = builder.value(usd(25000))
				.groupPattern("one=150").build();
		Iterator<SubTransaction> iterator = getSubTransactions(transaction)
				.iterator();
		SubTransaction sub = iterator.next();
		assertEquals("one", sub.getGroup());
		assertEquals(Money.fromString("150", usd), sub.getValue());
		sub = iterator.next();
		assertEquals("", sub.getGroup());
		assertEquals(Money.fromString("100", usd), sub.getValue());
		assertFalse(iterator.hasNext());
	}

}
