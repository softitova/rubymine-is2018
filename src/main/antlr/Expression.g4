grammar Expression;

parse returns[Boolean result]
    :  expression EOF                         { $result = $expression.val; }
    ;

expression returns[Boolean val]
    :  or                                     { $val = $or.val; }
    ;

or returns [Boolean val]
    :   and OR helpOr                         { $val = $and.val || $helpOr.val; }
    |   and                                   { $val = $and.val; }
    ;

helpOr returns [Boolean val]
    : or                                      { $val = $or.val; }
    ;

and returns [Boolean val]
    :   not AND helpAnd                       { $val = $not.val && $helpAnd.val; }
    |   not                                   { $val = $not.val; }
    ;

helpAnd  returns [Boolean val]
    : and {$val = $and.val;}
    ;

not returns [Boolean val]
    :  NOT expr                               { $val = !$expr.val; }
    |  expr                                   { $val = $expr.val; }
    ;

expr returns [Boolean val]
    :  l Operation r
    {
        $val = (($Operation.text.equals("==") && $l.val == $r.val) ||
                ($Operation.text.equals("<") && $l.val < $r.val) ||
                ($Operation.text.equals(">") && $l.val > $r.val));
    }
    |  Boolean
    {
        if($Boolean.text.equals("True")) {
            $val = true;
        } else {
            $val = false;
        }
    }
    | '(' expression ')'                       { $val = $expression.val; }
    ;


l returns [int val]
    : rVal {$val = $rVal.val;};

r  returns [int val]
    : rVal {$val = $rVal.val;};

rVal returns [int val]
    :term expr1[$term.val]                     { $val = $expr1.val; }
    ;


expr1[int i] returns [int val]
    : '+' term expr1[$i + $term.val]           { $val = $expr1.val; }
    | '-' term expr1[$i - $term.val]           { $val = $expr1.val; }
    |                                          { $val = $i; }
    ;


term returns [int val]
    : fact expr2[$fact.val]                    { $val = $expr2.val; }
    ;


expr2[int i] returns [int val]
    : '*' fact expr1[$i * $fact.val]           { $val = $expr1.val; }
    | '/' fact expr1[$i / $fact.val]           { $val = $expr1.val; }
    |                                          { $val = $i; }
    ;


fact returns [int val]
    : unaryOp                                  { $val = $unaryOp.val; }
    |'(' rVal ')'                              { $val = $rVal.val; }
//    | Variable                                       { $val = $map.get($Variable.text); }
    | Number                                   { $val = Integer.parseInt($Number.text); }
    ;


unaryOp returns [int val]
    : '-' fact                                 { $val = (-1) * $fact.val; }
    | '+' fact                                 { $val = $fact.val; }
    ;

Boolean : 'True' | 'False';
Number:  Digit+;
Digit : [0-9$];
//Variable: Letter LetterOrDigit*;
//Letter: [a-zA-Z$_];
//LetterOrDigit: [a-zA-Z0-9$_] ;
//operation : G | S | E | NE | GE | SE;

Operation : '<' | '>' | '==' ;
WS : [ \t\r\n\u000C]+ -> skip;
AND : 'and';
OR  : 'or';
NOT : 'not';

