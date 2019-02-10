package com.rivers.core.tree;

import java.util.List;

public class TreeNode {
	private int id;
	private int parentId;
	private List<TreeNode> children = null;

	public void add(TreeNode node) {
		children.add(node);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
}