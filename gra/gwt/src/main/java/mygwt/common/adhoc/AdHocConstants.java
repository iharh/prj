package mygwt.common.adhoc;

public final class AdHocConstants {
    private AdHocConstants() { }
    public static final String NO_CODE = "adhoc.no.code";

    public static final String UPLOAD_OK = "adhoc.upload.ok";
    public static final String UPLOAD_ERROR_CANTSAVE = "adhoc.upload.error.cantsave";
    public static final String SAMPLE_ERROR_CANTOPEN = "adhoc.sample.error.cantopen";
    public static final String SAMPLE_ERROR_CANTREAD = "adhoc.sample.error.io";
    public static final String SAMPLE_ERROR_EMPTY = "adhoc.sample.error.empty";


    public static final String SAMPLE_ERROR_HEADER_FIRST_ROW_EMPTY = "adhoc.sample.error.header.first.row.empty";
    public static final String SAMPLE_ERROR_HEADER = "adhoc.sample.error.header";
    public static final String SAMPLE_ERROR_HEADER_CELL_EMPT = "adhoc.sample.error.headerempty";
    public static final String SAMPLE_ERROR_HEADER_DUPL_COL_NAME = "adhoc.sample.error.dupl.colname";
    public static final String SAMPLE_ERROR_HEADER_DIGIT_COL_NAME = "adhoc.sample.error.digit.colname";
    public static final String SAMPLE_COLSKIP_ALL_EMPTY = "adhoc.column.skip.all.empty";

    public static final String STORE_ERROR = "adhoc.store.error";
    public static final String STORE_ERROR_WITH_MESS = "adhoc.store.error.with.message";
    public static final String STORE_ERROR_CHECK_TYPE = "adhoc.store.error.checktype";
    public static final String STORE_ERROR_CHECK_ATTR = "adhoc.store.error.checkattr";
    public static final String STORE_ERROR_CHECK_RESERVED = "adhoc.store.error.checkreserved";
    public static final String RUN_ERROR = "adhoc.runtmpl.error";

    public static final String DATA_SOURCE_COLUMN_NAME = "ADHOC_DATA_SOURCE";
    public static final String NATURAL_ID_COLUMN_NAME = "ADHOC_NATURAL_ID";
    public static final String ROWNUM_COLUMN_NAME = "ADHOC_ROWNUM";

    public static final String UPLOAD_RESULT_S = "[result]";
    public static final String UPLOAD_RESULT_E = "[/result]";

    public static final String PARAM_DELIM = ",";
    /** Prefix for Structured Attribute when its name is one of reserved. */
    public static final String ATTR_PREFIX = "U_";

    /* due to oracle object name length restriction
22 = 30(oracle) - 2(numbering) - 6("_VALUE" - longest generated suffix) */
    public static final int MAX_ATTR_NAME_LEN = 22; // Mirror from StructuredAttributesConstants.MAX_ATTR_NAME_LEN
    public static final int MAX_VERBATIM_NAME_LEN = 30;
}
