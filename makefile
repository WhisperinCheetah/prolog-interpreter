CFLAGS = -Wall -Wextra -Wno-missing-braces -Werror=pointer-arith -O3

all: main.c parser.h parser.o
	gcc -o main main.c parser.o $(CFLAGS)

parser.o: parser.c parser.h
	gcc $(CFLAGS) -c parser.c 

