#include <stdio.h>
#include <assert.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdbool.h>

#include "parser.h"

typedef struct CandidateList {
	Term** candidates;
	int count;
	int size;
} CandidateList;

typedef struct StructurePair {
	Term* structure;
	CandidateList* candidate_lists;
} StructurePair;

typedef struct TermDatabase {
	int term_count;
	Term** terms;
	
	int structure_pair_count;
	int structure_pair_size;
	StructurePair* structure_pairs;
} TermDatabase;

void print_database(TermDatabase* db) {
	printf("TermDatabase {\n");
	printf("\tint term_count = %d\n", db->term_count);
	printf("\tTerm** terms = %p\n\n", db->terms);
	printf("\tint structure_pair_count = %d\n", db->structure_pair_count);
	printf("\tint structure_pair_size = %d\n", db->structure_pair_size);
	printf("\tStructurePair* pairs = {\n");
	
	for (int i = 0; i < db->structure_pair_count; i++) {
		StructurePair pair = db->structure_pairs[i];
		printf("\t\tStructurePair {\n");
		printf("\t\t\tFunctor = %s\n", pair.structure->structure.functor);
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
}

void init_database(TermDatabase* db) {
	db->term_count = 0;
	db->terms = NULL;
	db->structure_pair_count = 0;
	db->structure_pair_size = 0;
	db->structure_pairs = NULL;
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
				CandidateList list = pair.candidate_lists[i];
				Term* arg = term->structure.args[i];

				bool found = false;
				for (int j = 0; j < list.count; j++) {
					if (termcmp(list.candidates[j], arg)) {
						found = true;
						break;
					}
				}

				if (!found) {
					if (list.count >= list.size) {
						list.candidates =
							realloc(list.candidates, sizeof(Term*)*list.size*2);
						list.size *= 2;
					}
					
					list.candidates[list.count] = duplicate_term(arg);
					list.count++;
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

// TODO: fix adding arguments to structures
TermDatabase* create_and_populate_database(Term** terms, int term_count) {
	TermDatabase* db = calloc(sizeof(TermDatabase), 1);
	db->terms = terms;
	db->term_count = term_count;

	int structure_count = count_structures(terms, term_count); // should count unique
	StructurePair* list = calloc(sizeof(StructurePair), structure_count);

	db->structure_pairs = list;
	db->structure_pair_size = structure_count;
	db->structure_pair_count = 0;

	for (int i = 0; i < term_count; i++) {
		if (terms[i]->type == STRUCTURE) {
			add_to_structure_pair_list(db, terms[i]);
		}
	}

	return db;
}



Term* fill_db_term(Term* dbterm, Term* query) {
	Term* dup = duplicate_term(dbterm);

	// fill in variables with atoms and numbers
	for (int i = 0; i < dup->structure.arity; i++) {
		Term* arg = dup->structure.args[i];
		if (arg->type == VARIABLE) {
			// replace with value from query
			dup->structure.args[i] = duplicate_term(query->structure.args[i]);

			// replace variables with same name (TODO: RECURSIVE)
			for (int j = i+1; j < dup->structure.arity; j++) {
				Term* other_arg = dup->structure.args[j];
				if (other_arg->type == VARIABLE && termcmp(arg, other_arg)) {
					dup->structure.args[j] = duplicate_term(query->structure.args[i]);
				}
			}
		}
	}

	return dup;
}

bool evaluate_terms(Term* dbterm, Term* query) {
	Term* filled_dbterm = fill_db_term(dbterm, query);

	return termcmp(filled_dbterm, query);
}

bool resolve(Term* query, TermDatabase* db) {
	if (query->type != STRUCTURE) {
		return false;
	}
	
	for (int i = 0; i < db->term_count; i++) {
		Term* term = db->terms[i];
		
		if (evaluate_terms(term, query)) {
			return true;
		}
	}

	return false;
}

bool run_query(char* query_str, TermDatabase* db) {
	Term* query = parse_term(query_str);

	return resolve(query, db);
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

	TermDatabase* db = create_and_populate_database(terms, term_count);
	print_database(db);
	
	char buffer[257];
	buffer[0] = '\0';
	while (strcmp(buffer, "stop") != 0) {
		fgets(buffer, 256, stdin);
		buffer[strlen(buffer)-1] = '\0'; // remove newline

		if (buffer[0] == '?' && buffer[1] == '-') {
			if (run_query(&buffer[2], db)) {
				printf("true\n");
			} else {
				printf("false\n");
			}
		}
	}

	return 0;
}
