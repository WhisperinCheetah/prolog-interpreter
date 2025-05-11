#include <stdio.h>
#include <assert.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

#include "term.h"

Term* parse_term(char* start);
TermDatabase* parse(FILE* file);
