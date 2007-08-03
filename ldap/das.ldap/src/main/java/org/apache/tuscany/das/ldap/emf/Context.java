
package org.apache.tuscany.das.ldap.emf;

import java.util.Hashtable;
import java.util.Map;

import javax.naming.ldap.LdapContext;

import org.eclipse.emf.ecore.sdo.EDataObject;

public class Context
{
    private Map<String, LdapContext> xsdNamespaceToLdapContextMap = null;
    private Map<EDataObject, String> dataObjectToRelativeDNCache  = null; 


    private LdapContext dasContext                 = null;
    private LdapContext schemaContext              = null;
    private LdapContext dasMetaContext             = null;
    private LdapContext ecoreObjectClassesContext  = null;
    private LdapContext ecoreAttributeTypesContext = null;
    
    public Context()
    {
        xsdNamespaceToLdapContextMap               = 
            new Hashtable<String, LdapContext>();
        
        dataObjectToRelativeDNCache                = 
            new Hashtable<EDataObject, String>();
    }

    public LdapContext getDasContext()
    {
        return dasContext;
    }
    public void setDasContext( LdapContext dasContext )
    {
        this.dasContext = dasContext;
    }
    public LdapContext getSchemaContext()
    {
        return schemaContext;
    }
    public void setSchemaContext( LdapContext schemaContext )
    {
        this.schemaContext = schemaContext;
    }
    public LdapContext getDasMetaContext()
    {
        return dasMetaContext;
    }
    public void setDasMetaContext( LdapContext dasMetaContext )
    {
        this.dasMetaContext = dasMetaContext;
    }
    public LdapContext getEcoreObjectClassesContext()
    {
        return ecoreObjectClassesContext;
    }
    public void setEcoreObjectClassesContext( LdapContext ecoreObjectClassesContext )
    {
        this.ecoreObjectClassesContext = ecoreObjectClassesContext;
    }
    public LdapContext getEcoreAttributeTypesContext()
    {
        return ecoreAttributeTypesContext;
    }
    public void setEcoreAttributeTypesContext( LdapContext ecoreAttributeTypesContext )
    {
        this.ecoreAttributeTypesContext = ecoreAttributeTypesContext;
    }
    public Map<String, LdapContext> getXsdNamespaceToLdapContextMap()
    {
        return xsdNamespaceToLdapContextMap;
    }

    public Map<EDataObject, String> getDataObjectToRelativeDNCache()
    {
        return dataObjectToRelativeDNCache;
    }
}
