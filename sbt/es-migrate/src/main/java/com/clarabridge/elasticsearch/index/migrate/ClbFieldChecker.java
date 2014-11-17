package com.clarabridge.elasticsearch.ingex.migrate;

import com.clarabridge.transformer.indexing.pipe.ElasticSearchIndexer;

import org.elasticsearch.search.SearchHit;

import com.clarabridge.common.classification.lucene.LuceneAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.HashMap;

public class ClbFieldChecker {
    private static final Logger log = LoggerFactory.getLogger(ClbFieldChecker.class);

    private Map<String, Integer> docsShards = new HashMap<String, Integer>();
    private Map<String, Integer> verbShards = new HashMap<String, Integer>();
    //private Map<String, Integer> sentShards = new HashMap<String, Integer>();

    public void addHit(SearchHit hit) {
        String hitType = hit.getType();
        String hitId = hit.getId();
        Integer hitShard = new Integer(hit.getShard().getShardId());

        if (ElasticSearchIndexer.TYPE_DOCUMENT.equals(hitType)) {
            docsShards.put(hitId, hitShard);
        } else if (ElasticSearchIndexer.TYPE_VERBATIM.equals(hitType)) {
            verbShards.put(hitId, hitShard);
            String parentId = hit.field(LuceneAttributes.FIELD_NAME_ID_DOCUMENT).value().toString();

            Integer parentShard = docsShards.get(parentId);
            if (parentShard == null) {
                log.error("!!! no shard for parent for verbatim: {}", hitId);
            } else if (!parentShard.equals(hitShard)) {
                log.error("!!! shard mismatch for verbatim: {}", hitId);
            }
        } else if (ElasticSearchIndexer.TYPE_SENTENCE.equals(hitType)) {
            //sentShards.put(hitId, hitShard);
            String parentId = hit.field(LuceneAttributes.FIELD_NAME_ID_VERBATIM).value().toString();

            Integer parentShard = verbShards.get(parentId);
            if (parentShard == null) {
                log.error("!!! no shard for parent for sentence: {}", hitId);
            } else if (!parentShard.equals(hitShard)) {
                log.error("!!! shard mismatch for sentence: {}", hitId);
            }
        } //else { // TYPE_DOCUMENT does not need to have a parent TODO: ??? what about _parent for retweets?
        //    result = hit.field(PARENT).<String>value();
        //}
    }
}
