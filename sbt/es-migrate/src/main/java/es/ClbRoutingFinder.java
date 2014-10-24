package es;

import org.elasticsearch.search.SearchHit;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class ClbRoutingFinder {
    private static final Logger log = LoggerFactory.getLogger(ClbRoutingFinder.class);

    // TODO: move this out of here !!!
    public static class ElasticSearchIndexer {
        public static final String TYPE_DOCUMENT = "document"; //$NON-NLS-1$
        public static final String TYPE_VERBATIM = "verbatim"; //$NON-NLS-1$
        public static final String TYPE_SENTENCE = "sentence"; //$NON-NLS-1$
    }
    public static interface LuceneAttributes { // com.clarabridge.common.classification.lucene
        String FIELD_NAME_ID_DOCUMENT = "_id_document";
        // 6.2.0
        String FIELD_NAME_ID_PARENT_NATURAL = "parent_natural_id";  //$NON-NLS-1$
        String FIELD_NAME_ID_NATURAL = "natural_id";  //$NON-NLS-1$
    }

    // com.clarabridge.transformer.indexing.pipe.ElasticSearchRouting
    protected static final String SM_SERVICE = "sm_service"; //$NON-NLS-1$
    protected static final String SM_FACEBOOK = "facebook"; //$NON-NLS-1$
    protected static final String FB_POST_ID = "post_id"; //$NON-NLS-1$
    protected static final String EMPTY_ROUTING = "NULL"; //$NON-NLS-1$


    public static final String [] usedESFieldNames = new String [] {
        LuceneAttributes.FIELD_NAME_ID_DOCUMENT,
        LuceneAttributes.FIELD_NAME_ID_NATURAL,
        LuceneAttributes.FIELD_NAME_ID_PARENT_NATURAL,
        SM_SERVICE,
        FB_POST_ID
    };

    public static String getRoutingValue(SearchHit hit) {
        String result = null;

        String hitType = hit.getType();
        if (ElasticSearchIndexer.TYPE_DOCUMENT.equals(hitType)) {
            result = getUnsafeDocumentRouting(
                hit.field(LuceneAttributes.FIELD_NAME_ID_NATURAL).<String>value(),
                hit.field(LuceneAttributes.FIELD_NAME_ID_PARENT_NATURAL).<String>value(),
                hit.field(SM_SERVICE).<String>value(),
                hit.field(FB_POST_ID).<String>value()
            );
            result = StringUtils.isEmpty(result) ? EMPTY_ROUTING : result; 

        } else if (ElasticSearchIndexer.TYPE_VERBATIM.equals(hitType) || ElasticSearchIndexer.TYPE_SENTENCE.equals(hitType)) {
            result = hit.field(LuceneAttributes.FIELD_NAME_ID_DOCUMENT).value().toString(); // in-place just use: .<Long>value();
        }
        return result;
    }

    // TODO: eliminate copy-paste from ElasticSearchRouting by making this class to be a superclass for it
    public static String getDocumentRouting(String naturalId, String parentDocumentNaturalId, Map<String,String> attributeValues) {
        String value = getUnsafeDocumentRouting(naturalId, parentDocumentNaturalId, attributeValues);
        return StringUtils.isEmpty(value)?  EMPTY_ROUTING : value; 
    }
    
    private static String getUnsafeDocumentRouting(String naturalId, String parentDocumentNaturalId, Map<String,String> attributeValues) {
        return getUnsafeDocumentRouting(naturalId, parentDocumentNaturalId, attributeValues.get(SM_SERVICE), attributeValues.get(FB_POST_ID));
    }

    private static String getUnsafeDocumentRouting(String naturalId, String parentDocumentNaturalId, String smService, String fbPostId) {
        if (parentDocumentNaturalId == null) {
            return (naturalId != null)
                ? naturalId.toLowerCase() // For parent document itself routing ID always equals natural ID
                : StringUtils.EMPTY;
        } else {
            // Facebook may have 3 levels, always refet to post ID in 2nd and 3rd levels
            return (SM_FACEBOOK.equals(smService))
                ? fbPostId
                : parentDocumentNaturalId.toLowerCase();
        }
    }
}
