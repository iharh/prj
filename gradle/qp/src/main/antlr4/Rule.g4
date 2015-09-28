grammar Rule;

/* parser rules */

cb_rule
    :
    clause_or+ EOF
    ;

clause_or
    :
    clause_and ((OR|COMMA+)? clause_and)*
    ;

clause_and
    :
    clause_not (AND clause_not)*
    ;

clause_not
    :
    clause_basic (NOT clause_basic)*
    ;

clause_basic
	:
	modifier? LPAREN clause_or+ RPAREN term_modifier?
	| LPAREN clause_or+ RPAREN
	| atom
	;

atom
	:
	modifier? cb_lc
	| modifier? cb_catref
	| modifier? field multi_value term_modifier?
	| modifier? field? value term_modifier?
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

field
	:
	TERM_NORMAL COLON
	;

value
	:
	range_term_incl
	| range_term_excl
	| normal
	| truncated
	| quoted
	| quoted_truncated
	| cb_period_func
	| cb_hour_period_func
	| cb_current_period_func
	| QMARK
	| STAR COLON
    | STAR
    ;

range_term_incl
	:
    LBRACK range_value TO? range_value RBRACK
	;

range_term_excl
	:
    LCURLY range_value TO? range_value RCURLY
	;

range_value
	:
	truncated
	| quoted
	| quoted_truncated
	| date
	| normal
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

multi_value
	:
	LPAREN multi_clause RPAREN
	;

multi_clause
	:
	clause_or+
	;

multi_default
	:
	multi_or+
	;

multi_or
	:
	multi_and ((OR|COMMA+)? multi_and)*
	;

multi_and
	:
	multi_not (AND multi_not)*
	;

multi_not
	:
	multi_basic (NOT multi_basic)*
	;

multi_basic
	:
	mterm
	;

mterm
	:
	modifier? value
	;

normal
	:
	TERM_NORMAL
	| NUMBER
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
    TILDE CARAT?
    | CARAT TILDE?
    ;

date
    :
	DATE_TOKEN
	;

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
    ('-'|'\\!')
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

SQUOTE
    :
    '\''
    ;

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

AND
    :
    (('a' | 'A') ('n' | 'N') ('d' | 'D') | (AMPER AMPER?))
    ;

OR
    :
    (('o' | 'O') ('r' | 'R') | (VBAR VBAR?))
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
	(TERM_START_CHAR | '-' | '+')
	;

NUMBER
	:
	INT+ ('.' INT+)?
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