//package org.uberfire.ext.properties.editor.client.widgets;
//
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.dom.client.Style;
//import com.google.gwt.uibinder.client.UiBinder;
//import com.google.gwt.uibinder.client.UiField;
//import com.google.gwt.user.client.ui.Composite;
//import com.google.gwt.user.client.ui.Widget;
//import org.gwtbootstrap3.client.ui.html.Paragraph;
//
//public class PropertyEditorErrorWidget extends Composite {
//
//    @UiField
//    Paragraph label;
//
//    public PropertyEditorErrorWidget() {
//        initWidget( uiBinder.createAndBindUi( this ) );
//        getElement().getStyle().setDisplay( Style.Display.NONE );
//    }
//
//    public void setText( String text ) {
//        label.setText( text );
//    }
//
//    interface MyUiBinder extends UiBinder<Widget, PropertyEditorErrorWidget> {
//
//    }
//
//    private static MyUiBinder uiBinder = GWT.create( MyUiBinder.class );
//
//}