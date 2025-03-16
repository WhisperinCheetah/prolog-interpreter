#include <stdio.h>
#include <assert.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

#include "term.h"

Term* parse_term(char* start);
Term** parse(FILE* file, int* term_count);
