/*
 * Copyright 2012 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uberfire.ext.widgets.common.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.Legend;
import org.gwtbootstrap3.client.ui.constants.FormType;

public class FormStyleLayout extends Form {

    public FormStyleLayout() {
        setType( FormType.HORIZONTAL );
    }

    public FormStyleLayout( final String title ) {
        this();
        add( new Legend( title ) );
    }

    public FormStyleLayout( final Image icon,
                            final String title ) {
        this();
        add( new Legend() {{
            getElement().appendChild( icon.getElement() );
            getElement().setInnerText( title );
        }} );
    }

    public int addAttribute( String label,
                             IsWidget widget ) {
        final FormStyleItem formStyleItem = GWT.create( FormStyleItem.class );
        formStyleItem.setup( label, widget );
        add( formStyleItem );
        return getWidgetCount() - 1;
    }

    public int addRow( final IsWidget widget ) {
        final FormGroup formGroup = new FormGroup();
        formGroup.add( widget );
        add( formGroup );
        return getWidgetCount() - 1;
    }

    public void setAttributeVisibility( final int index,
                                        final boolean b ) {
        try {
            final IsWidget widget = getWidget( index );
            if ( widget != null ) {
                getWidget( index ).setVisible( b );
            }
        } catch ( final IndexOutOfBoundsException ignore ) {
        }
    }

}
