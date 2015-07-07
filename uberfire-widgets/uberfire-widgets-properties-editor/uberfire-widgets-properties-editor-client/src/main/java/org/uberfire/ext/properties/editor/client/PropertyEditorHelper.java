package org.uberfire.ext.properties.editor.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.shared.event.HiddenEvent;
import org.gwtbootstrap3.client.shared.event.HiddenHandler;
import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.client.shared.event.ShowHandler;
import org.gwtbootstrap3.client.ui.InputGroupAddon;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.Popover;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Placement;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.jboss.errai.ioc.client.container.IOC;
import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.uberfire.ext.properties.editor.client.fields.AbstractField;
import org.uberfire.ext.properties.editor.client.fields.PropertyEditorFieldType;
import org.uberfire.ext.properties.editor.client.widgets.AbstractPropertyEditorWidget;
import org.uberfire.ext.properties.editor.client.widgets.PropertyEditorItemButtons;
import org.uberfire.ext.properties.editor.client.widgets.PropertyEditorItemLabel;
import org.uberfire.ext.properties.editor.client.widgets.PropertyEditorItemWidget;
import org.uberfire.ext.properties.editor.model.CustomPropertyEditorFieldInfo;
import org.uberfire.ext.properties.editor.model.PropertyEditorCategory;
import org.uberfire.ext.properties.editor.model.PropertyEditorEvent;
import org.uberfire.ext.properties.editor.model.PropertyEditorFieldInfo;

public class PropertyEditorHelper {

    public static void extractEditorFrom( final PropertyEditorWidget propertyEditorWidget,
                                          final PanelGroup propertyMenu,
                                          final PropertyEditorEvent event,
                                          final String propertyNameFilter ) {
        propertyMenu.clear();
        for ( PropertyEditorCategory category : event.getSortedProperties() ) {
            createCategory( propertyEditorWidget, propertyMenu, category, propertyNameFilter );
        }
    }

    static void createCategory( final PropertyEditorWidget propertyEditorWidget,
                                final PanelGroup propertyMenu,
                                final PropertyEditorCategory category,
                                final String propertyNameFilter ) {

        PanelCollapse panelCollapse = createPanelCollapse( propertyEditorWidget, category );
        PanelHeader headerPanel = createPanelHeader( category, propertyMenu, panelCollapse );
        PanelBody panelBody = createPanelBody();


        boolean categoryHasActiveChilds = false;
        for ( final PropertyEditorFieldInfo field : category.getFields() ) {
            if ( isAMatchOfFilter( propertyNameFilter, field ) ) {
                categoryHasActiveChilds = true;
                panelBody.add( createItemsWidget( field,
                                                  category,
                                                  panelBody ) );
            }
        }
        if ( categoryHasActiveChilds ) {
            panelCollapse.add( panelBody );
            propertyMenu.add( headerPanel );
            propertyMenu.add( panelCollapse );
        }

    }

    static PanelHeader createPanelHeader( final PropertyEditorCategory category,
                                          final PanelGroup propertyMenu,
                                          PanelCollapse panelCollapse ) {
        final PanelHeader header = GWT.create( PanelHeader.class );
        header.setText( category.getName() );
        header.setDataToggle( Toggle.COLLAPSE );
        header.setDataParent( propertyMenu.getId() );
        header.setDataTargetWidget( panelCollapse );
        return header;
    }

    static PanelCollapse createPanelCollapse( final PropertyEditorWidget propertyEditorWidget,
                                              final PropertyEditorCategory category ) {
        final PanelCollapse collapse = GWT.create( PanelCollapse.class );
        collapse.addShowHandler( new ShowHandler() {
            @Override
            public void onShow( ShowEvent showEvent ) {
                propertyEditorWidget.setLastOpenAccordionGroupTitle( category.getName() );
            }
        } );
        collapse.addHiddenHandler( new HiddenHandler() {
            @Override
            public void onHidden( HiddenEvent hiddenEvent ) {
                hiddenEvent.stopPropagation();
            }
        } );
        if ( propertyEditorWidget.getLastOpenAccordionGroupTitle().equals( category.getName() ) ) {
            collapse.setIn( true );
        }

        return collapse;
    }

    private static PanelBody createPanelBody() {
        return GWT.create( PanelBody.class );
    }

    public static void extractEditorFrom( final PropertyEditorWidget propertyEditorWidget,
                                          final PanelGroup propertyMenu,
                                          final PropertyEditorEvent event ) {
        extractEditorFrom( propertyEditorWidget, propertyMenu, event, "" );
    }

