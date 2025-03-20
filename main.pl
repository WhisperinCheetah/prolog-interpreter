likes(john,mary).
likes(john,john).

likes(john,bob).
likes(bob,X).
	
loves(X,Y) :- likes(X,Y).
