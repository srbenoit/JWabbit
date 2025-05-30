package jwabbit.debugger;

/*
 * This software was derived from the Wabbitemu software, as it existed in October 2015, by Steve Benoit. This software
 * is licensed under the GNU General Public License version 2 (GPLv2). See the disclaimers or warranty and liability
 * included in the terms of that license.
 */

import jwabbit.gui.Gui;
import jwabbit.gui.fonts.Fonts;
import jwabbit.iface.Calc;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.Serial;
import java.util.EnumMap;
import java.util.Map;

/**
 * The toolbar for the debugger.
 */
final class DebuggerToolBar extends JToolBar implements ActionListener {

    /** Version number for serialization. */
    @Serial
    private static final long serialVersionUID = 4320552156458801444L;

    /** Map from action to item, so we can find items to enable/disable. */
    private final Map<EAction, AbstractButton> items;

    /** The popup menu. */
    private final JPopupMenu popup;

    /** The number of steps text field. */
    private final JTextField numSteps;

    /**
     * Constructs a new {@code DebuggerToolBar}.
     *
     * @param handler the handler that should receive actions generated by menu items
     */
    DebuggerToolBar(final ActionListener handler) {

        super();

        final Font font = Fonts.getSans().deriveFont(Font.PLAIN, 11.0f);
        this.items = new EnumMap<>(EAction.class);

        final BufferedImage runimg = Gui.loadImage("run.png");
        final JButton runBtn;
        if (runimg == null) {
            runBtn = new JButton("Run ");
        } else {
            runBtn = new JButton("Run ", new ImageIcon(runimg));
        }
        runBtn.setFont(font);
        runBtn.setFocusPainted(false);
        runBtn.setToolTipText("Run the calculator.");
        runBtn.setActionCommand(EAction.runCalc.getCmd());
        runBtn.addActionListener(handler);
        add(runBtn);
        this.items.put(EAction.runCalc, runBtn);

        final BufferedImage stopimg = Gui.loadImage("stop.png");
        final JButton stopBtn;
        if (stopimg == null) {
            stopBtn = new JButton("Stop ");
        } else {
            stopBtn = new JButton("Stop ", new ImageIcon(stopimg));
        }
        stopBtn.setFont(font);
        stopBtn.setFocusPainted(false);
        stopBtn.setToolTipText("Stop the calculator.");
        stopBtn.setActionCommand(EAction.stopCalc.getCmd());
        stopBtn.addActionListener(handler);
        stopBtn.setEnabled(false);
        add(stopBtn);
        this.items.put(EAction.stopCalc, stopBtn);

        final BufferedImage breakimg = Gui.loadImage("break.png");
        final JButton breakBtn;
        if (breakimg == null) {
            breakBtn = new JButton("Toggle breakpoint ");
        } else {
            breakBtn = new JButton("Toggle breakpoint ", new ImageIcon(breakimg));
        }
        breakBtn.setFont(font);
        breakBtn.setFocusPainted(false);
        breakBtn.setToolTipText("Toggle the breakpoint at the current selection.");
        breakBtn.setActionCommand(EAction.toggleExecutionBreak.getCmd());
        breakBtn.addActionListener(handler);
        add(breakBtn);
        this.items.put(EAction.toggleExecutionBreak, breakBtn);

        final BufferedImage membreakimg = Gui.loadImage("membreak.png");
        final JButton memBtn;
        if (membreakimg == null) {
            memBtn = new JButton("Toggle watchpoint ");
        } else {
            memBtn = new JButton("Toggle watchpoint ", new ImageIcon(membreakimg));
        }
        memBtn.setFont(font);
        memBtn.setFocusPainted(false);
        memBtn.setToolTipText("Toggle a memory breakpoint at the current selection.");
        memBtn.setActionCommand(EAction.toggleMemoryBreak.getCmd());
        memBtn.addActionListener(handler);
        add(memBtn);
        this.items.put(EAction.toggleMemoryBreak, memBtn);

        final BufferedImage downimg = Gui.loadImage("down.png");
        final JButton downBtn;
        if (downimg == null) {
            downBtn = new JButton();
        } else {
            downBtn = new JButton(new ImageIcon(downimg));
        }
        downBtn.setFont(font);
        downBtn.setFocusPainted(false);
        downBtn.setActionCommand("showmembreakpopup");
        downBtn.addActionListener(this);
        add(downBtn);

        this.popup = new JPopupMenu();

        final JMenuItem write = new JMenuItem("Break on write");
        write.setFont(font);
        write.setActionCommand(EAction.toggleMemoryWriteBreak.getCmd());
        write.addActionListener(handler);
        this.popup.add(write);
        this.items.put(EAction.toggleMemoryWriteBreak, write);

        final JMenuItem read = new JMenuItem("Break on read");
        read.setFont(font);
        read.setActionCommand(EAction.toggleMemoryReadBreak.getCmd());
        read.addActionListener(handler);
        this.popup.add(read);
        this.items.put(EAction.toggleMemoryReadBreak, read);

        final BufferedImage stepimg = Gui.loadImage("step.png");
        final JButton stepBtn;
        if (stepimg == null) {
            stepBtn = new JButton("Step ");
        } else {
            stepBtn = new JButton("Step ", new ImageIcon(stepimg));
        }
        stepBtn.setFont(font);
        stepBtn.setFocusPainted(false);
        stepBtn.setToolTipText("Run a single command.");
        stepBtn.setActionCommand(EAction.stepCalc.getCmd());
        stepBtn.addActionListener(handler);
        add(stepBtn);
        this.items.put(EAction.stepCalc, stepBtn);

        final BufferedImage stepnimg = Gui.loadImage("stepn.png");
        final JButton stepnBtn;
        if (stepnimg == null) {
            stepnBtn = new JButton("Step N ");
        } else {
            stepnBtn = new JButton("Step N ", new ImageIcon(stepnimg));
        }
        stepnBtn.setFont(font);
        stepnBtn.setFocusPainted(false);
        stepnBtn.setToolTipText("Run some number of commands.");
        stepnBtn.setActionCommand(EAction.stepnCalc.getCmd());
        stepnBtn.addActionListener(handler);
        add(stepnBtn);
        this.items.put(EAction.stepnCalc, stepnBtn);

        this.numSteps = new JTextField("1", 3);
        this.numSteps.setFont(font);
        this.numSteps.setMaximumSize(this.numSteps.getPreferredSize());
        this.numSteps.setMinimumSize(this.numSteps.getPreferredSize());
        add(this.numSteps);

        final BufferedImage stepoverimg = Gui.loadImage("stepover.png");
        final JButton stepoverBtn;
        if (stepoverimg == null) {
            stepoverBtn = new JButton("Step over ");
        } else {
            stepoverBtn = new JButton("Step over ", new ImageIcon(stepoverimg));
        }
        stepoverBtn.setFont(font);
        stepoverBtn.setFocusPainted(false);
        stepoverBtn.setToolTipText("Run a single line.");
        stepoverBtn.setActionCommand(EAction.stepOverCalc.getCmd());
        stepoverBtn.addActionListener(handler);
        add(stepoverBtn);
        this.items.put(EAction.stepOverCalc, stepoverBtn);

        final BufferedImage gotoimg = Gui.loadImage("goto.png");
        final JButton gotoBtn;
        if (gotoimg == null) {
            gotoBtn = new JButton("Goto ");
        } else {
            gotoBtn = new JButton("Goto ", new ImageIcon(gotoimg));
        }
        gotoBtn.setFont(font);
        gotoBtn.setFocusPainted(false);
        gotoBtn.setToolTipText("Go to an address in RAM or Flash.");
        gotoBtn.setActionCommand(EAction.gotoAddress.getCmd());
        gotoBtn.addActionListener(handler);
        add(gotoBtn);
        this.items.put(EAction.gotoAddress, gotoBtn);
    }

