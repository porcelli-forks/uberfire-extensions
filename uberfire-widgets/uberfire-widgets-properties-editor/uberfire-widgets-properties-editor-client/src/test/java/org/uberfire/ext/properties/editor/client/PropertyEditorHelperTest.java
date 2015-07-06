//package org.uberfire.ext.properties.editor.client;
//
//import java.util.ArrayList;
//
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.user.client.ui.Widget;
//import com.google.gwtmockito.GwtMockitoTestRunner;
//import org.gwtbootstrap3.client.shared.event.ShowHandler;
//import org.gwtbootstrap3.client.ui.Panel;
//import org.gwtbootstrap3.client.ui.PanelBody;
//import org.gwtbootstrap3.client.ui.PanelCollapse;
//import org.gwtbootstrap3.client.ui.PanelGroup;
//import org.gwtbootstrap3.client.ui.PanelHeader;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.uberfire.ext.properties.editor.client.widgets.PropertyEditorErrorWidget;
//import org.uberfire.ext.properties.editor.client.widgets.PropertyEditorItemLabel;
//import org.uberfire.ext.properties.editor.client.widgets.PropertyEditorItemsWidget;
//import org.uberfire.ext.properties.editor.client.widgets.PropertyEditorTextBox;
//import org.uberfire.ext.properties.editor.model.PropertyEditorCategory;
//import org.uberfire.ext.properties.editor.model.PropertyEditorEvent;
//import org.uberfire.ext.properties.editor.model.PropertyEditorFieldInfo;
//import org.uberfire.ext.properties.editor.model.PropertyEditorType;
//
//import static org.junit.Assert.*;
//import static org.mockito.Mockito.*;
//
//@RunWith(GwtMockitoTestRunner.class)
//@Ignore
//public class PropertyEditorHelperTest {
//
//    @Test(expected = PropertyEditorHelper.NullEventException.class)
//    public void validateNullEventTest() {
//        PropertyEditorHelper.validade( null );
//    }
//
//    @Test(expected = PropertyEditorHelper.NoPropertiesException.class)
//    public void validateEventWithNoPropertiesTest() {
//        PropertyEditorEvent event = new PropertyEditorEvent( "id", new ArrayList<PropertyEditorCategory>() );
//        PropertyEditorHelper.validade( event );
//    }
//
//    @Test
//    public void validateEventTest() {
//        ArrayList<PropertyEditorCategory> properties = new ArrayList<PropertyEditorCategory>();
//        properties.add( new PropertyEditorCategory( "Category" ) );
//        PropertyEditorEvent event = new PropertyEditorEvent( "id", properties );
//        assertTrue( PropertyEditorHelper.validade( event ) );
//    }
//
//    @Test
//    public void isAMatchOfEmptyFilterTest() {
//        PropertyEditorFieldInfo field = new PropertyEditorFieldInfo();
//        assertTrue( PropertyEditorHelper.isAMatchOfFilter( "", field ) );
//    }
//
//    @Test
//    public void isAMatchOfFilterTest() {
//        PropertyEditorFieldInfo field = new PropertyEditorFieldInfo( "label", PropertyEditorType.TEXT );
//        assertTrue( PropertyEditorHelper.isAMatchOfFilter( "l", field ) );
//        assertTrue( PropertyEditorHelper.isAMatchOfFilter( "label", field ) );
//        assertTrue( PropertyEditorHelper.isAMatchOfFilter( "LABEL", field ) );
//        assertTrue( PropertyEditorHelper.isAMatchOfFilter( "abel", field ) );
//        assertFalse( PropertyEditorHelper.isAMatchOfFilter( "LABELL", field ) );
//        assertFalse( PropertyEditorHelper.isAMatchOfFilter( "LASBELL", field ) );
//        assertFalse( PropertyEditorHelper.isAMatchOfFilter( "p", field ) );
//    }
//
//    @Test
//    public void createLabelTest() {
//        PropertyEditorFieldInfo field = new PropertyEditorFieldInfo( "label", PropertyEditorType.TEXT );
//        PropertyEditorItemLabel label = PropertyEditorHelper.createLabel( field );
//        verify( label ).setText( "label" );
//    }
//
//    @Test
//    public void createErrorHandlingInfraStructureTest() {
//
//        PropertyEditorTextBox widget = GWT.create( PropertyEditorTextBox.class );
//        PropertyEditorItemWidget itemWidget = GWT.create( PropertyEditorItemWidget.class );
//        PropertyEditorItemsWidget parent = GWT.create( PropertyEditorItemsWidget.class );
//        PropertyEditorErrorWidget errorWidget = GWT.create( PropertyEditorErrorWidget.class );
//
//        PropertyEditorHelper.createErrorHandlingInfraStructure( parent, itemWidget, errorWidget, widget );
//        verify( widget ).setErrorWidget( errorWidget );
//        verify( widget ).setParent( parent );
//        verify( itemWidget ).add( widget );
//
//    }
//
//    @Test
//    public void createAccordionGroupTest() {
//        PropertyEditorWidget propertyEditorWidget = GWT.create( PropertyEditorWidget.class );
//        PropertyEditorCategory category = new PropertyEditorCategory( "Category" );
////        Panel accordionGroup = PropertyEditorHelper.createPanel( propertyEditorWidget.propertyMenu, propertyEditorWidget, category );
//
//        assertTrue( accordionGroup.getWidget( 0 ) instanceof PanelHeader );
//        assertTrue( accordionGroup.getWidget( 1 ) instanceof PanelCollapse );
//        assertTrue( ( (PanelCollapse) accordionGroup.getWidget( 1 ) ).getWidget( 0 ) instanceof PanelBody );
//
//        final PanelHeader header = (PanelHeader) accordionGroup.getWidget( 0 );
//        final PanelCollapse collapse = (PanelCollapse) accordionGroup.getWidget( 1 );
//
//        verify( header ).setTitle( "Category" );
//        verify( collapse ).addShowHandler( any( ShowHandler.class ) );
//    }
//
//    @Test
//    public void createOpenAccordionGroupTest() {
//        PropertyEditorWidget propertyEditorWidget = GWT.create( PropertyEditorWidget.class );
//        when( propertyEditorWidget.getLastOpenAccordionGroupTitle() ).thenReturn( "Category" );
//
//        PropertyEditorCategory category = new PropertyEditorCategory( "Category" );
////        Panel accordionGroup = PropertyEditorHelper.createPanel( propertyEditorWidget.propertyMenu, propertyEditorWidget, category );
//
//        assertTrue( accordionGroup.getWidget( 0 ) instanceof PanelHeader );
//        assertTrue( accordionGroup.getWidget( 1 ) instanceof PanelCollapse );
//        assertTrue( ( (PanelCollapse) accordionGroup.getWidget( 1 ) ).getWidget( 0 ) instanceof PanelBody );
//
//        final PanelHeader header = (PanelHeader) accordionGroup.getWidget( 0 );
//        final PanelCollapse collapse = (PanelCollapse) accordionGroup.getWidget( 1 );
//
//        verify( header ).setTitle( "Category" );
//        verify( collapse ).addShowHandler( any( ShowHandler.class ) );
//        verify( collapse ).setIn( true );
//    }
//
//    @Test
//    public void createCategoryWithNoFields() {
//        PropertyEditorWidget propertyEditorWidget = GWT.create( PropertyEditorWidget.class );
//        PanelGroup propertyMenu = GWT.create( PanelGroup.class );
//
//        PropertyEditorCategory category = new PropertyEditorCategory( "1" );
//        PropertyEditorHelper.createCategory( propertyEditorWidget, propertyMenu, category, "" );
//
//        verify( propertyMenu, never() ).add( any( Widget.class ) );
//    }
//
//}
