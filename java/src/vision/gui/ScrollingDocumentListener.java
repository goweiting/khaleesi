package vision.gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;

// ScrollingDocumentListener takes care of re-scrolling when appropriate
class ScrollingDocumentListener implements DocumentListener {
    private JScrollPane jScrollPane;
    private JTextArea jTextArea;

    // RK: h4x
    public ScrollingDocumentListener(JScrollPane scrollPane, JTextArea textArea) {
        jScrollPane = scrollPane;
        jTextArea = textArea;
    }

    public void changedUpdate(DocumentEvent e) {
        maybeScrollToBottom();
    }

    public void insertUpdate(DocumentEvent e) {
        maybeScrollToBottom();
    }

    public void removeUpdate(DocumentEvent e) {
        maybeScrollToBottom();
    }

    private void maybeScrollToBottom() {
        JScrollBar scrollBar = jScrollPane.getVerticalScrollBar();
        boolean scrollBarAtBottom = isScrollBarFullyExtended(scrollBar);
        boolean scrollLock = Toolkit.getDefaultToolkit()
                .getLockingKeyState(KeyEvent.VK_SCROLL_LOCK);
        if (scrollBarAtBottom && !scrollLock) {
            // Push the call to "scrollToBottom" back TWO PLACES on the
            // AWT-EDT queue so that it runs *after* Swing has had an
            // opportunity to "react" to the appending of new text:
            // this ensures that we "scrollToBottom" only after a new
            // bottom has been recalculated during the natural
            // revalidation of the GUI that occurs after having
            // appending new text to the JTextArea.
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            scrollToBottom(jTextArea);
                        }
                    });
                }
            });
        }
    }

    boolean isScrollBarFullyExtended(JScrollBar vScrollBar) {
        BoundedRangeModel model = vScrollBar.getModel();
        return (model.getExtent() + model.getValue()) == model.getMaximum();
    }

    void scrollToBottom(JComponent component) {
        Rectangle visibleRect = component.getVisibleRect();
        visibleRect.y = component.getHeight() - visibleRect.height;
        component.scrollRectToVisible(visibleRect);
    }
}