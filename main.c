#include <stdio.h>
#include <assert.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdbool.h>

#include "parser.h"

bool termcmp(Term* t1, Term* t2) {
	if (t1->type != t2->type) {
		return false;
	}
	
	switch(t1->type) {
	case ATOM:
		return strcmp(t1->atom.name, t2->atom.name) == 0;
	case NUMBER:
		return t1->number.value == t2->number.value;
	case VARIABLE:
		return strcmp(t1->variable.name, t2->variable.name) == 0;
	case STRUCTURE:
		if (
			strcmp(t1->structure.functor, t2->structure.functor) == 0
			&& t1->structure.arity == t2->structure.arity
			) {
			
			for (int i = 0; i < t1->structure.arity; i++) {
				if (!termcmp(t1->structure.args[i], t2->structure.args[i])) {
					return false;
				}
			}

			return true;
		}
	}

	return false;
}

Term* unify_db_term(Term* dbterm, Term* query) {
	
}

bool resolve(Term* query, Term** db, int db_size) {
	if (query->type != STRUCTURE) {
		return false;
	}
	
	for (int i = 0; i < db_size; i++) {
		Term* term = db[i];
		
		if (termcmp(term, query)) {
			return true;
		}
	}

	return false;
}

bool run_query(char* query_str, Term** db, int db_size) {
	Term* query = parse_term(query_str);

	return resolve(query, db, db_size);
}

int main(int argc, char** argv) {
	if (argc != 2) {
		fprintf(stderr, "At least 1 argument expected, exiting\n");
		return 2;
	}

	FILE* file = fopen(argv[1], "r");

	if (!file) {
		fprintf(stderr, "Could not open file %s\n", argv[1]);
		return 2;
	}

	int term_count;
	Term** terms = parse(file, &term_count);

	if (terms == NULL) {
		fprintf(stderr, "Something went wrong while parsing terms\n");
		return 1;
	}

	char buffer[257];
	buffer[0] = '\0';
	while (strcmp(buffer, "stop") != 0) {
		fgets(buffer, 256, stdin);
		buffer[strlen(buffer)-1] = '\0'; // remove newline

		if (buffer[0] == '?' && buffer[1] == '-') {
			if (run_query(&buffer[2], terms, term_count)) {
				printf("true\n");
			} else {
				printf("false\n");
			}
		}
	}

	return 0;
}
