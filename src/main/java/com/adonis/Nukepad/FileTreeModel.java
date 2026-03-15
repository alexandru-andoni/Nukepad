/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adonis.Nukepad;

import java.io.File;
import java.util.Arrays;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author croco
 */
public class FileTreeModel extends DefaultTreeModel {
    public FileTreeModel(File root) {
        super(createNodes(root));
    }
    private static DefaultMutableTreeNode createNodes(File file) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
        
        File[] files = file.listFiles();
        if(files == null) return node;
        
        Arrays.sort(files, (a,b) -> {
            if(a.isDirectory() && !b.isDirectory()) return -1;
            if(!a.isDirectory() && b.isDirectory()) return 1;
            return a.getName().compareToIgnoreCase(b.getName());
        });
        for(File f : files) {
            node.add(createNodes(f));
        }
        return node;
    }
    
}
