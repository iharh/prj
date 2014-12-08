package com.clarabridge.elasticsearch.index.migrate;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.index.mapper.internal.RoutingFieldMapper;

import com.clarabridge.common.classification.lucene.LuceneAttributes;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class ClbRoutingFinder {
    private static final Logger log = LoggerFactory.getLogger(ClbRoutingFinder.class);

    protected static final String SM_SERVICE = "sm_service"; //$NON-NLS-1$
    protected static final String SM_FACEBOOK = "facebook"; //$NON-NLS-1$
    protected static final String FB_POST_ID = "post_id"; //$NON-NLS-1$
    protected static final String EMPTY_ROUTING = "NULL"; //$NON-NLS-1$

    public static final String [] usedESFieldNames = new String [] {
        RoutingFieldMapper.NAME,
        LuceneAttributes.FIELD_NAME_ID_DOCUMENT,
        LuceneAttributes.FIELD_NAME_ID_NATURAL,
        LuceneAttributes.FIELD_NAME_ID_PARENT_NATURAL,
        SM_SERVICE,
        FB_POST_ID
    };

    public static String getRoutingValue(SearchHit hit) {
        return hit.field(RoutingFieldMapper.NAME).<String>value();
    }

    // not-used
    /*
    private static String getUpdateRoutingValue(SearchHit hit, Map<Long, String> docRouting) {
        String result = null;

        String hitType = hit.getType();
        if (ESConstants.TYPE_DOCUMENT.equals(hitType)) {
            Long docId = hit.field(LuceneAttributes.FIELD_NAME_ID_DOCUMENT).<Long>value();

            result = getUnsafeDocumentRouting(
                hit.field(LuceneAttributes.FIELD_NAME_ID_NATURAL).<String>value(),
                hit.field(LuceneAttributes.FIELD_NAME_ID_PARENT_NATURAL).<String>value(),
                hit.field(SM_SERVICE).<String>value(),
                hit.field(FB_POST_ID).<String>value()
            );

            result = StringUtils.isEmpty(result) ? EMPTY_ROUTING : result; 
            docRouting.put(docId, result);
        } else if (ESConstants.TYPE_VERBATIM.equals(hitType) || ESConstants.TYPE_SENTENCE.equals(hitType)) {
            Long docId = hit.field(LuceneAttributes.FIELD_NAME_ID_DOCUMENT).<Long>value();
            result = docRouting.get(docId);
            //result = StringUtils.isEmpty(result) ? EMPTY_ROUTING : result; 
        }
        return result;
    }
    */

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
