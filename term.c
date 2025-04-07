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
	if (term == NULL) {
		printf("Term: NULL\n");
		return;
	}
	
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

void print_rule(Rule* rule) {
	print_term(rule->head);
	printf(" :- ");
	print_term(rule->body[0]);
	for (int i = 1; i < rule->body_count; i++) {
		printf(", ");
		print_term(rule->body[i]);
	}
	printf("\n");
}

Rule* duplicate_rule(Rule* rule) {
	Rule* copy = calloc(sizeof(Rule), 1);
	
	copy->head = duplicate_term(rule->head);

	Term** body_copy = calloc(sizeof(Term*), rule->body_count);
	for (int i = 0; i < rule->body_count; i++) {
		body_copy[i] = duplicate_term(rule->body[i]);
	}
	copy->body = body_copy;
	copy->body_count = rule->body_count;

	return copy;
}

void print_database(TermDatabase* db) {
	printf("TermDatabase {\n");
	printf("\tint term_count = %d\n", db->term_count);
	printf("\tTerm** terms = %p\n\n", db->terms);

	printf("\tint rule_count = %d\n", db->rule_count);
	printf("\tRule** rules = %p\n\n", db->rules);
	
	printf("\tint structure_pair_count = %d\n", db->structure_pair_count);
	printf("\tint structure_pair_size = %d\n", db->structure_pair_size);
	printf("\tStructurePair* pairs = {\n");
	
	for (int i = 0; i < db->structure_pair_count; i++) {
		StructurePair pair = db->structure_pairs[i];
		printf("\t\tStructurePair {\n");
		printf("\t\t\tFunctor = %s/%d\n",
			   pair.structure->structure.functor,
			   pair.structure->structure.arity);
		printf("\t\t\tCandidateList* lists = \n");

		for (int j = 0; j < pair.structure->structure.arity; j++) {
			CandidateList list = pair.candidate_lists[j];

			printf("\t\t\t[");
			for (int k = 0; k < list.count; k++) {
				Term* candidate = list.candidates[k];
				if (candidate->type == ATOM) {
					printf("%s, ", list.candidates[k]->atom.name);
				} else {
					printf("%f, ", list.candidates[k]->number.value);
				}
			}
			printf("],\n");
		}
	}

	printf("}\n");
}

#define INIT_SIZE 10

void init_database(TermDatabase* db) {
	db->term_size = INIT_SIZE;
	db->term_count = 0;
	db->terms = calloc(sizeof(Term*), INIT_SIZE);

	db->rule_size = INIT_SIZE;
	db->term_count = 0;
	db->rules = calloc(sizeof(Term*), INIT_SIZE);
	
	db->structure_pair_count = 0;
	db->structure_pair_size = 0;
	db->structure_pairs = NULL;
}

TermDatabase* create_database() {
	TermDatabase* db = calloc(sizeof(TermDatabase), 1);

	init_database(db);

	return db;
}

int count_structures(Term** terms, int term_count) {
	int count = 0;
	for (int i = 0; i < term_count; i++) {
		if (terms[i]->type == STRUCTURE) {
			count++;
		}
	}

	return count;
}

bool try_add_to_structure_pair_list(TermDatabase* db, Term* term) {
	for (int i = 0; i < db->structure_pair_count; i++) {
		StructurePair pair = db->structure_pairs[i];
		Term* s = pair.structure;
		if (
			strcmp(s->structure.functor, term->structure.functor) == 0
			&& s->structure.arity == term->structure.arity
			) {
			// add arguments to list
			for (int i = 0; i < s->structure.arity; i++) {
				CandidateList* list = &pair.candidate_lists[i];
				Term* arg = term->structure.args[i];

				if (arg->type == VARIABLE || arg->type == STRUCTURE) break;

				bool found = false;
				for (int j = 0; j < list->count; j++) {
					if (termcmp(list->candidates[j], arg)) {
						found = true;
						break;
					}
				}

				if (!found) {
					if (list->count >= list->size) {
						list->candidates =
							realloc(list->candidates, sizeof(Term*)*list->size*2);
						list->size *= 2;
					}
					
					list->candidates[list->count] = duplicate_term(arg);
					list->count++;
				}
			}
			
			return true;
		}
	}

	return false;
}

void add_to_structure_pair_list(TermDatabase* db, Term* term) {
	if (try_add_to_structure_pair_list(db, term)) return;

	Term* structure = calloc(sizeof(Term), 1);
	structure->type = STRUCTURE;
	structure->structure.functor = strdup(term->structure.functor);
	structure->structure.arity = term->structure.arity;

	CandidateList* lists = calloc(sizeof(CandidateList), term->structure.arity);
	for (int i = 0; i < term->structure.arity; i++) {
		Term** candidates = calloc(sizeof(Term*), 10);
		lists[i].candidates = candidates;
		lists[i].count = 0;
		lists[i].size = 10;
	}
	
	StructurePair pair = {
		.structure = structure,
		.candidate_lists = lists,
	};

	if (db->structure_pair_count >= db->structure_pair_size) {
		db->structure_pairs =
			realloc(
					db->structure_pairs,
					sizeof(StructurePair)*db->structure_pair_size*2
					);
		db->structure_pair_size *= 2;
	}
	
	db->structure_pairs[db->structure_pair_count] = pair;
	db->structure_pair_count++;

	try_add_to_structure_pair_list(db, term);
}

void create_candidates(TermDatabase* db) {
	int structure_count = count_structures(db->terms, db->term_count); // should count unique values
	StructurePair* list = calloc(sizeof(StructurePair), structure_count);

	db->structure_pairs = list;
	db->structure_pair_size = structure_count;
	db->structure_pair_count = 0;

	for (int i = 0; i < db->term_count; i++) {
		if (db->terms[i]->type == STRUCTURE) {
			add_to_structure_pair_list(db, db->terms[i]);
		}
	}
}

TermDatabase* create_and_populate_database(Term** terms, int term_count) {
	if (terms == NULL) return NULL;
	
	TermDatabase* db = calloc(sizeof(TermDatabase), 1);
	db->terms = terms;
	db->term_count = term_count;

	create_candidates(db);

	return db;
}

void add_rule_to_db(TermDatabase* db, Rule* rule) {
	if (db->rule_count >= db->rule_size) {
		db->rules = realloc(db->rules, db->rule_size*2*sizeof(Rule*));
		db->rule_size *= 2;
	}

	db->rules[db->rule_count++] = rule;
}

void add_term_to_db(TermDatabase* db, Term* term) {
	if (db->term_count >= db->term_size) {
		db->terms = realloc(db->terms, db->term_size*2*sizeof(Term*));
		db->term_size *= 2;
	}

	db->terms[db->term_count++] = term;
}

