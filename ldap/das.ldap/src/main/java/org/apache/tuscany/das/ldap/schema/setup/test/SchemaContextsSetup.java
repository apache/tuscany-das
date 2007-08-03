package org.apache.tuscany.das.ldap.schema.setup.test;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;

public class SchemaContextsSetup 
extends AbstractTestSetup
{
    public void tearDown() throws NamingException, Exception
    {
        dasSyntaxesContext.close();
        dasContext.destroySubcontext( SYNTAXES_CONTEXT_RDN );

        dasAttributeTypesContext.close();
        dasContext.destroySubcontext( ATTRIBUTE_TYPES_CONTEXT_RDN );
        
        dasObjectClassesContext.close();
        dasContext.destroySubcontext( OBJECT_CLASSES_CONTEXT_RDN );
        dasContext.close();
        
        xsdSyntaxesContext.close();
        xsdContext.destroySubcontext( SYNTAXES_CONTEXT_RDN );
        
        xsdContext.close();
        schemaContext.destroySubcontext( XSD_CONTEXT_RDN );

        ecoreSyntaxesContext.close();
        ecoreContext.destroySubcontext( SYNTAXES_CONTEXT_RDN );
        
        ecoreContext.close();
        schemaContext.destroySubcontext( ECORE_CONTEXT_RDN );
        
        schemaContext.destroySubcontext( DAS_CONTEXT_RDN );
        schemaContext.close();
        super.tearDown();
    }
    
    public void setUp() throws NamingException, Exception
    {
        super.setUp();
        schemaContext            = connect();
        dasContext               = createDASContext();
        dasSyntaxesContext       = createSyntaxesContext();
        dasAttributeTypesContext = createAttributeTypesContext();
        dasObjectClassesContext  = createObjectClassesContext();
        xsdContext               = createXSDContext();
        xsdSyntaxesContext       = createXSDSyntaxesContext();
        ecoreContext             = createEcoreContext();
        ecoreSyntaxesContext     = createEcoreSyntaxesContext();
    }
    
    private DirContext createDASContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(META_SCHEMA);

        Attribute dasAttribute = new BasicAttribute(
            CN, 
            DAS_CONTEXT_NAME);

        contextAttributes.put( dasAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return schemaContext.createSubcontext( 
            DAS_CONTEXT_RDN, contextAttributes );
    }

    
    private DirContext createXSDContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(META_SCHEMA);

        Attribute xsdAttribute = new BasicAttribute(
            CN, 
            XSD_CONTEXT_NAME);

        contextAttributes.put( xsdAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return schemaContext.createSubcontext( 
            XSD_CONTEXT_RDN, contextAttributes );
    }

    private DirContext createXSDSyntaxesContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        Attribute xsdAttribute = new BasicAttribute(
            OU, 
            SYNTAXES_CONTEXT_NAME);

        contextAttributes.put( xsdAttribute );
        contextAttributes.put( objectClassAttribute );
        
        return xsdContext.createSubcontext( 
            SYNTAXES_CONTEXT_RDN, contextAttributes );
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
    
    private DirContext createSyntaxesContext() throws NamingException
    {   
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);
        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        Attribute syntaxesRDNAttribute = new BasicAttribute(
            OU, 
            SYNTAXES_CONTEXT_NAME);

        contextAttributes.put( objectClassAttribute );
        contextAttributes.put( syntaxesRDNAttribute );
        
        return dasContext.createSubcontext( 
            SYNTAXES_CONTEXT_RDN, contextAttributes );
    }
    
    
    private DirContext createAttributeTypesContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);

        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        Attribute attributeTypes = new BasicAttribute(
            OU, 
            ATTRIBUTE_TYPES_CONTEXT_NAME);

        contextAttributes.put( attributeTypes );
        contextAttributes.put( objectClassAttribute );
        
        return dasContext.createSubcontext( 
            ATTRIBUTE_TYPES_CONTEXT_RDN, contextAttributes );
    }

    private DirContext createObjectClassesContext() throws NamingException
    {
        Attributes contextAttributes   = new BasicAttributes();
        
        Attribute objectClassAttribute = new BasicAttribute(
        OBJECT_CLASS, 
        TOP);

        objectClassAttribute.add(ORGANIZATIONAL_UNIT);

        Attribute attributeTypes = new BasicAttribute(
            OU, 
            OBJECT_CLASSES_CONTEXT_NAME);

        contextAttributes.put( attributeTypes );
        contextAttributes.put( objectClassAttribute );
        
        return dasContext.createSubcontext( 
            OBJECT_CLASSES_CONTEXT_RDN, 
            contextAttributes );
    }
    
    protected DirContext dasContext               = null;
    protected DirContext xsdContext               = null;
    protected DirContext xsdSyntaxesContext       = null;
    protected DirContext ecoreContext             = null;
    protected DirContext ecoreSyntaxesContext     = null;
    protected DirContext dasSyntaxesContext       = null;
    protected DirContext dasAttributeTypesContext = null;
    protected DirContext dasObjectClassesContext  = null;
}