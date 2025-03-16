#include <stdio.h>

#include "term.h"

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

void copy_term(Term* dest, Term* src) {
	dest->type = src->type;
	
	switch(src->type) {
	case ATOM:
		char* name_copy = strdup(src->atom.name);
		dest->atom.name = name_copy;
		break;
	case VARIABLE:
		char* name_copy = strdup(src->variable.name);
		dest->variable.name = name_copy;
		break;
	case NUMBER:
		dest->number.value = src->number.value;
		break;
	case STRUCTURE:
		char* name_copy = strdup(src->structure.functor);
		Term** arg_list = calloc(sizeof(Term*), src->structure.arity);
	}
}

Term* duplicate_term(Term* src) {
	Term* dup = calloc(sizeof(Term), 1);
	copy_term(dup, src);

	return dup;
}
