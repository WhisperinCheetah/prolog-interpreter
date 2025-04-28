likes(john,mary).
likes(mary,john).

likes(john,jee).
likes(john,lee).
likes(john,sam).
	
loves(X,Y) :- likes(X,Y), likes(Y,X).
