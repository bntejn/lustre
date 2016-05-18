package coverage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jkind.lustre.BinaryExpr;
import jkind.lustre.BinaryOp;
import jkind.lustre.BoolExpr;
import jkind.lustre.IdExpr;
import jkind.lustre.Node;
import jkind.lustre.VarDecl;

public class SequentialEquation {
	
	public List<Obligation> generate(HashMap<VarDecl, ObservedTree> sequantialTrees) {
		List<Obligation> obligations = new ArrayList<Obligation>();
		ObservedTree tree;
		
		for (VarDecl root: sequantialTrees.keySet()) {
			System.out.println("Generate delay dependency epxressions for [" + root + "]...");
			tree = sequantialTrees.get(root);
			obligations.addAll(gerenateExprForTree(tree));
		}
		return obligations;
	}
	
	private List<Obligation> gerenateExprForTree(ObservedTree tree) {
		List<Obligation> obligations = new ArrayList<Obligation>();
		ObservedTreeNode root = tree.root;
		List<ObservedTreeNode> firstLevelChildren = root.getChildren();
		String seqUsedBy = "_SEQ_USED_BY_";
		IdExpr lhs;
		for (ObservedTreeNode node : firstLevelChildren) {
			lhs = new IdExpr(node.data + seqUsedBy + root.data);
			Obligation obligation = new Obligation(lhs, false, new BoolExpr(true));
			System.out.println(obligation.condition + " = " + obligation);
			obligations.add(obligation);

			for (ObservedTreeNode child : node.getChildren()) {
				obligations.addAll(genereateExprForNode(child, root));
			}
		}
		return obligations;
	}
	
	private List<Obligation> genereateExprForNode(ObservedTreeNode node, ObservedTreeNode root) {
		List<Obligation> obligations = new ArrayList<Obligation>();
		if (node == null) {
			return null;
		}
		
		IdExpr lhs;
		Obligation obligation;
		String seqUsedBy = "_SEQ_USED_BY_";
		String combUsedBy = "_COMB_USED_BY_";
//		System.out.println(node.data + "'s parent is " + node.getParent().data);
		lhs = new IdExpr(node.data + seqUsedBy + root.data);
		IdExpr opr1 = new IdExpr(node.data + combUsedBy + node.getParent().data);
		IdExpr opr2 = new IdExpr(node.getParent().data + seqUsedBy + root.data);
		BinaryExpr expr = new BinaryExpr(opr1, BinaryOp.AND, opr2);
		obligation = new Obligation(lhs, false, expr);
		obligations.add(obligation);
		System.out.println(obligation.condition + " = " + obligation);
		
		if (node.getChildren() != null) {
			for (ObservedTreeNode child : node.getChildren()) {
				obligations.addAll(genereateExprForNode(child, root));
			}
		}
		return obligations;
	}
}
