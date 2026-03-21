/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.adonis.Nukepad;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 *
 * @author croco
 */
class Nukepad extends JFrame implements ActionListener{

    public static Nukepad getInstance() {
        return instance;
    }
    private JTabbedPane tabs;
    RSyntaxTextArea text;
    JFrame frame;
    private File currentFile;
    
    Nukepad(File projectRoot) {
        frame = new JFrame("Editor");
        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/nukepadlogo.png"));
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try{
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            
            MetalLookAndFeel.setCurrentTheme(new OceanTheme());
        }catch(Exception e) {
            e.printStackTrace();
        }
        text = new RSyntaxTextArea();
        text.setCodeFoldingEnabled(true);
        text.setAntiAliasingEnabled(true);
        CompletionProvider provider = createCompletionProvider();
        AutoCompletion ac = new AutoCompletion(provider);
        ac.install(text);
        JMenuBar menb = new JMenuBar();
        JMenu men1 = new JMenu("File");
        
        JMenuItem menit1 = new JMenuItem("New");
        JMenuItem menit2 = new JMenuItem("Open");
        JMenuItem menit3 = new JMenuItem("Save");
        JMenuItem menit4 = new JMenuItem("Print");
        JMenuItem menit5 = new JMenuItem("Quit");
        
        menit1.addActionListener(this);
        menit2.addActionListener(this);
        menit3.addActionListener(this);
        menit3.addActionListener(this);
        menit4.addActionListener(this);
        menit5.addActionListener(this);
        
        men1.add(menit1);
        men1.add(menit2);
        men1.add(menit3);
        men1.add(menit4);
        men1.add(menit5);
        
        JMenu men2 = new JMenu("Edit");
        JMenuItem menit6 = new JMenuItem("Cut");
        JMenuItem menit7 = new JMenuItem("Copy");
        JMenuItem menit8 = new JMenuItem("Paste");
        
        menit6.addActionListener(this);
        menit7.addActionListener(this);
        menit8.addActionListener(this);
        
        JButton button1 = new JButton("Compile");
        button1.addActionListener(this);
        
        JButton button2 = new JButton("Run");
        button2.addActionListener(this);
        
        JButton button3 = new JButton("Author's signature");
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    java.awt.Desktop.getDesktop().browse(URI.create("https://github.com/alexandru-andoni"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        men2.add(menit6);
        men2.add(menit7);
        men2.add(menit8);
        
        menb.add(men1);
        menb.add(men2);
        menb.add(button1);
        menb.add(button2);
        menb.add(button3);
        
        frame.setJMenuBar(menb);
        RTextScrollPane scroll = new RTextScrollPane(text);
        scroll.setRowHeaderView(new LineNumberPanel(text));
        tabs = new JTabbedPane();
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabs.addTab("Untitled", scroll);
        File rootDir = new File(System.getProperty("user.home"));
        JTree tree = new FileTree(rootDir);
        tree.setCellRenderer(new FileTreeCellRenderer());
        JScrollPane treeScroll = new JScrollPane(tree);
        JPanel categoriesPanel = buildCategoriesPanel();
        JScrollPane categoriesScroll = new JScrollPane(categoriesPanel);
        
        SearchPanel searchPanel = new SearchPanel(new File("C:\\Users"));
        searchPanel.setPreferredSize(new Dimension(280, 0));
        searchPanel.setMinimumSize(new Dimension(100, 100));
        JTabbedPane leftTabs = new JTabbedPane();
        leftTabs.addTab("Files", treeScroll);
        leftTabs.addTab("Search", searchPanel);
        leftTabs.addTab("Categories", categoriesScroll);
        leftTabs.setPreferredSize(new Dimension(280, 0));
        
        JSplitPane mainSplit = new JSplitPane (
            JSplitPane.HORIZONTAL_SPLIT,
                leftTabs,
                tabs
        );
        mainSplit.setDividerLocation(280);
        
        frame.add(mainSplit, BorderLayout.CENTER);
        instance = this;
        frame.setSize(1280, 720);
        frame.setVisible(true);
        
    }
    private static Nukepad instance;
   
    

    @Override
    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        switch(s) {
           case"Cut":
               text.cut();
               break;
           case"Copy":
               text.copy();
               break;
           case"Paste":
               text.paste();
               break;
           case"Save":
               JFileChooser jSave = new JFileChooser("f:");
               int rSave = jSave.showSaveDialog(null);
               
               if(rSave == JFileChooser.APPROVE_OPTION) {
                   File file = new File(jSave.getSelectedFile().getAbsolutePath());
                   try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                       writer.write(text.getText());
                   } catch (Exception evt) {
                       JOptionPane.showMessageDialog(frame, evt.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                   }
               } else {
                   JOptionPane.showMessageDialog(frame, "The user has cancelled the operation!");
               }
               break;
           case "Print":
               try {
                   text.print();
               } catch(Exception evt) {
                   JOptionPane.showMessageDialog(frame, evt.getMessage());
               }
               break;
           case "Open":
               JFileChooser jOpen = new JFileChooser("f:");
               int rOpen = jOpen.showOpenDialog(null);
               if (rOpen == JFileChooser.APPROVE_OPTION) {
                   File file = new File(jOpen.getSelectedFile().getAbsolutePath());
                   try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
                       StringBuilder string = new StringBuilder();
                       String line;
                       while((line = reader.readLine()) != null){
                           string.append(line).append("\n");
                       }
                       text.setText(string.toString());
                   } catch (Exception evt) {
                       JOptionPane.showMessageDialog(frame, evt.getMessage());
                   }
               } else {
                   JOptionPane.showMessageDialog(frame, "The user has cancelled the operation!");
               }
               break;
           case"New":
               text.setText("");
               break;
           case"Quit":
               frame.setVisible(false);
               break;
           case"Compile":
               try {
                   Pattern pat = Pattern.compile("public\\s+class\\s+(\\w+)");
                   Matcher mat = pat.matcher(text.getText());
                   if(mat.find()) {
                       String className = mat.group(1);
                   }
                   
                   File file = new File(getClass() + ".java");
                   try(FileWriter writer = new FileWriter(file)) {
                       writer.write(text.getText());
                   }
                   JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                   if(compiler == null) {
                       JOptionPane.showMessageDialog(frame, "No java compiler fould! Run this program using a JDK, not a JRE!");
                       return;
                   }
                   int result = compiler.run(null, null, null, file.getPath());
                   if(result == 0) {
                       JOptionPane.showMessageDialog(frame, "Compiler succesfully executed!");
                   } else {
                       JOptionPane.showMessageDialog(frame, "Compiler failed!");
                   }
               } catch(Exception evt) {
                   JOptionPane.showMessageDialog(frame, evt.getMessage());
               }
               break;
               
           case "Run":
               try {
                Process process = Runtime.getRuntime().exec("java"+ getClass());
                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));

                StringBuilder output = new StringBuilder();
                String line;

                while ((line = input.readLine()) != null) output.append(line).append("\n");
                while ((line = error.readLine()) != null) output.append(line).append("\n");

                JOptionPane.showMessageDialog(frame, output.toString());

            } catch (Exception evt) {
                JOptionPane.showMessageDialog(frame, evt.getMessage());
            }
            break;
           
