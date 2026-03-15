/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.adonis.Nukepad;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author croco
 */
class LineNumberPanel extends JPanel implements CaretListener, DocumentListener {
    private final JTextArea textArea;
    private int lastDigits;
    
    public LineNumberPanel(JTextArea textArea) {
        this.textArea = textArea;
        textArea.getDocument().addDocumentListener(this);
        textArea.addCaretListener(this);
        setPreferredWidth();
        
    }
    private void setPreferredWidth() {
        int lines = textArea.getLineCount();
        int digits = String.valueOf(lines).length();
        
        if(digits != lastDigits) {
            lastDigits = digits;
            int width = 10 + digits * 10;
            setPreferredSize(new Dimension(width, Integer.MAX_VALUE));
            revalidate();
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        FontMetrics fontm = textArea.getFontMetrics(textArea.getFont());
        int lineheight = fontm.getHeight();
        int start = textArea.getVisibleRect().y / lineheight + 1;
        int end = start + textArea.getVisibleRect().height / lineheight;
        
      for(int i = start; i <= end; i ++) {
          int y = i * lineheight- fontm.getDescent();
          g.drawString(String.valueOf(i), 5, y);
      }
    }

    @Override
    public void caretUpdate(CaretEvent e) {
       repaint();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        setPreferredWidth();
        repaint();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
       setPreferredWidth();
       repaint();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        repaint();
    }
    
}
