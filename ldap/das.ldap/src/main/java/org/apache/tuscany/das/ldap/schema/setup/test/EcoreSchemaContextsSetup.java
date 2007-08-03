package org.apache.tuscany.das.ldap.schema.setup.test;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import org.apache.tuscany.das.ldap.constants.DASConstants;

public class EcoreSchemaContextsSetup 
extends AbstractTestSetup
implements DASConstants
{
    public void tearDown() 
    throws NamingException, Exception
    {
        ecoreSyntaxesContext.close();
        ecoreContext.destroySubcontext( SYNTAXES_CONTEXT_RDN );
        
        ecoreAttributeTypesContext.close();
        ecoreContext.destroySubcontext( ATTRIBUTE_TYPES_CONTEXT_RDN );
        
        ecoreObjectClassesContext.close();
        ecoreContext.destroySubcontext( OBJECT_CLASSES_CONTEXT_RDN );
        
        ecoreContext.close();
        schemaContext.destroySubcontext( ECORE_CONTEXT_RDN );
        
        schemaContext.close();
        super.tearDown();
    }
    
    public void setUp() throws NamingException, Exception
    {
        super.setUp();
        schemaContext              = connect();
        ecoreContext               = createEcoreContext();
        ecoreSyntaxesContext       = createEcoreSyntaxesContext();
        ecoreObjectClassesContext  = createEcoreObjectClassesContext();
        ecoreAttributeTypesContext = createEcoreAttributeTypesContext();
    }
    
    private DirContext createEcoreContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(META_SCHEMA);

        Attribute ecoreAttribute = new BasicAttribute(
            CN, 
            ECORE_CONTEXT_NAME);

        contextAttributes.put( ecoreAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return schemaContext.createSubcontext( 
            ECORE_CONTEXT_RDN, contextAttributes );
    }
    

    private DirContext createEcoreObjectClassesContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        Attribute ecoreAttribute = new BasicAttribute(
            OU, 
            OBJECT_CLASSES_CONTEXT_NAME);

        contextAttributes.put( ecoreAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return ecoreContext.createSubcontext( 
            OBJECT_CLASSES_CONTEXT_RDN, contextAttributes );
    }
    
    

    private DirContext createEcoreAttributeTypesContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        Attribute ecoreAttribute = new BasicAttribute(
            OU, 
            ATTRIBUTE_TYPES_CONTEXT_NAME);

        contextAttributes.put( ecoreAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return ecoreContext.createSubcontext( 
            ATTRIBUTE_TYPES_CONTEXT_RDN, contextAttributes );
    }
    
    
    private DirContext createEcoreSyntaxesContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        Attribute ecoreAttribute = new BasicAttribute(
            OU, 
            SYNTAXES_CONTEXT_NAME);

        contextAttributes.put( ecoreAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return ecoreContext.createSubcontext( 
            SYNTAXES_CONTEXT_RDN, contextAttributes );
    }
    
    protected DirContext ecoreContext               = null;
    protected DirContext ecoreObjectClassesContext  = null;
    protected DirContext ecoreAttributeTypesContext = null;
    protected DirContext ecoreSyntaxesContext       = null;
}