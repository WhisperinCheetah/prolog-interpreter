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

bool isrule(char* line) {
	int i = 1;

	if (line[0] == '\0') return false;
	
	while (line[i] != '.' && line[i] != '\0') {
		if (line[i-1] == ':' && line[i] == '-') {
			return true;
		}
		i++;
	}
	return false;
}

int count_body_terms(char* start) {
	int count = 0;
	int i = 0;
	while (start[i] != '.' && start[i] != '\0') {
		if (start[i] == ',') {
			count++;
		}

		if (start[i] == '(') {
			i += distance_to_next_char(&start[i], ')');
		}

		i++;
	}

	return count;
}

Term** parse_rule_body(char* start, int* body_count) {
	int term_count = count_body_terms(start);
	*body_count = term_count;

	Term** body = calloc(sizeof(Term*), term_count);
	
	bool ended = false;
	while (!ended) {
		ended = true;
	}
	Term* body_term = parse_term_no_whitespace(start);
	body[0] = body_term;
	// BodyNode* body = calloc(sizeof(BodyNode), 1);
	// body->term = body_term;
	// body->left = NULL;
	// body->right = NULL;

	return body;
}

Rule* parse_rule(char* start) {
	printf("parsing rule: %s\n", start);

	int body_count;
	Term* head = parse_term_no_whitespace(start);
	Term** body = parse_rule_body(start + distance_to_next_char(start, '-') + 1, &body_count);
	
	Rule* rule = calloc(sizeof(Rule), 1);
	rule->head = head;
	rule->body = body;
	rule->body_count = body_count;
	
	return rule;
}

void parse_lines(TermDatabase* db, char** lines, int line_count) {
	for (int i = 0; i < line_count; i++) {
		if (isrule(lines[i])) {
			Rule* rule = parse_rule(lines[i]);
			add_rule_to_db(db, rule);
		} else {
			Term* term = parse_term_no_whitespace(lines[i]);
			add_term_to_db(db, term);
		}
	}
}

TermDatabase* parse(FILE* file) {
	char* program = read_entire_file(file);

	if (!program) {
		fprintf(stderr, "Something went wrong while reading file\n");
		fclose(file);
		return NULL;
	}

	clear_whitespace(program);

	int line_count;
	char** lines = split_by_dot(program, &line_count);

	printf("[\n");
	for (int i = 0; i < line_count; i++) {
		printf("\t%s,\n", lines[i]);
	}
	printf("]\n");

	TermDatabase* db = create_database();
	parse_lines(db, lines, line_count);
	create_candidates(db);
	
	return db;
}
