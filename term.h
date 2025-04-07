#ifndef _TERM_H_
#define _TERM_H_

#include <stdbool.h>

/** TERMS **/
typedef enum { ATOM, NUMBER, VARIABLE, STRUCTURE } TermType;

typedef struct Term {
    TermType type;
    union {
        struct { char *name; } atom;
        struct { double value; } number;
        struct { char *name; } variable;
        struct { char *functor; struct Term **args; int arity; } structure;
    };
} Term;

void print_term(Term* term);
void copy_term(Term* dest, Term* src);
Term* duplicate_term(Term* src);
bool termcmp(Term* t1, Term* t2);

/** RULES **/
typedef enum { CONJUNCTION, DISJUNCTION } BodyType;

typedef struct Rule {
    Term* head;
	Term** body;
	int body_count;
} Rule;

typedef struct BodyNode {
    BodyType type; // Conjunction or disjunction with next
	Term* term; // NULL if not leaf
	struct BodyNode *left;
    struct BodyNode *right;
} BodyNode;

void print_rule(Rule* rule);

/** DATABASE **/
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
	int term_size;
	Term** terms;

	int rule_count;
	int rule_size;
	Rule** rules;
	
	int structure_pair_count;
	int structure_pair_size;
	StructurePair* structure_pairs;
} TermDatabase;

void print_database(TermDatabase*);
void init_database(TermDatabase*);
TermDatabase* create_database();
TermDatabase* create_and_populate_database(Term** terms, int term_count);

void add_rule_to_db(TermDatabase*, Rule*);
void add_term_to_db(TermDatabase*, Term*);

void create_candidates(TermDatabase*);


#endif
