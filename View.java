package com.javarush.task.task32.task3209;

import com.javarush.task.task32.task3209.listeners.FrameListener;
import com.javarush.task.task32.task3209.listeners.TabbedPaneChangeListener;
import com.javarush.task.task32.task3209.listeners.UndoListener;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {
    private Controller controller;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JTextPane htmlTextPane = new JTextPane();
    private JEditorPane plainTextPane = new JEditorPane();
    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);

    public View() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException e) {
            ExceptionHandler.log(e);
        } catch (InstantiationException e) {
            ExceptionHandler.log(e);

        } catch (UnsupportedLookAndFeelException e) {
            ExceptionHandler.log(e);

        } catch (ClassNotFoundException e) {
            ExceptionHandler.log(e);

        }

    }

    public UndoListener getUndoListener() {
        return undoListener;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String action = actionEvent.getActionCommand();

        if (action.equals("Новый")) {
            controller.createNewDocument();
        } else if (action.equals("Открыть")) {
            controller.openDocument();
        } else if (action.equals("Сохранить")) {
            controller.saveDocument();
        } else if (action.equals("Сохранить как...")) {
            controller.saveDocumentAs();
        } else if (action.equals("Выход")) {
            controller.exit();
        } else if (action.equals("О программе")) {
            this.showAbout();
        }
    }

    public void selectHtmlTab() {
        tabbedPane.setSelectedIndex(0);
        resetUndo();
    }

    public boolean isHtmlTabSelected() {

        return tabbedPane.getSelectedIndex() == 0;
    }

    public void update() {
        htmlTextPane.setDocument(controller.getDocument());
    }

    public void undo() {
        try {
            undoManager.undo();
        } catch (CannotUndoException undoExeption) {
            ExceptionHandler.log(undoExeption);
        }

    }

    public void showAbout() {
        JOptionPane.showMessageDialog(getContentPane()
                , "Это HTML-редактор Лебедева А.В.. version: 1.0 "
                , "Информация:"
                , JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void redo() {
        try {
            undoManager.redo();
        } catch (CannotRedoException undoExeption) {
            ExceptionHandler.log(undoExeption);
        }
    }

    public void resetUndo() {
        undoManager.discardAllEdits();
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void init() {

        initGui();
        setTitle("Мой HTML редактор");
        FrameListener frameListener = new FrameListener(this);
        addWindowListener(frameListener);
        setVisible(true);
    }

    public void exit() {
        controller.exit();
    }

    public void initMenuBar() {
        //Создаём панель менюхи

        JMenuBar jMenuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, jMenuBar);
        MenuHelper.initEditMenu(this, jMenuBar);
        MenuHelper.initStyleMenu(this, jMenuBar);
        MenuHelper.initAlignMenu(this, jMenuBar);
        MenuHelper.initColorMenu(this, jMenuBar);
        MenuHelper.initFontMenu(this, jMenuBar);
        MenuHelper.initHelpMenu(this, jMenuBar);
        getContentPane().add(jMenuBar, BorderLayout.NORTH);

    }

    public void initEditor() {
        htmlTextPane.setContentType("text/html");
        JScrollPane jScrollPaneHtml = new JScrollPane(htmlTextPane);
        tabbedPane.add("HTML", jScrollPaneHtml);
        JScrollPane jScrollPaneText = new JScrollPane(plainTextPane);
        tabbedPane.add("Текст", jScrollPaneText);
        tabbedPane.setPreferredSize(new Dimension(500, 250));
        TabbedPaneChangeListener tabbedPaneChangeListener = new TabbedPaneChangeListener(this);
        tabbedPane.addChangeListener(tabbedPaneChangeListener);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);


    }

    public void initGui() {
        initMenuBar();
        initEditor();
        pack();


    }

    public void selectedTabChanged() {
        if (tabbedPane.getSelectedIndex() == 0) {
            controller.setPlainText(plainTextPane.getText());
        } else if (tabbedPane.getSelectedIndex() == 1) {
            plainTextPane.setText(controller.getPlainText());
        }
        this.resetUndo();
    }


    public boolean canUndo() {
        return undoManager.canUndo();
    }

    public boolean canRedo() {
        return undoManager.canRedo();

    }
}