           default:
               System.out.println("Unknown command:" + s);
        }
    }
    public static void main(String[] args) {
        new IntroScreen();
    }
    public void openWebPage(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch(java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public void setEditorText(String textis) {
        text.setText(textis);
    }
    public File getCurrentFile() {
        return currentFile;
    }

    void setCurrentFile(File file) {
        this.currentFile = file;
    }

    private CompletionProvider createCompletionProvider() {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        
        provider.addCompletion(new BasicCompletion(provider, "class"));
        provider.addCompletion(new BasicCompletion(provider, "public"));
        provider.addCompletion(new BasicCompletion(provider, "static"));
        provider.addCompletion(new BasicCompletion(provider, "void"));
        provider.addCompletion(new BasicCompletion(provider, "int"));
        provider.addCompletion(new BasicCompletion(provider, "String"));

        return provider;
    }
    public void openFileInNewTab(File file, String content) {
        RSyntaxTextArea editor = new RSyntaxTextArea();
        editor.setCodeFoldingEnabled(true);
        editor.setAntiAliasingEnabled(true);
        String name= file.getName().toLowerCase();
        switch(name.substring(name.lastIndexOf('.') +1)) {
            case"java":
                editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
                break;
            case"xml":
                editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
                break;
            case"html":
                editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
                break;
            case"js":
                editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
                break;
            case"py":
                editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_PYTHON);
                break;
            case"cpp":
                editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
                break;
            case"cs":
                editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CSHARP);
                break;
            case"c":
                editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_C);
                break;
            default:
                editor.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
                break;
        }
        editor.setText(content);
        
        RTextScrollPane scroll = new RTextScrollPane(editor);
        scroll.setRowHeaderView(new LineNumberPanel(editor));
        
        tabs.addTab(file.getName(), scroll);
        tabs.setSelectedComponent(scroll);
        
        this.text = editor;
        this.currentFile = file;
        
        makeTabClosable(tabs, scroll, file.getName());
    }

   private void makeTabClosable(JTabbedPane tabs, Component tab, String title) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setOpaque(false);

    JLabel label = new JLabel(title);
    JButton close = new JButton("✕");
    close.setBorder(null);
    close.setFocusable(false);
    close.setContentAreaFilled(false);

    close.addActionListener(e -> {
        int index = tabs.indexOfComponent(tab);
        if (index != -1) {
            tabs.remove(index);
        }
    });

    panel.add(label, BorderLayout.WEST);
    panel.add(close, BorderLayout.EAST);

    tabs.setTabComponentAt(tabs.indexOfComponent(tab), panel);
    }

    private JPanel buildCategoriesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        Map<String, DefaultListModel<String>> categories = new LinkedHashMap<>();
        
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        JButton addCat = new JButton("Add Category (+)");
        JButton removeCat = new JButton("Remove Category (-)");
        toolbar.add(addCat);
        toolbar.add(removeCat);
        panel.add(toolbar);
        JLabel[] selected = {null};
        
        addCat.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(panel, "Category Name:");
            if (name == null || name.isBlank()) return;
            
            DefaultListModel<String> model = new DefaultListModel<>();
            categories.put(name, model);
            
            JPanel section = buildCategorySection(name, model, categories, panel);
            panel.add(section);
            panel.revalidate();
        });
        removeCat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] names = categories.keySet().toArray(new String[0]);
                String choice = (String) JOptionPane.showInputDialog(
                        panel, "Which category to remove?", "Remove",
                        JOptionPane.PLAIN_MESSAGE, null, names,
                        names.length >0?names[0] : null);
                if(choice == null) return;
                categories.remove(choice);
                panel.removeAll();
                panel.add(toolbar);
                categories.forEach((n, m) ->
                        panel.add(buildCategorySection(n, m, categories, panel)));
                panel.revalidate();
                panel.repaint();
            }
        });
        return panel;
    }

    private JPanel buildCategorySection(
            String name,
            DefaultListModel<String> model, 
            Map<String, DefaultListModel<String>> allCategories, 
            JPanel parent) {
        JPanel section = new JPanel(new BorderLayout());
        section.setBorder(BorderFactory.createTitledBorder(name));
        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        section.add(new JScrollPane(list), BorderLayout.CENTER);
        
        JPopupMenu popup = new JPopupMenu();
        JMenuItem addFile = new JMenuItem("Add file...");
        JMenuItem addFolder = new JMenuItem("Add folder...");
        JMenuItem removeItem = new JMenuItem("Remove selected");
        
        addFile.addActionListener( e -> {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setMultiSelectionEnabled(true);
            if (fc.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
                for(File f : fc.getSelectedFiles())
                    model.addElement(f.getAbsolutePath());
            
        });
        popup.add(addFile);
        popup.add(addFolder);
        popup.addSeparator();
        popup.add(removeItem);
        
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                maybeShow(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShow(e);
            }
            private void maybeShow(MouseEvent e){
                if(e.isPopupTrigger())
                    popup.show(list, e.getX(), e.getY());
            }
        });
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    String path = list.getSelectedValue();
                    if(path == null) return;
                    String content;
                    try {
                        content = new String(Files.readAllBytes(Paths.get(path)));
                        JTextArea textArea = new JTextArea(content);
                    JScrollPane scroll = new JScrollPane(textArea);
                    tabs.addTab(new File(path).getName(), scroll);
                    tabs.setSelectedIndex(tabs.getTabCount() - 1);
                    } catch (IOException ex) {
                        System.getLogger(Nukepad.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                    }
                    
                    }
                        
                }
            });
        
     return section;  
    }
   
    
}
