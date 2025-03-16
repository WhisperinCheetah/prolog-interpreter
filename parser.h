#include <stdio.h>
#include <assert.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

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
Term** parse(FILE* file, int* term_count);
