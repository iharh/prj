package com.clarabridge.elasticsearch.index.migrate;

import com.clarabridge.transformer.indexing.pipe.ElasticSearchIndexer;

import org.elasticsearch.search.SearchHit;

import com.clarabridge.common.classification.lucene.LuceneAttributes;

//import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClbParentFinder {
    private static final Logger log = LoggerFactory.getLogger(ClbParentFinder.class);

    protected static final String PARENT = "_parent"; //$NON-NLS-1$

    public static final String [] usedESFieldNames = new String [] {
        //LuceneAttributes.FIELD_NAME_ID_DOCUMENT,
        LuceneAttributes.FIELD_NAME_ID_VERBATIM,
        PARENT
    };

    public static String getParentValue(SearchHit hit) {
        String result = null;

        String hitType = hit.getType();
        if (ElasticSearchIndexer.TYPE_VERBATIM.equals(hitType)) {
            result = hit.field(LuceneAttributes.FIELD_NAME_ID_DOCUMENT).value().toString();
        } else if (ElasticSearchIndexer.TYPE_SENTENCE.equals(hitType)) {
            result = hit.field(LuceneAttributes.FIELD_NAME_ID_VERBATIM).value().toString();
        } else { // TYPE_DOCUMENT does not need to have a parent TODO: ??? what about _parent for retweets?
            result = hit.field(PARENT).<String>value();
        }
        return result;
    }
}
