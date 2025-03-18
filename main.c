#include <stdio.h>
#include <assert.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdbool.h>

#include "parser.h"
#include "term.h"

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

bool structures_eq(Term* s1, Term* s2) {
	if (s1->type != STRUCTURE || s2->type != STRUCTURE) {
		return false;
	}

	return
		strcmp(s1->structure.functor, s2->structure.functor) == 0
		&& s1->structure.arity == s2->structure.arity;
}

CandidateList* get_candidate_list(TermDatabase* db, Term* structure, int argi) {
	CandidateList* list = NULL;
	for (int i = 0; i < db->structure_pair_count; i++) {
		if (structures_eq(structure, db->structure_pairs[i].structure)) {
			list = &db->structure_pairs[i].candidate_lists[argi];
			break;
		}
	}

	return list;
}

Term* get_next_candidate(CandidateList* list, int* start_index) {
	if (*start_index >= list->count) {
		return NULL;
	}

	// can be one-liner
	Term* res = list->candidates[*start_index];
	*start_index += 1;

	return res;
}

int count_vars(Term* query) {
	int var_count = 0;
	for (int i = 0; i < query->structure.arity; i++) {
		Term* this = query->structure.args[i];
		if (this->type == VARIABLE) {
			bool counted = false;
			for (int j = 0; j < i; j++) {
				Term* other = query->structure.args[j];
				if (other->type == VARIABLE
					&& strcmp(other->variable.name, this->variable.name) == 0
					) {
					counted = true;
					break;
				}
			}
			if (!counted) var_count++;
		}
	}

	return var_count;
}

int get_argi(Term* query) {
	for (int i = 0; i < query->structure.arity; i++) {
		if (query->structure.args[i]->type == VARIABLE) {
			return i;
		}
	}

	return -1;
}

Term* fill_vars(Term* query, int argi, Term* filling) {
	Term* d_query = duplicate_term(query);
	
	Term* var = query->structure.args[argi];
	for (int i = argi; i < query->structure.arity; i++) {
		if (termcmp(var, query->structure.args[i])) {
			d_query->structure.args[i] = duplicate_term(filling);
		}
	}

	return d_query;
}

// memory leaks go brrrr..
Term* branch_and_bound(TermDatabase* db, Term* query, int varc) {
	if (varc == 0) {
		// resolve
		if (resolve(query, db)) {
			return query;
		} else {
			return NULL;
		}
	} else {
		// fill next var in
		int argi = get_argi(query);

		if (argi < 0) {
			return NULL;
		}
		
		CandidateList* list = get_candidate_list(db, query, argi);

		for (int i = 0; i < list->count; i++) {
			Term* filled_query = fill_vars(query, argi, list->candidates[i]);
			Term* bab_res = branch_and_bound(db, filled_query, varc-1);

			if (bab_res) {
				return bab_res;
			}

			free(filled_query); // TODO: CREATE PROPER FREE FUNCTION
		}

		return NULL;
	}
}

// currently assuming only 1 variable
// should return response in form of VAR = ATOM
Term* unify(TermDatabase* db, Term* query) {
	int varc = count_vars(query);

	return branch_and_bound(db, query, varc);
}

bool contains_variables(Term* structure) {
	for (int i = 0; i < structure->structure.arity; i++) {
		Term* arg = structure->structure.args[i];
		if (arg->type == VARIABLE) {
			return true;
		}
	}

	return false;
}

void print_filled_vars(Term* query, Term* filled) {
	for (int i = 0; i < query->structure.arity; i++) {
		Term* var = query->structure.args[i];
		if (var->type == VARIABLE) {
			Term* filling = filled->structure.args[i];
			switch(filling->type) {
			case ATOM:
				printf("%s = %s\n", var->variable.name, filling->atom.name);
				break;
			case NUMBER:
				printf("%s = %f\n", var->variable.name, filling->number.value);
				break;
			case STRUCTURE:
				printf("FILLING STRUCTURE NOT IMPLEMENTED\n");
				break;
			case VARIABLE:
				printf("There shouldn't be a variable here\n");
				print_term(filled);
				exit(1);
			}
		}
	}
}

// returns response (true, false, X=...)
void run_query(char* query_str, TermDatabase* db) {
	Term* query = parse_term(query_str);

	if (contains_variables(query)) {
		Term* res = unify(db, query);

		if (res == NULL) {
			printf("FAILED\n");
			return;
		}
		
		print_filled_vars(query, res);
	} else {
		bool res = resolve(query, db);
		
		if (res) printf("true\n");
		else printf("false\n");
	}
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