    static PropertyEditorItemsWidget createItemsWidget( final PropertyEditorFieldInfo field,
                                                        final PropertyEditorCategory category,
                                                        final PanelBody panelBody ) {
        PropertyEditorItemsWidget items = GWT.create( PropertyEditorItemsWidget.class );

        items.add( createLabel( field ) );
        items.add( createField( field, items, category, panelBody ) );

        return items;
    }

    static PropertyEditorItemLabel createLabel( final PropertyEditorFieldInfo field ) {
        PropertyEditorItemLabel item = GWT.create( PropertyEditorItemLabel.class );
        item.setText( field.getLabel() );
        item.setFor( String.valueOf( field.hashCode() ) );
        return item;
    }

    static PropertyEditorItemWidget createField( final PropertyEditorFieldInfo field,
                                                 final PropertyEditorItemsWidget parent,
                                                 PropertyEditorCategory category,
                                                 PanelBody panelBody ) {
        PropertyEditorItemWidget itemWidget = GWT.create( PropertyEditorItemWidget.class );
        PropertyEditorFieldType editorFieldType = PropertyEditorFieldType.getFieldTypeFrom( field );

        Widget fieldWidget;
        if ( editorFieldType == PropertyEditorFieldType.CUSTOM ) {
            Class<?> widgetClass = ( (CustomPropertyEditorFieldInfo) field ).getCustomEditorClass();
            fieldWidget = getWidget( field, widgetClass );
        } else {
            fieldWidget = editorFieldType.widget( field );
        }
        createErrorHandlingInfraStructure( parent, fieldWidget );

        itemWidget.add( fieldWidget );

        if ( field.isRemovalSupported() ) {
            itemWidget.add( createRemoveAddOn( field, category, parent, panelBody ) );
        }

        if ( field.hasHelpInfo() ) {
            itemWidget.add( createHelp( field ) );
        }

        return itemWidget;
    }

    private static Widget createHelp( PropertyEditorFieldInfo field ) {
        final Popover popover = GWT.create( Popover.class );
        final InputGroupAddon button = GWT.create( InputGroupAddon.class );
        popover.setTitle( field.getHelpHeading() );
        popover.setContent( field.getHelpText() );
        popover.setPlacement( Placement.LEFT );
        button.setIcon( IconType.QUESTION );
        popover.add( button );
        return Widget.asWidgetOrNull( popover );
    }

    private static InputGroupAddon createRemoveAddOn( final PropertyEditorFieldInfo field,
                                                      final PropertyEditorCategory category,
                                                      final PropertyEditorItemsWidget parent,
                                                      final PanelBody categoryPanel ) {

        InputGroupAddon button = GWT.create( InputGroupAddon.class );
        button.setIcon( IconType.MINUS );
        button.addDomHandler( new ClickHandler() {
            public void onClick( ClickEvent event ) {
                category.getFields().remove( field );
                categoryPanel.remove( parent );
            }
        }, ClickEvent.getType() );
        return button;
    }

    private static Widget getWidget( final PropertyEditorFieldInfo property,
                                     final Class fieldType ) {
        SyncBeanManager beanManager = IOC.getBeanManager();
        IOCBeanDef iocBeanDef = beanManager.lookupBean( fieldType );
        AbstractField field = (AbstractField) iocBeanDef.getInstance();
        return field.widget( property );
    }

    static void createErrorHandlingInfraStructure( final PropertyEditorItemsWidget parent,
                                                   Widget widget ) {
        AbstractPropertyEditorWidget abstractPropertyEditorWidget = (AbstractPropertyEditorWidget) widget;
        abstractPropertyEditorWidget.setParent( parent );
    }

    public static boolean validade( PropertyEditorEvent event ) {
        if ( event == null ) {
            throw new NullEventException();
        }
        if ( event.getSortedProperties().isEmpty() ) {
            throw new NoPropertiesException();
        }

        return event != null && !event.getSortedProperties().isEmpty();
    }

    static boolean isAMatchOfFilter( String propertyNameFilter,
                                     PropertyEditorFieldInfo field ) {
        if ( propertyNameFilter.isEmpty() ) {
            return true;
        }
        return field.getLabel().toUpperCase().contains( propertyNameFilter.toUpperCase() );
    }

    public static class NullEventException extends RuntimeException {

    }

    public static class NoPropertiesException extends RuntimeException {

    }
}