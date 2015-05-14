/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * 12-jul-2005: created
 *
 */
package com.philemonworks.umlx;

import com.philemonworks.umlx.elements.Model;

/**
 * IExporter is 
 * 
 * @author E.M.Micklei
 */
public interface IExporter {
    public void setFilePath(String path);
    public void export(Model aModel);
}
