package org.hibersap.configuration;

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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibersap.HibersapException;
import org.hibersap.configuration.xml.HiberSapJaxbXmlParser;

/*
 * @author Carsten Erker
 */
public final class Environment
{
    /*
     * The Hibersap Version.
     */
    public static final String VERSION = "0.2";

    /*
     * Where to find the hibersap.properties configuration file in the classpath.
     */
    public static final String HIBERSAP_PROPERTIES_FILE = "/hibersap.properties";

    /*
     * Where to find the hibersap.xml configuration file in the classpath.
     */
    public static final String HIBERSAP_XML_FILE = "/META-INF/hibersap.xml";

    private static final Log LOG = LogFactory.getLog( Environment.class );

    private static final Properties PROPERTIES = readProperties();

    private static Properties readProperties()
    {
        LOG.info( "Hibersap " + VERSION );
        Properties result = readXMLProperties( HIBERSAP_XML_FILE );

        if ( result.isEmpty() )
        {
            LOG.warn( "XML based configuration file " + HIBERSAP_XML_FILE
                + " not found or not readable, trying to use properties file " + HIBERSAP_PROPERTIES_FILE );
            result = readStringProperties( HIBERSAP_PROPERTIES_FILE );
        }

        addSystemProperties( result );

        return result;
    }

    static Properties readStringProperties( final String propertiesFile )
    {
        LOG.debug( "Trying to read properties from " + propertiesFile );

        final Properties properties = new Properties();

        try
        {
            final InputStream stream = ConfigHelper.getResourceAsStream( propertiesFile );

            try
            {
                properties.load( stream );
            }
            catch ( final Exception e )
            {
                LOG.error( "problem loading properties from " + propertiesFile );
            }
            finally
            {
                closeStream( propertiesFile, stream );
            }
        }
        catch ( final HibersapException he )
        {
            LOG.info( propertiesFile + " not found" );
        }

        return properties;
    }

    static Properties readXMLProperties( final String hibersapXmlFile )
    {
        LOG.debug( "Trying to read properties from " + hibersapXmlFile );
        final Properties properties = new Properties();

        try
        {
            final HiberSapJaxbXmlParser hibersapXMLParser = new HiberSapJaxbXmlParser();
            properties.putAll( hibersapXMLParser.parseResource( hibersapXmlFile ) );
            LOG.debug( "Properties read from " + hibersapXmlFile + ": " + properties );
        }
        catch ( final Exception e )
        {
            LOG.error( "Problems with reading/parsing " + hibersapXmlFile, e );
        }
        return properties;
    }

    private static void addSystemProperties( final Properties properties )
    {
        try
        {
            properties.putAll( System.getProperties() );
        }
        catch ( final SecurityException se )
        {
            LOG.warn( "could not copy system properties, system properties will be ignored" );
        }
    }

    private static void closeStream( final String fileName, final InputStream stream )
    {
        try
        {
            stream.close();
        }
        catch ( final IOException ioe )
        {
            LOG.error( "could not close stream on " + fileName, ioe );
        }
    }

    /*
     * Return <tt>System</tt> properties, extended by any properties specified in
     * <tt>hibersap.properties</tt>.
     * 
     * @return Properties
     */
    public static Properties getProperties()
    {
        final Properties copy = new Properties();
        copy.putAll( PROPERTIES );
        return copy;
    }
}
