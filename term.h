#ifndef _TERM_H_
#define _TERM_H_

typedef enum { ATOM, NUMBER, VARIABLE, STRUCTURE } TermType;

typedef struct Term {
    TermType type;
    union {
        struct { char *name; } atom;
        struct { double value; } number;
        struct { char *name; } variable;
        struct { char *functor; struct Term **args; int arity; } structure;
    };
} Term;

void print_term(Term* term);
void copy_term(Term* dest, Term* src);
Term* duplicate_term(Term* src);

#endif
