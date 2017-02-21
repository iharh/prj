package mygwt.common.security.model;

public enum ClassName {
    DB(1000), PROJECT(1001), NODE(1003), MODEL(1002), SENTIMENTS(1005), DISCOVERY(1006), REPORT(1004), SETTINGS(1008),
    AD_HOC_DATA(1010), ADD_PROJECT(1009), LOCK_NODES(1011), SENTENCE_EXCLUSION(1012), DELETE_MODEL(1013), 
    SHARED_FILTERS(1014), ADD_MODEL(1015), EXCEPTION_RULES(1016), ACCESS_OUTSIDE_ACCOUNT(1019),
    REPORT_DISTRIBUTION(1018), FORCE_DISCONNECT(1017), TAXONOMY(1020), DB_LIST(1021), NONE(1099);

    private long id;
    
    private ClassName(long id) {
        this.id = id;
    }
	
    public static ClassName findById(long id) {
        for (ClassName e : values()) {
            if (e.id == id)
                return e;
        }
        throw new IllegalArgumentException("Uknown objectClassId(" + id + ")");
    }
}
