package jwabbit.gui;

/*
 * This software was derived from the Wabbitemu software, as it existed in October 2015, by Steve Benoit. This software
 * is licensed under the GNU General Public License version 2 (GPLv2). See the disclaimers or warranty and liability
 * included in the terms of that license.
 */

import jwabbit.CalcBasicAction;
import jwabbit.CalcSetProfileAction;
import jwabbit.CalcThread;
import jwabbit.ECalcAction;
import jwabbit.Launcher;
import jwabbit.gui.options.OptionsDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * Handler for actions generated by menu items.
 */
class CalcActionHandler implements ActionListener {

    /** The calculator thread to which to post actions. */
    private final CalcThread thread;

    /**
     * Constructs a new {@code CalcActionHandler}.
     *
     * @param theThread the calculator thread to which to post calculator actions
     */
    CalcActionHandler(final CalcThread theThread) {

        super();

        this.thread = theThread;
    }

    /**
     * Gets the calculator thread.
     *
     * @return the thread
     */
    public final CalcThread getThread() {

        return this.thread;
    }

    /**
     * Handles action events.
     *
     * @param e the action event
     */
    @Override
    public final void actionPerformed(final ActionEvent e) {

        final EAction action = EAction.fromCommand(e.getActionCommand());

        switch (Objects.requireNonNull(action)) {

            case newCalc:
                Gui.createCalcRegisterEvents(true);
                break;

            case screenshot:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.TAKE_SCREENSHOT));
                break;

            case startRecord:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.START_RECORD));
                break;

            case endRecord:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.END_RECORD));
                break;

            case close:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.CLOSE));
                break;

            case exit:
                final int count = Launcher.getNumCalcs();
                for (int i = 0; i < count; ++i) {
                    final CalcThread theThread = Launcher.getCalcThread(i);
                    if (theThread != null) {
                        theThread.enqueueAction(new CalcBasicAction(ECalcAction.CLOSE));
                    }
                }
                break;

            case enableSound:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.ENABLE_SOUND));
                break;

            case disableSound:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.DISABLE_SOUND));
                break;

            case enableLink:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.CONNECT_VLINK));
                break;

            case getAnswer:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.GET_LAST_ANSWER));
                break;

            case copyAnswer:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.COPY_LAST_ANSWER));
                break;

            case paste:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.PASTE));
                break;

            case pause:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.STOP));
                break;

            case speed25:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.SET_SPEED_25));
                break;

            case speed50:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.SET_SPEED_50));
                break;

            case speed100:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.SET_SPEED_100));
                break;

            case speed200:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.SET_SPEED_200));
                break;

            case speed400:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.SET_SPEED_400));
                break;

            case speedMax:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.SET_SPEED_MAX));
                break;

            case profileFull:
                this.thread.enqueueAction(new CalcSetProfileAction("full"));
                break;

            case profileGraphing:
                this.thread.enqueueAction(new CalcSetProfileAction("graphing"));
                break;

            case profileScientific:
                this.thread.enqueueAction(new CalcSetProfileAction("scientific"));
                break;

            case profileBasic:
                this.thread.enqueueAction(new CalcSetProfileAction("basic"));
                break;

            case options:
                OptionsDialog.show();
                break;

            case reset:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.RESET));
                break;

            case debug:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.DEBUG));
                break;

            case turnOn:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.TURN_ON));
                break;

            case runWizard:
                this.thread.enqueueAction(new CalcBasicAction(ECalcAction.RUN_WIZARD));
                break;

            case about:
                final CalcUI ui = Launcher.getCalcUI(this.thread.getSlot());
                if (ui != null) {
                    new AboutDialog(ui.getMainFrame()).setVisible(true);
                }
                break;

            case open:
            case save:
            case enableSkin:
            case viewVariables:
            case viewKeypressHistory:
            case viewDetachedLCD:
            case checkUpdates:
            case showWhatsNew:
            case reportBug:
            case openWebSite:
            case recordGif:
            default:
                break;
        }
    }
}
