package com.coltsoftware.liquidsledgehammer.collections;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public final class FinancialTreeNodeTests extends FinancialTreeNodeTestsBase {

	@Test
	public void initial_size() {
		assertEquals(0, count(root.getSubTransactions()));
		assertTrue(root.getTotalValue().isZero());
	}

	@Test
	public void can_add_transaction() {
		root.add(createSubTransaction(usd(100)));
		assertEquals(1, count(root.getSubTransactions()));
		assertEquals(usd(100), root.getTotalValue());
	}

	@Test
	public void can_add_2_transactions() {
		root.add(createSubTransaction(usd(100)));
		root.add(createSubTransaction(usd(345)));
		assertEquals(2, count(root.getSubTransactions()));
		assertEquals(usd(445), root.getTotalValue());
	}

	@Test
	public void can_add_another_tree() {
		FinancialTreeNode ftn = new FinancialTreeNode("c1");
		ftn.add(createSubTransaction(gbp(100)));
		root.add(ftn);
		assertEquals(0, count(root.getSubTransactions()));
		assertEquals(gbp(100), root.getTotalValue());
	}

	@Test
	public void can_add_two_other_trees() {
		FinancialTreeNode ftn1 = new FinancialTreeNode("c1");
		FinancialTreeNode ftn2 = new FinancialTreeNode("c2");
		ftn1.add(createSubTransaction(yen(100)));
		ftn2.add(createSubTransaction(yen(200)));
		root.add(ftn1);
		root.add(ftn2);
		assertEquals(0, count(root.getSubTransactions()));
		assertEquals(yen(300), root.getTotalValue());
	}

	@Test
	public void parent_after_add_tree() {
		FinancialTreeNode ftn = new FinancialTreeNode("c1");
		root.add(ftn);
		assertEquals(root, ftn.getParent());
	}

	@Test(expected = TreeException.class)
	public void cant_assign_to_two_parents() {
		FinancialTreeNode otherRoot = new FinancialTreeNode();
		FinancialTreeNode ftn = new FinancialTreeNode();
		root.add(ftn);
		otherRoot.add(ftn);
	}

	@Test
	public void can_iterate_one_child() {
		FinancialTreeNode child1 = new FinancialTreeNode("c1");
		root.add(child1);
		assertEquals(1, count(root));
		assertSame(child1, root.iterator().next());
	}

	@Test
	public void can_iterate_two_children() {
		FinancialTreeNode child1 = new FinancialTreeNode("c1");
		FinancialTreeNode child2 = new FinancialTreeNode("c2");
		root.add(child1);
		root.add(child2);
		assertEquals(2, count(root));
		Iterator<FinancialTreeNode> iterator = root.iterator();
		assertSame(child1, iterator.next());
		assertSame(child2, iterator.next());
	}

}
