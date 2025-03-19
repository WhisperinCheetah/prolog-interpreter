CFLAGS = -Wall -Wextra -Wno-missing-braces -Werror=pointer-arith -O3

all: main.c parser.h parser.o unifier.o
	gcc -o main main.c parser.o term.o unifier.o $(CFLAGS)

parser.o: parser.c parser.h term.o
	gcc $(CFLAGS) -c parser.c term.o

term.o: term.c term.h
	gcc $(CFLAGS) -c term.c

unifier.o: unifier.c unifier.h
	gcc $(CFLAGS) -c unifier.c
