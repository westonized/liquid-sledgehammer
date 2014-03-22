package com.coltsoftware.liquidsledgehammer.collections;

import static org.junit.Assert.*;

import org.junit.Test;

public final class FinancialTreeNodeTests extends FinancialTreeNodeTestsBase {

	@Test
	public void initial_size() {
		assertEquals(0, count(root.getTransactions()));
		assertEquals(0, root.getTotalValue().getValue());
	}

	@Test
	public void can_add_transaction() {
		root.add(createTransaction(100));
		assertEquals(1, count(root.getTransactions()));
		assertEquals(100, root.getTotalValue().getValue());
	}

	@Test
	public void can_add_2_transactions() {
		root.add(createTransaction(100));
		root.add(createTransaction(345));
		assertEquals(2, count(root.getTransactions()));
		assertEquals(445, root.getTotalValue().getValue());
	}

	@Test
	public void can_add_another_tree() {
		FinancialTreeNode ftn = new FinancialTreeNode();
		ftn.add(createTransaction(100));
		root.add(ftn);
		assertEquals(0, count(root.getTransactions()));
		assertEquals(100, root.getTotalValue().getValue());
	}

	@Test
	public void can_add_two_other_trees() {
		FinancialTreeNode ftn1 = new FinancialTreeNode();
		FinancialTreeNode ftn2 = new FinancialTreeNode();
		ftn1.add(createTransaction(100));
		ftn2.add(createTransaction(200));
		root.add(ftn1);
		root.add(ftn2);
		assertEquals(0, count(root.getTransactions()));
		assertEquals(300, root.getTotalValue().getValue());
	}

	@Test
	public void parent_after_add_tree() {
		FinancialTreeNode ftn = new FinancialTreeNode();
		root.add(ftn);
		assertEquals(root, ftn.getParent());
	}

	@Test(expected=TreeException.class)
	public void cant_assign_to_two_parents() {
		FinancialTreeNode otherRoot = new FinancialTreeNode();
		FinancialTreeNode ftn = new FinancialTreeNode();		
		root.add(ftn);
		otherRoot.add(ftn);
	}

}