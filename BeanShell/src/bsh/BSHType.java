/*****************************************************************************
 *                                                                           *
 *  This file is part of the BeanShell Java Scripting distribution.          *
 *  Documentation and updates may be found at http://www.beanshell.org/      *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    * 
 *                                                                           *
 *  The Original Code is BeanShell. The Initial Developer of the Original    *
 *  Code is Pat Niemeyer. Portions created by Pat Niemeyer are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  Patrick Niemeyer (pat@pat.net)                                           *
 *  Author of Learning Java, O'Reilly & Associates                           *
 *  http://www.pat.net/~pat/                                                 *
 *                                                                           *
 *****************************************************************************/


package bsh;

import java.lang.reflect.Array;

class BSHType extends SimpleNode
{
    private int arrayDims;
    //private Class type;

    BSHType(int id) { super(id); }

    public void addArrayDimension() { arrayDims++; }

    // Returns a class for the type
    public Class getType(NameSpace namespace) throws EvalError
    {
/*
Note: Broken - need to add class loader listener if we're going to cache types
below is probably broken...  should work through namespace
*/
        // return cached type if available
        //if(type != null)
         //  return type;

		// If we want to cache this as above we have to add a listener for
		// the BshClassManager to let us know when types may have changed
    	Class type;

        //  first node will either be PrimitiveType or AmbiguousName
        SimpleNode node = (SimpleNode)jjtGetChild(0);

        Class baseType;
        if(node instanceof BSHPrimitiveType)
            baseType = ((BSHPrimitiveType)node).getType();
        else 
            baseType = ((BSHAmbiguousName)node).toClass(namespace);

        if(arrayDims > 0) {
            try {
                // construct array which has zero length in all dimensions
                // (faster than constructing the name by hand - see below)
                int[] dims = new int[arrayDims];
                Object obj = Array.newInstance(baseType, dims);
                type = obj.getClass();
            } catch(Exception e) {
                throw new EvalError("Couldn't construct array type", this);
            }
        } else
            type = baseType;

        return type;
    }
}
