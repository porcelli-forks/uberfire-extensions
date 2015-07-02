package org.uberfire.client.docks;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.uberfire.client.resources.WebAppResource;
import org.uberfire.client.workbench.docks.UberfireDockPosition;
import org.uberfire.mvp.ParameterizedCommand;

public class DocksExpandedBar
        extends Composite {

    private UberfireDockPosition position;

    @UiField
    FlowPanel titlePanel;

    @UiField
    FlowPanel targetPanel;

    interface ViewBinder
            extends
            UiBinder<Widget, DocksExpandedBar> {

    }

    private ViewBinder uiBinder = GWT.create( ViewBinder.class );

    private static WebAppResource CSS = GWT.create( WebAppResource.class );

    public DocksExpandedBar( UberfireDockPosition position ) {
        initWidget( uiBinder.createAndBindUi( this ) );
        this.position = position;
    }

    public void setup( String identifier,
                       ParameterizedCommand<String> deselectCommand ) {
        titlePanel.clear();
        Label label = new Label( identifier );
        label.addStyleName( CSS.CSS().dockLabel() );
        titlePanel.add( label );
        createButtons( identifier, deselectCommand );
    }

    public void createButtons( final String identifier,
                               final ParameterizedCommand<String> deselectCommand ) {
        Button collapse = new Button();
        collapse.setIcon( IconType.SORT_DOWN );
        collapse.setSize( ButtonSize.EXTRA_SMALL );
        collapse.addClickHandler( new ClickHandler() {
            @Override
            public void onClick( ClickEvent event ) {
                deselectCommand.execute( identifier );
            }
        } );
        collapse.addStyleName( CSS.CSS().dockExpandedButton() );
        titlePanel.add( collapse );
    }

    public void setPanelSize( int width,
                              int height ) {
        targetPanel.setPixelSize( width, height );
    }

    public FlowPanel targetPanel() {
        return targetPanel;
    }

    public void clear() {
        targetPanel.clear();
    }

    public double defaultWidgetSize() {
        return 150;
    }

    public UberfireDockPosition getPosition() {
        return position;
    }
}
