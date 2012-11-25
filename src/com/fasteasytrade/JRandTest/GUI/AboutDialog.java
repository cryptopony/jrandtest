/*
 * Created on 05/02/2005
 *
 * JRandTest package
 *
 * Copyright (c) 2005, Zur Aougav, aougav@hotmail.com
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list 
 * of conditions and the following disclaimer. 
 * 
 * Redistributions in binary form must reproduce the above copyright notice, this 
 * list of conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution. 
 * 
 * Neither the name of the JRandTest nor the names of its contributors may be 
 * used to endorse or promote products derived from this software without specific 
 * prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR 
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.fasteasytrade.JRandTest.GUI;

import java.awt.AWTEvent;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

/**
 * Display about modal window.
 * <p>
 * @author Zur Aougav
 */
public class AboutDialog extends Dialog {
    Button okButton;
    Label titlePage;
    VWrappingLabel description;

    public AboutDialog(Frame parent, String title, boolean modal) {
        this(parent, modal);
        setTitle(title);
    }

    public AboutDialog(Frame parent, boolean modal) {

        super(parent, modal);

        setLayout(null);
        addNotify();
        setSize(getInsets().left + getInsets().right + 417, getInsets().top + getInsets().bottom + 429);

        okButton = new Button("OK");
        okButton.setBounds(getInsets().left + 144, getInsets().top + 384, 66, 27);
        add(okButton);

        titlePage = new Label();
        titlePage.setBounds(getInsets().left + 24, getInsets().top + 24, 312, 36);
        titlePage.setFont(new Font("TimesRoman", Font.BOLD | Font.ITALIC, 24));
        add(titlePage);
        titlePage.setText("About JRandTest...");

        description = new VWrappingLabel();
        description.setVAlignStyle(250);
        description.setBounds(getInsets().left + 24, getInsets().top + 68, 360, 305);
        description.setFont(new Font("TimesRoman", Font.BOLD, 14));
        add(description);

        String s = "The JRandTest was created by Zur Aougav.";
        s += "\n\nIt was created as a part of cryptography and cryptanalysis project.";
        s += "\n\nThis project's purpose is to help students";
        s += " and professionals testing randomness of random";
        s += " sources, PRNGs, encryption algorithms.";
        s += "\n\nComments or Suggestions:  aougav@hotmail.com";
        s += "\n\nThis project exploits the power of Java.";
        s += "\n\nJRandTest developed using Eclipse from www.eclipse.org.";

        description.setBackground(Color.cyan);
        description.setText(s);

        setTitle("About JRandTest");
        setResizable(false);
    }

    @Override
    public synchronized void show() {
        Rectangle bounds = getParent().getBounds();
        Rectangle abounds = getBounds();

        setLocation(bounds.x + (bounds.width - abounds.width) / 2, bounds.y + (bounds.height - abounds.height) / 2);

        super.setVisible(true);
    }

    @Override
    public void processEvent(AWTEvent event) {
        if (event.getID() == WindowEvent.WINDOW_CLOSING) {
            setVisible(false);
            return;
        }

        if (event.getSource() == okButton && (event instanceof ActionEvent)) {
            setVisible(true);
        }
        super.processEvent(event);
    }

}
