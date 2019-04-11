package com.javarush.task.task32.task3209.listeners;

import com.javarush.task.task32.task3209.View;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FrameListener extends WindowAdapter {
    public View view;

    @Override
    public void windowClosing(WindowEvent e) {
        view.exit();
    }

    public FrameListener(View view) {
        this.view = view;
    }
}
