package org.hibersap.examples.flightlist;

/*
 * Copyright (C) 2008 akquinet tech@spree GmbH
 * 
 * This file is part of Hibersap.
 * 
 * Hibersap is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 * 
 * Hibersap is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with Hibersap. If
 * not, see <http://www.gnu.org/licenses/>.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.examples.AbstractHibersapTest;
import org.hibersap.session.Session;
import org.hibersap.session.SessionManager;
import org.junit.Test;

/**
 * @author Carsten Erker
 */
public class FlightListTest
    extends AbstractHibersapTest
{
    private static final Log LOG = LogFactory.getLog( FlightListTest.class );

    // TODO make test pass
    @Test
    public void testLetTestCasePass()
    {

    }

    // @Test
    public void showFlightList()
    {
        final AnnotationConfiguration configuration = new AnnotationConfiguration();
        final SessionManager sessionManager = configuration.buildSessionManager();
        final Session session = sessionManager.openSession();

        try
        {
            final FlightListBapi flightList = new FlightListBapi( "DE", "Frankfurt", "DE", "Berlin", null, false, 10 );
            session.execute( flightList );
            showResult( flightList );
        }
        finally
        {
            session.close();
        }
    }



    private void showResult( final FlightListBapi flightList )
    {
        LOG.info( flightList );
    }
}
