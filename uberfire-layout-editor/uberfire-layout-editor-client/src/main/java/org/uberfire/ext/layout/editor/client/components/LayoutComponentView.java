package org.uberfire.ext.layout.editor.client.components;

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
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.Row;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ColumnSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.jboss.errai.ioc.client.container.IOC;
import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.uberfire.ext.layout.editor.api.editor.LayoutComponent;
import org.uberfire.ext.layout.editor.client.dnd.DropColumnPanel;
import org.uberfire.ext.layout.editor.client.structure.ColumnEditorUI;
import org.uberfire.ext.layout.editor.client.structure.EditorWidget;
import org.uberfire.ext.layout.editor.client.structure.LayoutComponentWidgetUI;
import org.uberfire.ext.layout.editor.client.structure.LayoutEditorUI;
import org.uberfire.ext.layout.editor.client.util.LayoutDragComponent;

public class LayoutComponentView extends Composite {

    private LayoutDragComponent type;
    private LayoutComponentWidgetUI layoutComponentWidgetUI;

    @UiField
    Container fluidContainer;

    private EditorWidget parent;

    interface ScreenEditorMainViewBinder
            extends
            UiBinder<Widget, LayoutComponentView> {

    }

    private static ScreenEditorMainViewBinder uiBinder = GWT.create( ScreenEditorMainViewBinder.class );

    public LayoutComponentView( final ColumnEditorUI parent,
                                final LayoutDragComponent type,
                                final boolean newComponent ) {
        initWidget( uiBinder.createAndBindUi( this ) );
        this.type = type;
        this.parent = parent;
        this.layoutComponentWidgetUI = new LayoutComponentWidgetUI( parent, fluidContainer, type );
        build();
        if ( newComponent && type.hasConfigureModal() ) {
            showConfigureModal();
        }
    }

    public LayoutComponentView( final ColumnEditorUI parent,
                                final LayoutComponent editor,
                                final LayoutDragComponent type ) {
        initWidget( uiBinder.createAndBindUi( this ) );
        this.parent = parent;
        this.type = type;
        this.layoutComponentWidgetUI = new LayoutComponentWidgetUI( parent, fluidContainer, type );
        loadLayoutComponentsProperties( editor );
        build();
    }

    private void loadLayoutComponentsProperties( final LayoutComponent editor ) {
        LayoutEditorUI layoutEditor = getLayoutEditor();
        layoutEditor.loadComponentProperties( this.layoutComponentWidgetUI, editor );
    }

    private void build() {
        layoutComponentWidgetUI.getWidget().clear();
        layoutComponentWidgetUI.getWidget().add( generateMainRow() );
    }

    private Row generateMainRow() {
        Row row = new Row();
        row.add( generateLayoutComponentPreview() );
        row.add( generateButtonColumn() );
        return row;
    }

    private Column generateLayoutComponentPreview() {
        Column buttonColumn = new Column( ColumnSize.LG_8 );
        buttonColumn.getElement().getStyle().setProperty( "textAlign", "left" );
        buttonColumn.add( type.getComponentPreview() );
        return buttonColumn;
    }

    private Column generateButtonColumn() {
        Column buttonColumn = new Column( ColumnSize.LG_4 );
        buttonColumn.getElement().getStyle().setProperty( "textAlign", "right" );
        if ( type.hasConfigureModal() ) {
            buttonColumn.add( generateConfigureButton() );
        }
        buttonColumn.add( generateRemoveButton() );
        return buttonColumn;
    }

    private Button generateConfigureButton() {
        Button remove = GWT.create( Button.class );
        remove.setSize( ButtonSize.EXTRA_SMALL );
        remove.setType( ButtonType.PRIMARY );
        remove.setIcon( IconType.EDIT );
        remove.getElement().getStyle().setProperty( "marginRight", "3px" );
        remove.addClickHandler( new ClickHandler() {
            public void onClick( ClickEvent event ) {
                showConfigureModal();
            }
        } );
        return remove;
    }

    private void showConfigureModal() {
        final Modal configureModal = type.getConfigureModal( layoutComponentWidgetUI );
        configureModal.show();
    }

    private Button generateRemoveButton() {
        Button remove = GWT.create( Button.class );
        remove.setSize( ButtonSize.EXTRA_SMALL );
        remove.setType( ButtonType.DANGER );
        remove.setIcon( IconType.REMOVE );
        remove.getElement().getStyle().setProperty( "marginRight", "3px" );
        remove.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent event ) {
                removeThisWidgetFromParent();
                addDropColumnPanel();
            }
        } );
        return remove;
    }

    private void removeThisWidgetFromParent() {
        parent.getWidget().remove( this );
        layoutComponentWidgetUI.removeFromParent();
    }

    private void addDropColumnPanel() {
        ColumnEditorUI columnEditorUIParent = (ColumnEditorUI) parent;
        columnEditorUIParent.getWidget().add( new DropColumnPanel( columnEditorUIParent ) );
    }

    protected LayoutEditorUI getLayoutEditor() {
        SyncBeanManager beanManager = IOC.getBeanManager();
        IOCBeanDef<LayoutEditorUI> layoutEditorUIIOCBeanDef = beanManager.lookupBean( LayoutEditorUI.class );
        return layoutEditorUIIOCBeanDef.getInstance();
    }

}
