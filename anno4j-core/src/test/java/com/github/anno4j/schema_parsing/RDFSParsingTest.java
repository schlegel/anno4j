package com.github.anno4j.schema_parsing;

import com.github.anno4j.Anno4j;
import com.github.anno4j.model.impl.ResourceObject;
import com.github.anno4j.querying.Comparison;
import com.github.anno4j.rdfs_parser.model.RDFSClazz;
import com.github.anno4j.rdfs_parser.model.RDFSProperty;
import org.apache.marmotta.ldpath.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.object.LangString;
import org.openrdf.rio.*;

import java.net.URL;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Test suite that tests the parsing of an RDF schema and the Anno4j classes therefore.
 */
public class RDFSParsingTest {

    private Anno4j anno4j;
    private final static String CIDOC_URL = "http://new.cidoc-crm.org/sites/default/files/cidoc_crm_v5.0.4_official_release.rdfs.xml";
    private final static String CIDOC_NS = "http://www.cidoc-crm.org/cidoc-crm/";

    private final static String ENTITY_URI = CIDOC_NS + "E1_CRM_Entity";
    private final static String TEMPORAL_ENTITY_URI = CIDOC_NS + "E2_Temporal_Entity";
    private final static String ACTIVITY_URI = CIDOC_NS + "E7_Activity";
    private final static String INSCRIPTION_URI = CIDOC_NS + "E34_Inscription";
    private final static String LINGUISTIC_OBJECT_URI = CIDOC_NS + "E33_Linguistic_Object";
    private final static String MARK_URI = CIDOC_NS + "E37_Mark";

    private final static String WAS_MOTIVATED_BY_URI = CIDOC_NS + "P17_was_motivated_by";
    private final static String WAS_INFLUENCED_BY_URI = CIDOC_NS + "P15_was_influenced_by";

    private final static String USED_SPECIFIC_OBJECT_URI = CIDOC_NS + "P16_used_specific_object";
    private final static String OCCURED_IN_THE_PRESENCE_OF_URI = CIDOC_NS + "P12_occurred_in_the_presence_of";

    @Before
    public void setUp() throws Exception {
        this.anno4j = new Anno4j();
        this.anno4j.parseSchema(new URL(CIDOC_URL), RDFFormat.RDFXML, CIDOC_NS);
    }

    @Test
    public void testClazzesAndProperties() throws RepositoryException, ParseException, MalformedQueryException, QueryEvaluationException {
        List<RDFSClazz> clazzes = this.anno4j.createQueryService().addCriteria(".", CIDOC_NS, Comparison.STARTS_WITH).execute(RDFSClazz.class);

        assertEquals(82, clazzes.size());

        List<RDFSProperty> properties = this.anno4j.createQueryService().addCriteria(".", CIDOC_NS, Comparison.STARTS_WITH).execute(RDFSProperty.class);

        assertEquals(262, properties.size());
    }

    @Test
    public void testClazzFields() throws RepositoryException, ParseException, MalformedQueryException, QueryEvaluationException {
        RDFSClazz entity = this.anno4j.createQueryService().addCriteria(".", ENTITY_URI).execute(RDFSClazz.class).get(0);

        Set<CharSequence> comments = entity.getComments();

        assertEquals(6, entity.getLabels().size());
        assertEquals(1, comments.size());

        CharSequence comment = comments.iterator().next();
        assertEquals(LangString.class, comment.getClass());
        assertTrue(((LangString) comment).startsWith("This class comprises"));

        RDFSClazz temporalEntity = this.anno4j.createQueryService().addCriteria(".", TEMPORAL_ENTITY_URI).execute(RDFSClazz.class).get(0);

        assertEquals(entity.getResourceAsString(), ((ResourceObject) temporalEntity.getSubClazzes().toArray()[0]).getResourceAsString());

        RDFSClazz inscription = this.anno4j.createQueryService().addCriteria(".", INSCRIPTION_URI).execute(RDFSClazz.class).get(0);

        assertEquals(2, inscription.getSubClazzes().size());
        RDFSClazz subClazz1 = (RDFSClazz) inscription.getSubClazzes().toArray()[0];
        RDFSClazz subClazz2 = (RDFSClazz) inscription.getSubClazzes().toArray()[1];

        assertTrue(subClazz1.getResourceAsString().equals(LINGUISTIC_OBJECT_URI) || subClazz1.getResourceAsString().equals(MARK_URI));
        assertTrue(subClazz2.getResourceAsString().equals(LINGUISTIC_OBJECT_URI) || subClazz2.getResourceAsString().equals(MARK_URI));
    }

    @Test
    public void testPropertyFields() throws RepositoryException, ParseException, MalformedQueryException, QueryEvaluationException {
        RDFSProperty motivated = this.anno4j.createQueryService().addCriteria(".", WAS_MOTIVATED_BY_URI).execute(RDFSProperty.class).get(0);
        RDFSProperty influenced = this.anno4j.createQueryService().addCriteria(".", WAS_INFLUENCED_BY_URI).execute(RDFSProperty.class).get(0);

        RDFSClazz entity = this.anno4j.createQueryService().addCriteria(".", ENTITY_URI).execute(RDFSClazz.class).get(0);
        RDFSClazz activity = this.anno4j.createQueryService().addCriteria(".", ACTIVITY_URI).execute(RDFSClazz.class).get(0);

        Set<RDFSClazz> motivatedDomain = motivated.getDomains();
        Set<RDFSClazz> motivatedRange = motivated.getRanges();

        Set<CharSequence> motivatedComments = motivated.getComments();
        assertEquals(6, motivated.getLabels().size());
        assertEquals(1, motivatedComments.size());

        CharSequence comment = motivatedComments.iterator().next();
        assertEquals(LangString.class, comment.getClass());
        assertTrue(((LangString) comment).startsWith("This property describes an item"));
        assertEquals(influenced.getResourceAsString(), ((ResourceObject) motivated.getSubProperties().toArray()[0]).getResourceAsString());
        assertEquals(1, motivatedDomain.size());
        assertEquals(1, motivatedRange.size());
        assertEquals(activity.getResourceAsString(), motivatedDomain.iterator().next().getResourceAsString());
        assertEquals(entity.getResourceAsString(), motivatedRange.iterator().next().getResourceAsString());

        RDFSProperty specific = this.anno4j.createQueryService().addCriteria(".", USED_SPECIFIC_OBJECT_URI).execute(RDFSProperty.class).get(0);
        RDFSProperty subProperty1 = (RDFSProperty) specific.getSubProperties().toArray()[0];
        RDFSProperty subProperty2 = (RDFSProperty) specific.getSubProperties().toArray()[1];

        assertTrue(subProperty1.getResourceAsString().equals(OCCURED_IN_THE_PRESENCE_OF_URI) || subProperty1.getResourceAsString().equals(WAS_INFLUENCED_BY_URI));
        assertTrue(subProperty2.getResourceAsString().equals(OCCURED_IN_THE_PRESENCE_OF_URI) || subProperty2.getResourceAsString().equals(WAS_INFLUENCED_BY_URI));
    }
}
