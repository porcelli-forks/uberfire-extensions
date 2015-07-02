package org.uberfire.ext.plugin.client.perspective.editor.layout.editor;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.InputSize;
import org.uberfire.ext.layout.editor.client.LayoutEditorPluginAPI;
import org.uberfire.ext.layout.editor.client.structure.EditorWidget;
import org.uberfire.ext.layout.editor.client.util.LayoutDragComponent;
import org.uberfire.ext.plugin.client.perspective.editor.layout.editor.popups.EditHTML;

@Dependent
public class HTMLLayoutDragComponent extends LayoutDragComponent {

    public static final String HTML_CODE_PARAMETER = "HTML_CODE";

    @Inject
    private LayoutEditorPluginAPI layoutEditorPluginAPI;

    @Override
    public String label() {
        return "Html Component";
    }

    @Override
    public Widget getDragWidget() {
        TextBox textBox = GWT.create( TextBox.class );
        textBox.setPlaceholder( "HTML Component" );
        textBox.setReadOnly( true );
        textBox.setSize( InputSize.DEFAULT );
        return textBox;
    }

    @Override
    public IsWidget getComponentPreview() {
        return new Label( "HTML Component" );
    }

    @Override
    public boolean hasConfigureModal() {
        return true;
    }

    @Override
    public Modal getConfigureModal( EditorWidget editorWidget ) {
        return new EditHTML( editorWidget, layoutEditorPluginAPI );
    }
}
