/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adonis.Nukepad;

import java.awt.Component;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author croco
 */
public class FileListRenderer extends DefaultListCellRenderer {
    
  @Override
  public Component getListCellRendererComponent (
          JList<?> list, Object value, int index,
          boolean isSelected, boolean cellHasFocus
  ){
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      
      String path = (String) value;
      String fileName = new File(path).getName();
      String folder = new File(path).getParent();
      
      setText("<html><b>" + fileName + "</b><br>" +
              "<span style = 'color:#888888; font-size:10px'>" + folder + "</span></html>");
      setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
      
      return this;
  }
    
}
