#include <stdio.h>
#include <string.h>
#include <stdlib.h>

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

	char* name_copy;
	switch(src->type) {
	case ATOM:
		name_copy = strdup(src->atom.name);
		dest->atom.name = name_copy;
		break;
	case VARIABLE:
		name_copy = strdup(src->variable.name);
		dest->variable.name = name_copy;
		break;
	case NUMBER:
		dest->number.value = src->number.value;
		break;
	case STRUCTURE:
		name_copy = strdup(src->structure.functor);
		Term** arg_list = calloc(sizeof(Term*), src->structure.arity);
		for (int i = 0; i < src->structure.arity; i++) {
			arg_list[i] = duplicate_term(src->structure.args[i]);
		}

		dest->structure.functor = name_copy;
		dest->structure.args = arg_list;
		dest->structure.arity = src->structure.arity;
		break;
	}
}

Term* duplicate_term(Term* src) {
	Term* dup = calloc(sizeof(Term), 1);
	copy_term(dup, src);

	return dup;
}

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
