all:
	gcc -I./a3clib/lib/ -L./a3clib/lib/output/ -o main main.c -la3clib -lm
