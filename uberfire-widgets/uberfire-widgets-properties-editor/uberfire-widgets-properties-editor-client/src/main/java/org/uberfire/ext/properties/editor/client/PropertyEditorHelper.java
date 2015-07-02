package org.uberfire.ext.properties.editor.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.shared.event.HiddenEvent;
import org.gwtbootstrap3.client.shared.event.HiddenHandler;
import org.gwtbootstrap3.client.shared.event.ShowEvent;
import org.gwtbootstrap3.client.shared.event.ShowHandler;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.jboss.errai.ioc.client.container.IOC;
import org.jboss.errai.ioc.client.container.IOCBeanDef;
import org.jboss.errai.ioc.client.container.SyncBeanManager;
import org.uberfire.ext.properties.editor.client.fields.AbstractField;
import org.uberfire.ext.properties.editor.client.fields.PropertyEditorFieldType;
import org.uberfire.ext.properties.editor.client.widgets.AbstractPropertyEditorWidget;
import org.uberfire.ext.properties.editor.client.widgets.PropertyEditorErrorWidget;
import org.uberfire.ext.properties.editor.client.widgets.PropertyEditorItemHelp;
import org.uberfire.ext.properties.editor.client.widgets.PropertyEditorItemLabel;
import org.uberfire.ext.properties.editor.client.widgets.PropertyEditorItemRemovalButton;
import org.uberfire.ext.properties.editor.client.widgets.PropertyEditorItemsWidget;
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

    public static void extractEditorFrom( final PropertyEditorWidget propertyEditorWidget,
                                          final PanelGroup propertyMenu,
                                          final PropertyEditorEvent event ) {
        extractEditorFrom( propertyEditorWidget, propertyMenu, event, "" );
    }

    static void createCategory( final PropertyEditorWidget propertyEditorWidget,
                                final PanelGroup propertyMenu,
                                final PropertyEditorCategory category,
                                final String propertyNameFilter ) {

        final Panel categoryPanel = createPanel( propertyMenu, propertyEditorWidget, category );
        final PanelBody panelBody = (PanelBody) ( (PanelCollapse) categoryPanel.getWidget( 1 ) ).getWidget( 0 );

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
            propertyMenu.add( categoryPanel );
        }
    }

    static Panel createPanel( final PanelGroup propertyMenu,
                              final PropertyEditorWidget propertyEditorWidget,
                              final PropertyEditorCategory category ) {
        final Panel categoryPanel = GWT.create( Panel.class );

        final PanelCollapse collapse = new PanelCollapse();
        {
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

            collapse.add( new PanelBody() );
        }
        final PanelHeader header = new PanelHeader();
        {
            header.setText( category.getName() );
            header.setDataToggle( Toggle.COLLAPSE );
            header.setDataParent( propertyMenu.getId() );
            header.setDataTargetWidget( collapse );
        }

        categoryPanel.add( header );
        categoryPanel.add( collapse );

        return categoryPanel;
    }

    static PropertyEditorItemsWidget createItemsWidget( final PropertyEditorFieldInfo field,
                                                        final PropertyEditorCategory category,
                                                        final PanelBody categoryAccordion ) {
        PropertyEditorItemsWidget items = GWT.create( PropertyEditorItemsWidget.class );
        items.add( createLabel( field ) );
        items.add( createField( field, items ) );
        if ( field.hasHelpInfo() ) {
            items.add( createHelp( field ) );
        }
        if ( field.isRemovalSupported() ) {
            items.add( createRemovalButton( field,
                                            category,
                                            items,
                                            categoryAccordion ) );
        }
        return items;
    }

    static PropertyEditorItemLabel createLabel( final PropertyEditorFieldInfo field ) {
        PropertyEditorItemLabel item = GWT.create( PropertyEditorItemLabel.class );
        item.setText( field.getLabel() );
        return item;
    }

    static PropertyEditorItemHelp createHelp( final PropertyEditorFieldInfo field ) {
        PropertyEditorItemHelp itemHelp = GWT.create( PropertyEditorItemHelp.class );
        itemHelp.setHeading( field.getHelpHeading() );
        itemHelp.setText( field.getHelpText() );
        return itemHelp;
    }

    static PropertyEditorItemWidget createField( final PropertyEditorFieldInfo field,
                                                 final PropertyEditorItemsWidget parent ) {
        PropertyEditorItemWidget itemWidget = GWT.create( PropertyEditorItemWidget.class );
        PropertyEditorErrorWidget errorWidget = GWT.create( PropertyEditorErrorWidget.class );
        PropertyEditorFieldType editorFieldType = PropertyEditorFieldType.getFieldTypeFrom( field );
        Widget widget;
        if ( editorFieldType == PropertyEditorFieldType.CUSTOM ) {
            Class<?> widgetClass = ( (CustomPropertyEditorFieldInfo) field ).getCustomEditorClass();
            widget = getWidget( field, widgetClass );
        } else {
            widget = editorFieldType.widget( field );
        }

        createErrorHandlingInfraStructure( parent, itemWidget, errorWidget, widget );
        itemWidget.add( widget );
        itemWidget.add( errorWidget );

        return itemWidget;
    }

    private static Widget getWidget( final PropertyEditorFieldInfo property,
                                     final Class fieldType ) {
        SyncBeanManager beanManager = IOC.getBeanManager();
        IOCBeanDef iocBeanDef = beanManager.lookupBean( fieldType );
        AbstractField field = (AbstractField) iocBeanDef.getInstance();
        return field.widget( property );
    }

    static void createErrorHandlingInfraStructure( final PropertyEditorItemsWidget parent,
                                                   final PropertyEditorItemWidget itemWidget,
                                                   final PropertyEditorErrorWidget errorWidget,
                                                   final Widget widget ) {
        AbstractPropertyEditorWidget abstractPropertyEditorWidget = (AbstractPropertyEditorWidget) widget;
        abstractPropertyEditorWidget.setErrorWidget( errorWidget );
        abstractPropertyEditorWidget.setParent( parent );
        itemWidget.add( widget );
    }

    static PropertyEditorItemRemovalButton createRemovalButton( final PropertyEditorFieldInfo field,
                                                                final PropertyEditorCategory category,
                                                                final PropertyEditorItemsWidget items,
                                                                final PanelBody categoryPanel ) {
        final PropertyEditorItemRemovalButton button = new PropertyEditorItemRemovalButton();
        button.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent event ) {
                category.getFields().remove( field );
                categoryPanel.remove( items );
            }
        } );
        return button;
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