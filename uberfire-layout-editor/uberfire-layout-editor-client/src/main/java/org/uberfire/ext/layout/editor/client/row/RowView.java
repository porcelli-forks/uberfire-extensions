package org.uberfire.ext.layout.editor.client.row;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Column;
import org.gwtbootstrap3.client.ui.Container;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.uberfire.ext.layout.editor.api.editor.ColumnEditor;
import org.uberfire.ext.layout.editor.api.editor.LayoutComponent;
import org.uberfire.ext.layout.editor.api.editor.RowEditor;
import org.uberfire.ext.layout.editor.client.components.LayoutComponentView;
import org.uberfire.ext.layout.editor.client.dnd.DropColumnPanel;
import org.uberfire.ext.layout.editor.client.structure.ColumnEditorUI;
import org.uberfire.ext.layout.editor.client.structure.EditorWidget;
import org.uberfire.ext.layout.editor.client.structure.LayoutEditorUI;
import org.uberfire.ext.layout.editor.client.structure.RowEditorWidgetUI;
import org.uberfire.ext.layout.editor.client.util.DragTypeBeanResolver;
import org.uberfire.ext.layout.editor.client.util.LayoutDragComponent;

public class RowView extends Composite {

    private DropColumnPanel oldDropColumnPanel;
    private RowEditorWidgetUI row;

    @UiField
    Container fluidContainer;

    private EditorWidget editorWidget;

    interface ScreenEditorMainViewBinder
            extends
            UiBinder<Widget, RowView> {

    }

    private static ScreenEditorMainViewBinder uiBinder = GWT.create( ScreenEditorMainViewBinder.class );

    public RowView( LayoutEditorUI parent,
                    String rowSpamString ) {
        initWidget( uiBinder.createAndBindUi( this ) );
        this.editorWidget = parent;
        this.row = new RowEditorWidgetUI( parent, fluidContainer, rowSpamString );
        build();
    }

    public RowView( ColumnEditorUI parent,
                    String rowSpamString,
                    DropColumnPanel oldDropColumnPanel ) {
        initWidget( uiBinder.createAndBindUi( this ) );
        this.editorWidget = parent;
        this.oldDropColumnPanel = oldDropColumnPanel;
        this.row = new RowEditorWidgetUI( parent, fluidContainer, rowSpamString );
        build();

    }

    private RowView( ColumnEditorUI parent,
                     List<String> rowSpans,
                     DropColumnPanel oldDropColumnPanel,
                     RowEditor rowEditor ) {
        initWidget( uiBinder.createAndBindUi( this ) );
        this.editorWidget = parent;
        this.oldDropColumnPanel = oldDropColumnPanel;
        this.row = new RowEditorWidgetUI( parent, fluidContainer, rowSpans );
        reload( rowEditor.getColumnEditors() );
    }

    public RowView( LayoutEditorUI parent,
                    RowEditor rowEditor ) {
        initWidget( uiBinder.createAndBindUi( this ) );
        this.editorWidget = parent;
        this.row = new RowEditorWidgetUI( parent, fluidContainer, rowEditor.getRowSpam() );
        reload( rowEditor.getColumnEditors() );
    }

    private Row generateColumns( List<ColumnEditor> columnEditors ) {
        Row rowWidget = new Row();
        rowWidget.getElement().getStyle().setProperty( "marginBottom", "15px" );

        for ( ColumnEditor columnEditor : columnEditors ) {

            Column column = createColumn( columnEditor );
            ColumnEditorUI parent = new ColumnEditorUI( row, column, columnEditor.getSpan() );

            // Create the drop panel always, but don't add it to the column in case we're reloading an existing layout, and the column already contains elements
            DropColumnPanel dropColumnPanel = generateDropColumnPanel( column, parent, !columnEditor.hasElements() );

            for ( RowEditor editor : columnEditor.getRows() ) {
                column.add( createRowView( parent, dropColumnPanel, editor ) );
            }

            for ( LayoutComponent layoutComponent : columnEditor.getLayoutComponents() ) {
                final LayoutDragComponent layoutDragComponent = getLayoutDragComponent( layoutComponent );
                column.add( createLayoutComponentView( parent, layoutComponent, layoutDragComponent ) );
            }

            rowWidget.add( column );
        }
        return rowWidget;
    }

    protected RowView createRowView( ColumnEditorUI parent,
                                     DropColumnPanel dropColumnPanel,
                                     RowEditor editor ) {
        return new RowView( parent, editor.getRowSpam(), dropColumnPanel, editor );
    }

