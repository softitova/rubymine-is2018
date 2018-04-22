grammar Expression;

parse returns[Boolean result]
    :  expression EOF                         { $result = $expression.val; }
    ;

expression returns[Boolean val]
    :  or                                     { $val = $or.val; }
    ;

or returns [Boolean val]
    :   and OR t = or                         { $val = $and.val || $t.val; }
    |   and                                   { $val = $and.val; }
    ;

helpOr returns [Boolean val]
    : or                                      { $val = $or.val; }
    ;

and returns [Boolean val]
    :   not AND t = and                       { $val = $not.val && $t.val; }
    |   not                                   { $val = $not.val; }
    ;

not returns [Boolean val]
    :   NOT expr                               { $val = !$expr.val; }
    |   expr                                   { $val = $expr.val; }
    ;

expr returns [Boolean val]
    :   l = rVal Operation r = rVal
    {

        $val = (($Operation.text.equals("==") && $l.val == $r.val) ||
                ($Operation.text.equals("!=") && $l.val != $r.val) ||
                ($Operation.text.equals("<") && $l.val < $r.val) ||
                ($Operation.text.equals("<=") && $l.val <= $r.val) ||
                ($Operation.text.equals(">") && $l.val > $r.val) ||
                ($Operation.text.equals(">=") && $l.val >= $r.val));

        System.out.println($l.val + " " + $r.val);
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

rVal returns [int val]
    :term expr1[$term.val]                     { $val = $expr1.val; }
    ;


expr1[int i] returns [int val]
    : '+' term t = expr1[$i + $term.val]           { $val = $t.val; }
    | '-' term t = expr1[$i - $term.val]           { $val = $t.val; }
    |                                          {
    System.out.println($i + " i in expr 1 shoul be sum");
    $val = $i; }
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

Operation : '<' | '>' | '==' | '<=' | '>=' |  '!=';
WS : [ \t\r\n\u000C]+ -> skip;
AND : 'and';
OR  : 'or';
NOT : 'not';

