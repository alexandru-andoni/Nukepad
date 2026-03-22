/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adonis.Nukepad;

import java.awt.Component;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author croco
 */
public class FileTreeCellRenderer extends DefaultTreeCellRenderer {

    private Icon folderIcon = UIManager.getIcon("FileView.directoryIcon");
    private Icon fileIcon = UIManager.getIcon("FileView.fileIcon");
    
    @Override
    public Component getTreeCellRendererComponent(
            JTree tree, Object value, boolean sel,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object userObj = node.getUserObject();

        if (!(userObj instanceof File)) {
            setText(userObj.toString());
            setIcon(null);
            return this;
        }
        File file = (File) userObj;
        setText(file.getName());
        setIcon(file.isDirectory() ? folderIcon : fileIcon);
        return this;
    }
}
