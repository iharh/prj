grammar Rule;

// parser rules

cb_rule
    :
    clause_or+ COMMA? EOF
    ;

clause_or
    :
    clause_and ((OR|COMMA+)? clause_and)*
    ;

clause_and
    :
    clause_not_or_basic (AND clause_not_or_basic)*
    ;

clause_not_or_basic
    :
    clause_not
    | clause_basic
    ;

clause_not
    :
    NOT clause_basic
    ;

clause_basic
    :
    modifier? LPAREN clause_or+ COMMA? RPAREN term_modifier?
    | atom
    ;

// | LPAREN clause_or+ RPAREN

atom
    :
    modifier? cb_lc
    | modifier? cb_catref
    | modifier? cb_catrollup
    | modifier? cb_field
    | modifier? cb_value_term_mod
    ;

// field rules

cb_field
    :
    field cb_singleormulti_value term_modifier?
    ;

field
    :
    TERM_NORMAL COLON
    ;

cb_singleormulti_value
    :
    value
    | multi_value
    ;

// value rules


multi_value
	:
	LPAREN multi_clause RPAREN
	;

multi_clause
	:
	clause_or+
	;

cb_value_term_mod
    :
    cb_value
    | value term_modifier
    ;

cb_value
    :
    value
    ;

// cb linguistic connection
cb_lc
    :
    LC COLON cb_lc_range
    ;

cb_lc_range
    :
    LBRACK cb_lc_lvalue COMMA modifier? cb_lc_rvalue RBRACK
    ;

cb_lc_lvalue
    :
    normal*
    | quoted
    ;

cb_lc_rvalue
    :
    normal*
    | quoted
    ;

// cb category reference
cb_catref
    :
    CATREF COLON LBRACK cb_catref_model cb_catref_path cb_catref_node RBRACK
    ;

cb_catref_model
    :
    CATREF_MODEL COLON PHRASE
    ;

cb_catref_path
    :
    CATREF_PATH COLON PHRASE
    ;

cb_catref_node
    :
    CATREF_NODE COLON PHRASE
    ;

// cb category rollup
cb_catrollup
    :
    CATROLLUP COLON cb_catrollup_id
    ;

cb_catrollup_id
    :
    normal | quoted
    ;


value
    :
    normal
    | truncated
    | quoted
    | quoted_truncated
    | range_term_incl
    | range_term_excl
    | cb_period_func
    | cb_hour_period_func
    | cb_current_period_func
    | QMARK
    | STAR COLON
    | STAR
    ;

range_term_incl
    :
    LBRACK range_values RBRACK
    ;

range_term_excl
    :
    LCURLY range_values RCURLY
    ;

range_values
    :
    range_value TO? range_value
    ;

range_value
    :
    normal
    | truncated
    | quoted
    | quoted_truncated
    | date
    | cb_date_func
    | cb_dateadd_func
    | STAR
    ;

cb_date_func
    :
    DATE_SIMPLE LPAREN RPAREN
    ;

cb_dateadd_func
    :
    DATE_ADD LPAREN DATE_MATH_UNIT COMMA modifier? NUMBER RPAREN
    ;

cb_period_func
    :
    DATE_PERIOD LPAREN DATE_MATH_UNIT COMMA modifier? NUMBER RPAREN
    ;

cb_hour_period_func
    :
    HOUR_PERIOD LPAREN modifier? NUMBER RPAREN
    ;

cb_current_period_func
    :
    CURRENT_PERIOD LPAREN DATE_MATH_UNIT RPAREN
    ;


normal
    :
    TERM_NORMAL
    | NUMBER
    | DATE_MATH_UNIT
    | NOT
    ;

truncated
    :
    TERM_TRUNCATED
    ;

quoted_truncated
    :
    PHRASE_ANYTHING
    ;

quoted
    :
    PHRASE
    ;

operator
    :
    AND
    | OR
    | NOT
    ;

modifier
    :
    PLUS
    | MINUS
    ;

term_modifier
    :
    TILDE
    | CARAT
    ;

date
    :
    DATE_TOKEN
    ;

//all_tokens
//    :
//    TO
//    | LC
//    | CATREF
//    | CATREF_PATH
//    | CATROLLUP
//    | AND
//    | AND1
//    | OR1
//    | NOT
//    | DATE_SIMPLE
//    | DATE_ADD
//    | DATE_PERIOD
//    | HOUR_PERIOD
//    | CURRENT_PERIOD
//    ;

