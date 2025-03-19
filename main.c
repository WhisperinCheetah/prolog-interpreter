#include <stdio.h>
#include <assert.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdbool.h>

#include "parser.h"
#include "term.h"
#include "unifier.h"

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

	TermDatabase* db = parse(file);

	if (db == NULL) {
		fprintf(stderr, "Something went wrong while parsing terms and creating db\n");
		return 1;
	}

	print_database(db);
	
	char buffer[257];
	buffer[0] = '\0';
	while (strcmp(buffer, "stop") != 0) {
		fgets(buffer, 256, stdin);
		buffer[strlen(buffer)-1] = '\0'; // remove newline

		if (buffer[0] == '?' && buffer[1] == '-') {
			run_query(&buffer[2], db);
		}
	}

	return 0;
}
