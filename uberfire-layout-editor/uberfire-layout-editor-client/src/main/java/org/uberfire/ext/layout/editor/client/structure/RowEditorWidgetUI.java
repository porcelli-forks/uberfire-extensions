package org.uberfire.ext.layout.editor.client.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.user.client.ui.ComplexPanel;
import org.uberfire.ext.layout.editor.client.util.LayoutDragComponent;

public class RowEditorWidgetUI implements EditorWidget {

    private final EditorWidget parent;
    private final ComplexPanel container;
    private List<String> rowSpans = new ArrayList<String>();

    private List<EditorWidget> columnEditors = new ArrayList<EditorWidget>();

    public RowEditorWidgetUI( EditorWidget parent,
                              ComplexPanel container,
                              String rowSpamString ) {
        this.parent = parent;
        this.container = container;
        parseRowSpanString( rowSpamString );
        parent.addChild( this );
    }

    public RowEditorWidgetUI( EditorWidget parent,
                              ComplexPanel container,
                              List<String> rowSpans ) {
        this.parent = parent;
        this.container = container;
        this.rowSpans = rowSpans;
        parent.addChild( this );
    }

    public ComplexPanel getWidget() {
        return container;
    }

    public List<String> getRowSpans() {
        return rowSpans;
    }

    private void parseRowSpanString( String rowSpamString ) {
        String[] spans = rowSpamString.split( " " );
        Collections.addAll( rowSpans, spans );
    }

    public void addChild( EditorWidget columnEditor ) {
        columnEditors.add( columnEditor );
    }

    public void removeFromParent() {
        parent.removeChild( this );

    }

    @Override
    public void removeChild( EditorWidget editorWidget ) {
        columnEditors.remove( editorWidget );
    }

    public List<EditorWidget> getColumnEditors() {
        return columnEditors;
    }

    @Override
    public LayoutDragComponent getType() {
        return null;
    }

}
