/* Generated By:JJTree&JavaCC: Do not edit this line. SyntaxTreeBuilderConstants.java */
package org.eclipse.rdf4j.query.parser.sparql.ast;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface SyntaxTreeBuilderConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int WS_CHAR = 1;
  /** RegularExpression Id. */
  int WHITESPACE = 2;
  /** RegularExpression Id. */
  int SINGLE_LINE_COMMENT = 3;
  /** RegularExpression Id. */
  int LPAREN = 4;
  /** RegularExpression Id. */
  int RPAREN = 5;
  /** RegularExpression Id. */
  int LBRACE = 6;
  /** RegularExpression Id. */
  int RBRACE = 7;
  /** RegularExpression Id. */
  int LBRACK = 8;
  /** RegularExpression Id. */
  int RBRACK = 9;
  /** RegularExpression Id. */
  int SEMICOLON = 10;
  /** RegularExpression Id. */
  int COLON = 11;
  /** RegularExpression Id. */
  int COMMA = 12;
  /** RegularExpression Id. */
  int DOT = 13;
  /** RegularExpression Id. */
  int EQ = 14;
  /** RegularExpression Id. */
  int NE = 15;
  /** RegularExpression Id. */
  int GT = 16;
  /** RegularExpression Id. */
  int LT = 17;
  /** RegularExpression Id. */
  int LE = 18;
  /** RegularExpression Id. */
  int GE = 19;
  /** RegularExpression Id. */
  int NOT = 20;
  /** RegularExpression Id. */
  int OR = 21;
  /** RegularExpression Id. */
  int AND = 22;
  /** RegularExpression Id. */
  int PLUS = 23;
  /** RegularExpression Id. */
  int MINUS = 24;
  /** RegularExpression Id. */
  int STAR = 25;
  /** RegularExpression Id. */
  int QUESTION = 26;
  /** RegularExpression Id. */
  int SLASH = 27;
  /** RegularExpression Id. */
  int PIPE = 28;
  /** RegularExpression Id. */
  int INVERSE = 29;
  /** RegularExpression Id. */
  int DT_PREFIX = 30;
  /** RegularExpression Id. */
  int NIL = 31;
  /** RegularExpression Id. */
  int ANON = 32;
  /** RegularExpression Id. */
  int IS_A = 33;
  /** RegularExpression Id. */
  int BASE = 34;
  /** RegularExpression Id. */
  int PREFIX = 35;
  /** RegularExpression Id. */
  int SELECT = 36;
  /** RegularExpression Id. */
  int CONSTRUCT = 37;
  /** RegularExpression Id. */
  int DESCRIBE = 38;
  /** RegularExpression Id. */
  int ASK = 39;
  /** RegularExpression Id. */
  int DISTINCT = 40;
  /** RegularExpression Id. */
  int REDUCED = 41;
  /** RegularExpression Id. */
  int AS = 42;
  /** RegularExpression Id. */
  int FROM = 43;
  /** RegularExpression Id. */
  int NAMED = 44;
  /** RegularExpression Id. */
  int WINDOW = 45;
  /** RegularExpression Id. */
  int ON = 46;
  /** RegularExpression Id. */
  int RANGE = 47;
  /** RegularExpression Id. */
  int STEP = 48;
  /** RegularExpression Id. */
  int WHERE = 49;
  /** RegularExpression Id. */
  int ORDER = 50;
  /** RegularExpression Id. */
  int GROUP = 51;
  /** RegularExpression Id. */
  int BY = 52;
  /** RegularExpression Id. */
  int ASC = 53;
  /** RegularExpression Id. */
  int DESC = 54;
  /** RegularExpression Id. */
  int LIMIT = 55;
  /** RegularExpression Id. */
  int OFFSET = 56;
  /** RegularExpression Id. */
  int OPTIONAL = 57;
  /** RegularExpression Id. */
  int GRAPH = 58;
  /** RegularExpression Id. */
  int UNION = 59;
  /** RegularExpression Id. */
  int MINUS_SETOPER = 60;
  /** RegularExpression Id. */
  int FILTER = 61;
  /** RegularExpression Id. */
  int HAVING = 62;
  /** RegularExpression Id. */
  int EXISTS = 63;
  /** RegularExpression Id. */
  int NOT_EXISTS = 64;
  /** RegularExpression Id. */
  int STR = 65;
  /** RegularExpression Id. */
  int LANG = 66;
  /** RegularExpression Id. */
  int LANGMATCHES = 67;
  /** RegularExpression Id. */
  int DATATYPE = 68;
  /** RegularExpression Id. */
  int BOUND = 69;
  /** RegularExpression Id. */
  int SAMETERM = 70;
  /** RegularExpression Id. */
  int IS_IRI = 71;
  /** RegularExpression Id. */
  int IS_BLANK = 72;
  /** RegularExpression Id. */
  int IS_LITERAL = 73;
  /** RegularExpression Id. */
  int IS_NUMERIC = 74;
  /** RegularExpression Id. */
  int COALESCE = 75;
  /** RegularExpression Id. */
  int BNODE = 76;
  /** RegularExpression Id. */
  int STRDT = 77;
  /** RegularExpression Id. */
  int STRLANG = 78;
  /** RegularExpression Id. */
  int UUID = 79;
  /** RegularExpression Id. */
  int STRUUID = 80;
  /** RegularExpression Id. */
  int IRI = 81;
  /** RegularExpression Id. */
  int IF = 82;
  /** RegularExpression Id. */
  int IN = 83;
  /** RegularExpression Id. */
  int NOT_IN = 84;
  /** RegularExpression Id. */
  int COUNT = 85;
  /** RegularExpression Id. */
  int SUM = 86;
  /** RegularExpression Id. */
  int MIN = 87;
  /** RegularExpression Id. */
  int MAX = 88;
  /** RegularExpression Id. */
  int AVG = 89;
  /** RegularExpression Id. */
  int SAMPLE = 90;
  /** RegularExpression Id. */
  int GROUP_CONCAT = 91;
  /** RegularExpression Id. */
  int SEPARATOR = 92;
  /** RegularExpression Id. */
  int REGEX = 93;
  /** RegularExpression Id. */
  int TRUE = 94;
  /** RegularExpression Id. */
  int FALSE = 95;
  /** RegularExpression Id. */
  int BIND = 96;
  /** RegularExpression Id. */
  int SERVICE = 97;
  /** RegularExpression Id. */
  int BINDINGS = 98;
  /** RegularExpression Id. */
  int VALUES = 99;
  /** RegularExpression Id. */
  int UNDEF = 100;
  /** RegularExpression Id. */
  int STRLEN = 101;
  /** RegularExpression Id. */
  int SUBSTR = 102;
  /** RegularExpression Id. */
  int STR_STARTS = 103;
  /** RegularExpression Id. */
  int STR_ENDS = 104;
  /** RegularExpression Id. */
  int STR_BEFORE = 105;
  /** RegularExpression Id. */
  int STR_AFTER = 106;
  /** RegularExpression Id. */
  int REPLACE = 107;
  /** RegularExpression Id. */
  int UCASE = 108;
  /** RegularExpression Id. */
  int LCASE = 109;
  /** RegularExpression Id. */
  int CONCAT = 110;
  /** RegularExpression Id. */
  int CONTAINS = 111;
  /** RegularExpression Id. */
  int ENCODE_FOR_URI = 112;
  /** RegularExpression Id. */
  int RAND = 113;
  /** RegularExpression Id. */
  int ABS = 114;
  /** RegularExpression Id. */
  int CEIL = 115;
  /** RegularExpression Id. */
  int FLOOR = 116;
  /** RegularExpression Id. */
  int ROUND = 117;
  /** RegularExpression Id. */
  int NOW = 118;
  /** RegularExpression Id. */
  int YEAR = 119;
  /** RegularExpression Id. */
  int MONTH = 120;
  /** RegularExpression Id. */
  int DAY = 121;
  /** RegularExpression Id. */
  int HOURS = 122;
  /** RegularExpression Id. */
  int MINUTES = 123;
  /** RegularExpression Id. */
  int SECONDS = 124;
  /** RegularExpression Id. */
  int TIMEZONE = 125;
  /** RegularExpression Id. */
  int TZ = 126;
  /** RegularExpression Id. */
  int MD5 = 127;
  /** RegularExpression Id. */
  int SHA1 = 128;
  /** RegularExpression Id. */
  int SHA224 = 129;
  /** RegularExpression Id. */
  int SHA256 = 130;
  /** RegularExpression Id. */
  int SHA384 = 131;
  /** RegularExpression Id. */
  int SHA512 = 132;
  /** RegularExpression Id. */
  int TUMBLING = 133;
  /** RegularExpression Id. */
  int TRIPLES = 134;
  /** RegularExpression Id. */
  int PT = 135;
  /** RegularExpression Id. */
  int LOAD = 136;
  /** RegularExpression Id. */
  int CLEAR = 137;
  /** RegularExpression Id. */
  int DROP = 138;
  /** RegularExpression Id. */
  int ADD = 139;
  /** RegularExpression Id. */
  int MOVE = 140;
  /** RegularExpression Id. */
  int COPY = 141;
  /** RegularExpression Id. */
  int CREATE = 142;
  /** RegularExpression Id. */
  int INSERT = 143;
  /** RegularExpression Id. */
  int DATA = 144;
  /** RegularExpression Id. */
  int DELETE = 145;
  /** RegularExpression Id. */
  int WITH = 146;
  /** RegularExpression Id. */
  int SILENT = 147;
  /** RegularExpression Id. */
  int DEFAULT_GRAPH = 148;
  /** RegularExpression Id. */
  int ALL = 149;
  /** RegularExpression Id. */
  int INTO = 150;
  /** RegularExpression Id. */
  int TO = 151;
  /** RegularExpression Id. */
  int USING = 152;
  /** RegularExpression Id. */
  int Q_IRI_REF = 153;
  /** RegularExpression Id. */
  int PNAME_NS = 154;
  /** RegularExpression Id. */
  int PNAME_LN = 155;
  /** RegularExpression Id. */
  int BLANK_NODE_LABEL = 156;
  /** RegularExpression Id. */
  int VAR1 = 157;
  /** RegularExpression Id. */
  int VAR2 = 158;
  /** RegularExpression Id. */
  int LANGTAG = 159;
  /** RegularExpression Id. */
  int INTEGER = 160;
  /** RegularExpression Id. */
  int INTEGER_POSITIVE = 161;
  /** RegularExpression Id. */
  int INTEGER_NEGATIVE = 162;
  /** RegularExpression Id. */
  int DECIMAL = 163;
  /** RegularExpression Id. */
  int DECIMAL_POSITIVE = 164;
  /** RegularExpression Id. */
  int DECIMAL_NEGATIVE = 165;
  /** RegularExpression Id. */
  int DOUBLE = 166;
  /** RegularExpression Id. */
  int DOUBLE1 = 167;
  /** RegularExpression Id. */
  int DOUBLE2 = 168;
  /** RegularExpression Id. */
  int DOUBLE3 = 169;
  /** RegularExpression Id. */
  int EXPONENT = 170;
  /** RegularExpression Id. */
  int DOUBLE_POSITIVE = 171;
  /** RegularExpression Id. */
  int DOUBLE_NEGATIVE = 172;
  /** RegularExpression Id. */
  int STRING_LITERAL1 = 173;
  /** RegularExpression Id. */
  int STRING_LITERAL2 = 174;
  /** RegularExpression Id. */
  int STRING_LITERAL_LONG1 = 175;
  /** RegularExpression Id. */
  int STRING_LITERAL_LONG2 = 176;
  /** RegularExpression Id. */
  int SAFE_CHAR1 = 177;
  /** RegularExpression Id. */
  int SAFE_CHAR2 = 178;
  /** RegularExpression Id. */
  int SAFE_CHAR_LONG1 = 179;
  /** RegularExpression Id. */
  int SAFE_CHAR_LONG2 = 180;
  /** RegularExpression Id. */
  int ECHAR = 181;
  /** RegularExpression Id. */
  int HEX = 182;
  /** RegularExpression Id. */
  int ALPHA = 183;
  /** RegularExpression Id. */
  int NUM = 184;
  /** RegularExpression Id. */
  int PN_CHARS_BASE = 185;
  /** RegularExpression Id. */
  int PN_CHARS_U = 186;
  /** RegularExpression Id. */
  int VAR_CHAR = 187;
  /** RegularExpression Id. */
  int PN_CHARS = 188;
  /** RegularExpression Id. */
  int PN_PREFIX = 189;
  /** RegularExpression Id. */
  int PN_LOCAL = 190;
  /** RegularExpression Id. */
  int PLX = 191;
  /** RegularExpression Id. */
  int PERCENT = 192;
  /** RegularExpression Id. */
  int PN_LOCAL_ESC = 193;
  /** RegularExpression Id. */
  int VARNAME = 194;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "<WS_CHAR>",
    "<WHITESPACE>",
    "<SINGLE_LINE_COMMENT>",
    "\"(\"",
    "\")\"",
    "\"{\"",
    "\"}\"",
    "\"[\"",
    "\"]\"",
    "\";\"",
    "\":\"",
    "\",\"",
    "\".\"",
    "\"=\"",
    "\"!=\"",
    "\">\"",
    "\"<\"",
    "\"<=\"",
    "\">=\"",
    "\"!\"",
    "\"||\"",
    "\"&&\"",
    "\"+\"",
    "\"-\"",
    "\"*\"",
    "\"?\"",
    "\"/\"",
    "\"|\"",
    "\"^\"",
    "\"^^\"",
    "<NIL>",
    "<ANON>",
    "\"a\"",
    "\"base\"",
    "\"prefix\"",
    "\"select\"",
    "\"construct\"",
    "\"describe\"",
    "\"ask\"",
    "\"distinct\"",
    "\"reduced\"",
    "\"as\"",
    "\"from\"",
    "\"named\"",
    "\"window\"",
    "\"on\"",
    "\"range\"",
    "\"step\"",
    "\"where\"",
    "\"order\"",
    "\"group\"",
    "\"by\"",
    "\"asc\"",
    "\"desc\"",
    "\"limit\"",
    "\"offset\"",
    "\"optional\"",
    "\"graph\"",
    "\"union\"",
    "\"minus\"",
    "\"filter\"",
    "\"having\"",
    "\"exists\"",
    "\"not exists\"",
    "\"str\"",
    "\"lang\"",
    "\"langmatches\"",
    "\"datatype\"",
    "\"bound\"",
    "\"sameTerm\"",
    "<IS_IRI>",
    "\"isBlank\"",
    "\"isLiteral\"",
    "\"isNumeric\"",
    "\"coalesce\"",
    "\"bnode\"",
    "\"strdt\"",
    "\"strlang\"",
    "\"uuid\"",
    "\"struuid\"",
    "<IRI>",
    "\"if\"",
    "\"in\"",
    "\"not in\"",
    "\"count\"",
    "\"sum\"",
    "\"min\"",
    "\"max\"",
    "\"avg\"",
    "\"sample\"",
    "\"group_concat\"",
    "\"separator\"",
    "\"regex\"",
    "\"true\"",
    "\"false\"",
    "\"bind\"",
    "\"service\"",
    "\"bindings\"",
    "\"values\"",
    "\"UNDEF\"",
    "\"strlen\"",
    "\"substr\"",
    "\"strStarts\"",
    "\"strEnds\"",
    "\"strBefore\"",
    "\"strAfter\"",
    "\"replace\"",
    "\"ucase\"",
    "\"lcase\"",
    "\"concat\"",
    "\"contains\"",
    "\"encode_for_URI\"",
    "\"rand\"",
    "\"abs\"",
    "\"ceil\"",
    "\"floor\"",
    "\"round\"",
    "\"now\"",
    "\"year\"",
    "\"month\"",
    "\"day\"",
    "\"hours\"",
    "\"minutes\"",
    "\"seconds\"",
    "\"timezone\"",
    "\"tz\"",
    "\"md5\"",
    "\"sha1\"",
    "\"sha224\"",
    "\"sha256\"",
    "\"sha384\"",
    "\"sha512\"",
    "\"tumbling\"",
    "\"triples\"",
    "\"pt\"",
    "\"load\"",
    "\"clear\"",
    "\"drop\"",
    "\"add\"",
    "\"move\"",
    "\"copy\"",
    "\"create\"",
    "\"insert\"",
    "\"data\"",
    "\"delete\"",
    "\"with\"",
    "\"silent\"",
    "\"default\"",
    "\"all\"",
    "\"into\"",
    "\"to\"",
    "\"using\"",
    "<Q_IRI_REF>",
    "<PNAME_NS>",
    "<PNAME_LN>",
    "<BLANK_NODE_LABEL>",
    "<VAR1>",
    "<VAR2>",
    "<LANGTAG>",
    "<INTEGER>",
    "<INTEGER_POSITIVE>",
    "<INTEGER_NEGATIVE>",
    "<DECIMAL>",
    "<DECIMAL_POSITIVE>",
    "<DECIMAL_NEGATIVE>",
    "<DOUBLE>",
    "<DOUBLE1>",
    "<DOUBLE2>",
    "<DOUBLE3>",
    "<EXPONENT>",
    "<DOUBLE_POSITIVE>",
    "<DOUBLE_NEGATIVE>",
    "<STRING_LITERAL1>",
    "<STRING_LITERAL2>",
    "<STRING_LITERAL_LONG1>",
    "<STRING_LITERAL_LONG2>",
    "<SAFE_CHAR1>",
    "<SAFE_CHAR2>",
    "<SAFE_CHAR_LONG1>",
    "<SAFE_CHAR_LONG2>",
    "<ECHAR>",
    "<HEX>",
    "<ALPHA>",
    "<NUM>",
    "<PN_CHARS_BASE>",
    "<PN_CHARS_U>",
    "<VAR_CHAR>",
    "<PN_CHARS>",
    "<PN_PREFIX>",
    "<PN_LOCAL>",
    "<PLX>",
    "<PERCENT>",
    "<PN_LOCAL_ESC>",
    "<VARNAME>",
  };

}
