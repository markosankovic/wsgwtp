package com.stuntcoders.wsgwtp.client.application.home.console;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class ConsoleView extends ViewWithUiHandlers<ConsoleUiHandlers>
        implements ConsolePresenter.MyView {
    public interface Binder extends UiBinder<HTMLPanel, ConsoleView> {
    }

    @UiField
    HTMLPanel panel;

    @UiField
    TextArea textArea;

    @UiField
    Button interruptButton;

    @Inject
    ConsoleView(Binder binder) {
        initWidget(binder.createAndBindUi(this));

        textArea.setReadOnly(true);
    }

    @Override
    public void appendText(String text) {
        textArea.setText(textArea.getText().concat("\n").concat(text).trim());
    }

    @UiHandler("interruptButton")
    void interruptButtonOnClick(ClickEvent event) {
        if (getUiHandlers() != null) {
            getUiHandlers().interrupt();
        }
    }

    @Override
    public Button getInterruptButton() {
        return interruptButton;
    }

    @Override
    public void removeInterruptButton() {
        interruptButton.removeFromParent();
    }
}
