package com.coltsoftware.liquidsledgehammer.commands;

import java.io.PrintStream;
import java.util.ArrayList;

import com.coltsoftware.liquidsledgehammer.collections.FinancialTreeNode;

public abstract class PathCommandBase {

    protected static FinancialTreeNode findPath(PrintStream out,
                                                FinancialTreeNode node, String pathString) {
        if ("".equals(pathString))
            return node;
        String[] paths = "..".equals(pathString) ? new String[]{pathString}
                : pathString.split("[./]");
        for (String path : paths) {
            if ("~".equals(path))
                node = node.getRoot();
            else if ("..".equals(path))
                node = node.getParent();
            else {
                FinancialTreeNode newNode = findChild(node, path, out);
                if (newNode == node)
                    break;
                node = newNode;
            }
        }
        return node;
    }

    protected static FinancialTreeNode findChild(FinancialTreeNode node,
                                                 String path, PrintStream out) {
        ArrayList<FinancialTreeNode> possibles = new ArrayList<FinancialTreeNode>();
        for (FinancialTreeNode child : node) {
            String name = child.getName();
            if (name.equals(path))
                return child;
            if (name.startsWith(path))
                possibles.add(child);
        }
        switch (possibles.size()) {
            case 1:
                return possibles.get(0);
            case 0: {
                out.println(String.format(
                        "Can't find any match for \"%s\" in \"%s\"", path,
                        node.getName()));
                return node;
            }
            default: {
                out.println(String.format("Multiple matches for \"%s\" in \"%s\"",
                        path, node.getName()));
                for (FinancialTreeNode possible : possibles)
                    out.println(possible.getName());
                return node;
            }
        }
    }
}