    protected LayoutComponentView createLayoutComponentView( ColumnEditorUI parent,
                                                             LayoutComponent layoutComponent,
                                                             LayoutDragComponent layoutDragComponent ) {
        return new LayoutComponentView( parent, layoutComponent, layoutDragComponent );
    }

    protected LayoutDragComponent getLayoutDragComponent( LayoutComponent layoutComponent ) {
        return new DragTypeBeanResolver().lookupDragTypeBean( layoutComponent.getDragTypeName() );
    }

    private void reload( List<ColumnEditor> columnEditors ) {
        row.getWidget().add( generateHeaderRow() );
        row.getWidget().add( generateColumns( columnEditors ) );
    }

    private Row generateColumns() {
        Row rowWidget = new Row();
        rowWidget.getElement().getStyle().setProperty( "marginBottom", "15px" );
        for ( String span : row.getRowSpans() ) {
            Column column = createColumn( span );
            rowWidget.add( column );
        }
        return rowWidget;
    }

    private void build() {
        row.getWidget().add( generateHeaderRow() );
        row.getWidget().add( generateColumns() );
    }

    private Column createColumn( ColumnEditor columnEditor ) {
        Column column = new Column( buildColumnSize( Integer.valueOf( columnEditor.getSpan() ) ) );
        column.add( generateLabel( "Column" ) );
        setCSS( column );
        return column;
    }

    private DropColumnPanel generateDropColumnPanel( Column column,
                                                     ColumnEditorUI parent,
                                                     boolean addToColumn ) {
        final DropColumnPanel drop = new DropColumnPanel( parent );
        if ( addToColumn ) {
            column.add( drop );
        }
        return drop;
    }

    private Column createColumn( String span ) {
        Column column = new Column( buildColumnSize( Integer.valueOf( span ) ) );
        column.add( generateLabel( "Column" ) );
        ColumnEditorUI columnEditor = new ColumnEditorUI( row, column, span );
        column.add( new DropColumnPanel( columnEditor ) );
        setCSS( column );
        return column;
    }

    private void setCSS( Column column ) {
        column.getElement().getStyle().setProperty( "border", "1px solid #DDDDDD" );
        column.getElement().getStyle().setProperty( "backgroundColor", "White" );
    }

    private Row generateHeaderRow() {
        Row row = new Row();
        row.add( generateRowLabelColumn() );
        row.add( generateButtonColumn() );
        return row;
    }

    private Column generateRowLabelColumn() {
        Column column = new Column( buildColumnSize( 6 ) );
        Label row1 = generateLabel( "Row" );
        column.add( row1 );
        return column;
    }

    private Column generateButtonColumn() {
        Column buttonColumn = new Column( buildColumnSize( 6 ) );
        buttonColumn.getElement().getStyle().setProperty( "textAlign", "right" );
        Button remove = generateButton();
        buttonColumn.add( remove );
        return buttonColumn;
    }

    private Button generateButton() {
        Button remove = GWT.create( Button.class );
        remove.setText( "Remove" );
        remove.setSize( ButtonSize.EXTRA_SMALL );
        remove.setType( ButtonType.DANGER );
        remove.setIcon( IconType.REMOVE );
        remove.getElement().getStyle().setProperty( "marginRight", "3px" );
        remove.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent event ) {
                editorWidget.getWidget().remove( RowView.this );
                if ( parentIsAColumn() ) {
                    attachDropColumnPanel();
                }
                row.removeFromParent();
            }
        } );
        return remove;
    }

    private void attachDropColumnPanel() {
        editorWidget.getWidget().add( oldDropColumnPanel );
    }

    private boolean parentIsAColumn() {
        return oldDropColumnPanel != null;
    }

    private Label generateLabel( String row ) {
        Label label = GWT.create( Label.class );
        label.setText( row );
        label.getElement().getStyle().setProperty( "marginLeft", "3px" );
        return label;
    }

    public static ColumnSize buildColumnSize( final int value ) {
        switch ( value ) {
            case 1:
                return ColumnSize.MD_1;
            case 2:
                return ColumnSize.MD_2;
            case 3:
                return ColumnSize.MD_3;
            case 4:
                return ColumnSize.MD_4;
            case 5:
                return ColumnSize.MD_5;
            case 6:
                return ColumnSize.MD_6;
            case 7:
                return ColumnSize.MD_7;
            case 8:
                return ColumnSize.MD_8;
            case 9:
                return ColumnSize.MD_9;
            case 10:
                return ColumnSize.MD_10;
            case 11:
                return ColumnSize.MD_11;
            case 12:
                return ColumnSize.MD_12;
            default:
                return ColumnSize.MD_12;
        }
    }

}
