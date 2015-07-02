package org.uberfire.ext.layout.editor.client.structure;

import com.google.gwt.user.client.ui.ComplexPanel;
import org.uberfire.ext.layout.editor.client.util.LayoutDragComponent;

public interface EditorWidget {

    ComplexPanel getWidget();

    void addChild( final EditorWidget editorWidget );

    void removeChild( final EditorWidget editorWidget );

    LayoutDragComponent getType();
}
