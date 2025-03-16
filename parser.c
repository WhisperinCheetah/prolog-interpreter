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
Nummers:
- nummer
Variabelen:
- begint met hoofdletter/underscore
Complexe termen:
- <functior>(<argumenten), argumenten zijn termen


 */

#include <stdio.h>
#include <assert.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

#include "parser.h"

char* read_entire_file(FILE* file) {
	if (!file) {
		return NULL;
	}

	int size;
	fseek(file, 0, SEEK_END); // goto eof
	size = ftell(file);
	rewind(file);
	
	char* buffer = calloc(sizeof(char), size + 1); // '\0'
	if (!buffer) return NULL;

	if (1 != fread(buffer, size, 1, file)) { // read 1 object of size 'size'
		return NULL;
	}

	return buffer;
}

void clear_whitespace(char* str) {
	int size = strlen(str);
	char* buffer = calloc(sizeof(char), size + 1);

	int whitespace_count = 0;
	for (int i = 0; i < size; i++) {
		if (isspace(str[i])) {
			whitespace_count++;
		} else {
			buffer[i - whitespace_count] = str[i];
		}
	}

	int new_size = size - whitespace_count;
	
	str = realloc(str, new_size + 1); // '\0'
	strcpy(str, buffer); // strncpy causes a bug ??
}

int count_char(char* program, char c) {
	int count = 0;
	int i = 0;
	while (program[i] != '\0') {
		if (program[i] == c) {
			count++;
		}
		i++;
	}

	return count;
}

// TODO: floating point values (check if between '(' and ')')
int distance_to_next_char(char* str, char c) {
	int dist = 0;
	while (str[dist] != c) {
		dist++;
	}

	return dist;
}

char** split_by_dot(char* program, int* line_count) {
	int term_count = count_char(program, '.');
	char** term_array = malloc(sizeof(char*) * term_count);
	*line_count = term_count;

	int term_start_pos = 0;
	for (int i = 0; i < term_count; i++) {
		int term_length = distance_to_next_char(&program[term_start_pos], '.');
		char* term = strndup(&program[term_start_pos], term_length);

		term_start_pos += term_length + 1;
		term_array[i] = term;
	}

	return term_array;
}



void print_term_name(Term* term) {
	switch(term->type) {
	case ATOM:
		printf("%s", term->atom.name);
		break;
	case NUMBER:
		printf("%f", term->number.value);
		break;
	case VARIABLE:
		printf("%s", term->variable.name);
		break;
	case STRUCTURE:
		printf("%s(", term->structure.functor);
		for (int i = 0; i < term->structure.arity; i++) {
			print_term_name(term->structure.args[i]);
			printf(",");
		}
		printf(")");
		break;
	}
}

void print_term(Term* term) {
	switch (term->type) {
	case ATOM:
		printf("Atom: %s\n", term->atom.name);
		break;
	case NUMBER:
		printf("Number: %f\n", term->number.value);
		break;
	case VARIABLE:
		printf("Variable: %s\n", term->variable.name);
		break;
	case STRUCTURE:
		printf("Structure: %s(", term->structure.functor);
		for (int i = 0; i < term->structure.arity; i++) {
			print_term_name(term->structure.args[i]);
			printf(",");
		}
		printf(")/%d\n", term->structure.arity);
		break;
    }
}

char* read_until_bracket(char* str) {
	int offset = 0;
	while (str[offset] != '(' && str[offset] != ')') {
		offset++;
	}

	char* buffer = calloc(sizeof(char), offset+1);
	memcpy(buffer, str, offset);
	buffer[offset+1] = '\0';

	return buffer;
}

char* read_name(char* start) {
	int offset = 0;
	while (
		   start[offset] != '('
		   && start[offset] != ')'
		   && start[offset] != ','
		   && start[offset] != '\0'
		   ) {
		offset++;
	}

	char* buffer = calloc(sizeof(char), offset+1);
	memcpy(buffer, start, offset);
	buffer[offset+1] = '\0';

	return buffer;
}

bool isatom(char* start) {
	int i = 0;
	while (start[i] != '\0') {
		if (start[i] == '(') {
			return false;
		} else if (start[i] == ')' || start[i] == ',') {
			return true;
		}
		i++;
	}

	return true;
}

int count_char_between_brackets(char* program, char c) {
	int count = 0;
	int i = 0;
	while (program[i] != '\0' && program[i] != '(' && program[i] != ')') {
		if (program[i] == c) {
			count++;
		}
		i++;
	}

	return count;
}

Term* parse_term_no_whitespace(char*);

Term** parse_structure_args(char* start, int* argc) {
	*argc = count_char_between_brackets(start, ',') + 1;
	Term** argv = calloc(sizeof(Term*), *argc);

	char* arg_start_char = start;
	for (int i = 0; i < *argc; i++) {
		argv[i] = parse_term_no_whitespace(arg_start_char);
		arg_start_char += distance_to_next_char(start, ',') + 1;
	}

	return argv;
}

Term* parse_term_no_whitespace(char* start) {
	Term* term = calloc(sizeof(Term), 1);
	if (isupper(start[0])) {
		term->type = VARIABLE;
		term->variable.name = read_name(start);
	} else if (islower(start[0])) {
		if (isatom(start)) {
			// parse atom
			term->type = ATOM;
			term->atom.name = read_name(start);
		} else {
			// parse complex term
			int argc;
			Term** argv =
				parse_structure_args(start + distance_to_next_char(start, '(') + 1, &argc);
			
			term->type = STRUCTURE;
			term->structure.functor = read_name(start);
			term->structure.args = argv;
			term->structure.arity = argc;
		}
	} else {
		// parse number
		term->type = NUMBER;
		term->number.value = atof(start);
	}

	return term;
}

Term* parse_term(char* start) {
	int length = strlen(start);
	char* copy = malloc(sizeof(char) * (length+1));
	strcpy(copy, start);

	clear_whitespace(copy);
	
	Term* term = parse_term_no_whitespace(copy);

	free(copy);

	return term;
}

Term** parse_lines(char** lines, int line_count) {
	Term** terms = calloc(sizeof(Term*), line_count);
	for (int i = 0; i < line_count; i++) {
		terms[i] = parse_term_no_whitespace(lines[i]);
	}
	return terms;
}

Term** parse(FILE* file, int* term_count) {
	char* program = read_entire_file(file);

	if (!program) {
		fprintf(stderr, "Something went wrong while reading file\n");
		fclose(file);
		return NULL;
	}

	clear_whitespace(program);

	int line_count;
	char** lines = split_by_dot(program, &line_count);

	*term_count = line_count;

	printf("[\n");
	for (int i = 0; i < line_count; i++) {
		printf("\t%s,\n", lines[i]);
	}
	printf("]\n");

	Term** terms = parse_lines(lines, line_count);

	for (int i = 0; i < line_count; i++) {
		print_term(terms[i]);
	}
	
	return terms;
}