    /**
     * Gets the number of steps.
     *
     * @return the number of steps
     */
    int getNumSteps() {

        final String str = this.numSteps.getText();
        int count;
        try {
            count = Integer.parseInt(str);
            if (count < 0) {
                this.numSteps.setText("1");
                count = 1;
            }
        } catch (final NumberFormatException ex) {
            this.numSteps.setText("1");
            count = 1;
        }

        return count;
    }

    /**
     * Sets state of buttons.
     *
     * @param theCalc the calculator
     */
    public void setState(final Calc theCalc) {

        // Called from the AWT event thread

        this.items.get(EAction.runCalc).setEnabled(!theCalc.isRunning());
        this.items.get(EAction.stopCalc).setEnabled(theCalc.isRunning());
        this.items.get(EAction.toggleExecutionBreak).setEnabled(!theCalc.isRunning());
        this.items.get(EAction.toggleMemoryBreak).setEnabled(!theCalc.isRunning());
        this.items.get(EAction.toggleMemoryReadBreak).setEnabled(!theCalc.isRunning());
        this.items.get(EAction.toggleMemoryWriteBreak).setEnabled(!theCalc.isRunning());
        this.items.get(EAction.stepCalc).setEnabled(!theCalc.isRunning());
        this.items.get(EAction.stepnCalc).setEnabled(!theCalc.isRunning());
        this.items.get(EAction.stepOverCalc).setEnabled(!theCalc.isRunning());
        this.items.get(EAction.gotoAddress).setEnabled(!theCalc.isRunning());
    }

    /**
     * Handles selection of the down button which shows the popup menu of types of memory breakpoint available.
     *
     * @param e the action event
     */
    @Override
    public void actionPerformed(final ActionEvent e) {

        if (e.getSource() instanceof AbstractButton) {
            final Rectangle srcBounds = ((Component) e.getSource()).getBounds();
            final Dimension size = this.popup.getPreferredSize();

            this.popup.show(this, srcBounds.x + srcBounds.width - size.width, srcBounds.y + srcBounds.height);
        }
    }
}
