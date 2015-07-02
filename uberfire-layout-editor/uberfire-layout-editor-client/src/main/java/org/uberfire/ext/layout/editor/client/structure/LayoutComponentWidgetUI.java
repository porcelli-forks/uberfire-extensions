package org.uberfire.ext.layout.editor.client.structure;

import com.google.gwt.user.client.ui.ComplexPanel;
import org.uberfire.ext.layout.editor.client.util.LayoutDragComponent;

public class LayoutComponentWidgetUI implements EditorWidget {

    private final EditorWidget parent;
    private final ComplexPanel container;
    private final LayoutDragComponent type;

    public LayoutComponentWidgetUI( final EditorWidget parent,
                                    final ComplexPanel container,
                                    final LayoutDragComponent type ) {
        this.parent = parent;
        this.container = container;
        this.type = type;
        parent.addChild( this );
    }

    public ComplexPanel getWidget() {
        return container;
    }

    public void removeFromParent() {
        parent.removeChild( this );
    }

    @Override
    public void addChild( final EditorWidget editorWidget ) {
    }

    @Override
    public void removeChild( final EditorWidget editorWidget ) {

    }

    @Override
    public LayoutDragComponent getType() {
        return type;
    }
}
