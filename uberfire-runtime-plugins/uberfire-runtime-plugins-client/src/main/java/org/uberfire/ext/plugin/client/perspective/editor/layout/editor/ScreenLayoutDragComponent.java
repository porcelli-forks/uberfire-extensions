package org.uberfire.ext.plugin.client.perspective.editor.layout.editor;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.InputSize;
import org.uberfire.ext.layout.editor.client.LayoutEditorPluginAPI;
import org.uberfire.ext.layout.editor.client.structure.EditorWidget;
import org.uberfire.ext.layout.editor.client.util.LayoutDragComponent;
import org.uberfire.ext.plugin.client.perspective.editor.layout.editor.popups.EditScreen;
import org.uberfire.ext.properties.editor.model.PropertyEditorChangeEvent;
import org.uberfire.ext.properties.editor.model.PropertyEditorFieldInfo;

@Dependent
public class ScreenLayoutDragComponent extends LayoutDragComponent {

    public static final String PLACE_NAME_PARAMETER = "Place Name";

    @Inject
    private LayoutEditorPluginAPI layoutEditorPluginAPI;

    @Override
    public String label() {
        return "Screen Component";
    }

    @Override
    public Widget getDragWidget() {
        TextBox textBox = GWT.create( TextBox.class );
        textBox.setPlaceholder( "Screen Component" );
        textBox.setReadOnly( true );
        textBox.setSize( InputSize.DEFAULT );
        return textBox;
    }

    @Override
    public IsWidget getComponentPreview() {
        return new Label( "Screen Component" );
    }

    @Override
    public boolean hasConfigureModal() {
        return true;
    }

    @Override
    public Modal getConfigureModal( EditorWidget editorWidget ) {
        return new EditScreen( editorWidget , layoutEditorPluginAPI);
    }

    public void observeEditComponentEventFromPropertyEditor( @Observes PropertyEditorChangeEvent event ) {

        PropertyEditorFieldInfo property = event.getProperty();
        if ( property.getEventId().equalsIgnoreCase( EditScreen.PROPERTY_EDITOR_KEY ) ) {
            layoutEditorPluginAPI.addPropertyToLayoutComponentByKey( property.getKey(), property.getLabel(), property.getCurrentStringValue() );
        }
    }
}
