package org.uberfire.ext.properties.editor.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

public class PropertyEditorItemRemovalButton extends Composite implements HasClickHandlers {

    @UiField
    Button button;

    public PropertyEditorItemRemovalButton() {
        initWidget( uiBinder.createAndBindUi( this ) );
        button.setType( ButtonType.DANGER );
        button.setText( "Delete" );
    }

    @Override
    public HandlerRegistration addClickHandler( ClickHandler handler ) {
        return button.addClickHandler( handler );
    }

    interface MyUiBinder extends UiBinder<Widget, PropertyEditorItemRemovalButton> {

    }

    private static MyUiBinder uiBinder = GWT.create( MyUiBinder.class );

}