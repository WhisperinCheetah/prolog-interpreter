from graphviz import Digraph

dot = Digraph(format='svg')

# Example types
dot.node('fact', 'Fact')
dot.node('rule', 'Rule')

dot.node('term', 'Term')
dot.node('complexTerm', 'ComplexTerm')
dot.node('predicate', 'Predicate')

dot.node('simpleTerm', 'SimpleTerm')
dot.node('atom', 'Atom')
dot.node('number', 'Number')
dot.node('variable', 'Variable')

# Tree structure
dot.edge('fact', 'rule')
dot.edge('fact', 'term')

dot.edge('term', 'simpleTerm')
dot.edge('term', 'complexTerm')
dot.edge('complexTerm', 'predicate')

dot.edge('simpleTerm', 'atom')
dot.edge('simpleTerm', 'number')
dot.edge('simpleTerm', 'variable')

# Save or render
dot.render('prolog_types_tree', view=True, format='svg')