/* lexical rules */


LPAREN
    :
    '('
    ;

RPAREN
    :
    ')'
    ;

LBRACK
    :
    '['
    ;

RBRACK
    :
    ']'
    ;

COLON
    :
    ':'
    ;

PLUS
    :
    '+'
    ;

MINUS
    :
    ('-'|'!')
    ;

STAR
    :
    '*'
    ;

QMARK
    :
    '?'+
    ;

fragment VBAR
    :
    '|'
    ;

fragment AMPER
    :
    '&'
    ;

COMMA
    :
    ','
    ;

LCURLY
    :
    '{'
    ;

RCURLY
    :
    '}'
    ;

CARAT
    :
    '^' (INT+ ('.' INT+)?)?
    ;

TILDE
    :
    '~' (INT+ ('.' INT+)?)?
    ;

DQUOTE
    :
    '\"'
    ;

/*
SQUOTE
    :
    '\''
    ;
*/

// ? why not upcase

TO
    :
    'TO'
    ;

LC
    :
    '_' ('l' | 'L') ('c' | 'C')
    ;

CATREF
    :
    '_' ('c' | 'C') ('a' | 'A') ('t' | 'T') ('r' | 'R') ('e' | 'E') ('f' | 'F')
    ;

CATREF_MODEL
    :
    'model'
    ;

CATREF_PATH
    :
    'path'
    ;

CATREF_NODE
    :
    'node'
    ;

CATROLLUP
    :
    '_' ('c' | 'C') ('a' | 'A') ('t' | 'T') ('r' | 'R') ('o' | 'O') ('l' | 'L') ('l' | 'L') ('u' | 'U') ('p' | 'P')
    ;

AND
    :
    (AND1 | (AMPER AMPER?))
    ;

AND1
    :
    ('a' | 'A') ('n' | 'N') ('d' | 'D')
    ;

OR
    :
    (OR1 | (VBAR VBAR?))
    ;

OR1
    :
    ('o' | 'O') ('r' | 'R')
    ;

NOT
    :
    ('n' | 'N') ('o' | 'O') ('t' | 'T')
    ;

WS
    :
    (
        ' '
        | '\t'
        | '\r'
        | '\n'
        | '\u3000'
        | '\u000C'
    ) -> skip
    ;

DATE_SIMPLE
    :
    '_DATE'
    ;

DATE_ADD
    :
    '_DATEADD'
    ;

DATE_PERIOD
    :
    '_PERIOD'
    ;

HOUR_PERIOD
    :
    '_HOUR_PERIOD'
    ;

CURRENT_PERIOD
    :
    '_CURRENTPERIOD'
    ;


DATE_MATH_UNIT
    :
    ( 'd' | 'D' | 'w' | 'W' | 'm' | 'M' | 'q' | 'Q' | 'y' | 'Y' )
    ;


fragment INT
    :
    '0' .. '9'
    ;

fragment ESC_CHAR
    :
    '\\' .
    ;

fragment TERM_START_CHAR
	:
	(~(' ' | '\t' | '\n' | '\r' | '\u3000'
	    | '\'' | '\"'
	    | '(' | ')' | '[' | ']' | '{' | '}'
	    | '+' | '-' | '!' | ':' | '~' | '^'
	    | '?' | '*' | '\\' | ','
	    )
	    | ESC_CHAR)
	;

fragment TERM_CHAR
	:
	(TERM_START_CHAR | '-' | '+' | '\'')
	;

NUMBER
    :
    ('-')? INT+ ('.' INT+)?
    ;	

DATE_TOKEN
    :
    INT INT? ('/'|'-'|'.') INT INT? ('/'|'-'|'.') INT INT (INT INT)?
    ;

TERM_NORMAL
    :
    TERM_START_CHAR ( TERM_CHAR )*
    ;

TERM_TRUNCATED
    :
    (STAR|QMARK) (TERM_CHAR+ (QMARK|STAR))+ (TERM_CHAR)*
    | TERM_START_CHAR (TERM_CHAR* (QMARK|STAR))+ (TERM_CHAR)*
    | (STAR|QMARK) TERM_CHAR+
    ;

PHRASE
    :
    DQUOTE (ESC_CHAR|~('\"'|'\\'|'?'|'*'))+ DQUOTE
    ;

PHRASE_ANYTHING
    :
    DQUOTE (ESC_CHAR|~('\"'|'\\'))+ DQUOTE
    ;
