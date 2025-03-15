

/*
Feiten:
- kleine letter
- objecten gescheiden door komma
- afsluiten met punt
likes(john,mary).
waardevol(goud).
vrouw(jane).

Variabelen:
- hoofdletter
- opvraging -> markeer unificatie 
- meerdere opvragingen -> verder van laatste markering
X
likes(john,X).

AND:
- ',' buiten een feit
- interne werking -> zoek eerste deel van conjunctie, unificeer, ga verder

Regels (:-):
likes(john,X) :- likes(X,wine).

Terms
- complex
- simple
-- variabelen
-- constanten
--- atomen (strings)
--- nummers

Atomen:
- moet beginnen met een kleine letter
Variabelen:
- begint met hoofdletter/underscore
Complexe termen:
- <functior>(<argumenten), argumenten zijn termen


 */

#include <stdio.h>
#include <assert.h>
#include <ctype.h>
#include <stdlib.h>
#include <a3clib.h>

char* read_entire_file(FILE* file, int* program_size) {
	if (!file) {
		return NULL;
	}

	int size;
	fseek(file, 0, SEEK_END); // goto eof
	size = ftell(file);
	rewind(file);

	*program_size = size;
	
	char* buffer = calloc(sizeof(char), size + 1); // '\0'
	if (!buffer) return NULL;

	if (1 != fread(buffer, size, 1, file)) { // read 1 object of size 'size'
		return NULL;
	}

	return buffer;
}

void clear_whitespace(char* str, int* size) {
	char* buffer = calloc(sizeof(char), *size + 1);

	int whitespace_count = 0;
	for (int i = 0; i < *size; i++) {
		if (isspace(str[i])) {
			whitespace_count++;
		} else {
			buffer[i - whitespace_count] = str[i];
		}
	}

	printf("Removed whitespace\n");
	printf("Original buffer: %s\n", str);
	printf("New buffer: %s\n", buffer);

	int new_size = *size - whitespace_count;
	
	*size = new_size;
	str = realloc(str, new_size + 1); // '\0'
	strncpy(str, buffer, new_size);
}

int count_dots(char* program) {
	int count = 0;
	int i = 0;
	while (program[i] != '\0') {
		if (program[i] == '.') count++;
		i++;
	}

	return count;
}

int distance_to_next_dot(char* str) {
	int dist = 0;
	while (str[dist] != '.') {
		dist++;
	}

	return dist;
}

char** split_by_dot(char* program) {
	int term_count = count_dots(program);
	printf("%d\n", term_count);
	char** term_array = malloc(sizeof(char*) * (term_count + 1));

	int term_start_pos = 0;
	for (int i = 0; i < term_count; i++) {
		int term_length = distance_to_next_dot(&program[term_start_pos]);
		printf("%d, %d\n", term_start_pos, term_length);
		char* term = strndup(&program[term_start_pos], term_length);

		term_start_pos += term_length + 1;
		term_array[i] = term;
	}

	term_array[term_count] = NULL;

	return term_array;
}

int main(int argc, char** argv) {
	if (argc != 2) {
		fprintf(stderr, "At least 1 argument expected, exiting\n");
		return 1;
	}

	FILE* file = fopen(argv[1], "r");

	if (!file) {
		fprintf(stderr, "Could not open file %s\n", argv[1]);
		return 1;
	}

	int program_size;
	char* program = read_entire_file(file, &program_size);

	if (!program) {
		fprintf(stderr, "Something went wrong while reading file\n");
		fclose(file);
		return 1;
	}

	clear_whitespace(program, &program_size);
	char** terms = split_by_dot(program);

	printf("[\n");
	int i = 0;
	while (terms[i] != NULL) {
		printf("\t%s,\n", terms[i]);
		i++;
	}
	printf("]\n");
	
	return 0;
}
