# Opgave Project Logisch Programmeren: Ghent Prolog

Er is een nijpend tekort aan Prolog-implementaties. Om dit probleem op te lossen, is het jouw taak om een eigen Prolog-interpreter *Ghent Prolog* te bouwen. Deze interpreter moet een subset van Prolog ondersteunen en in staat zijn om eenvoudige logische regels en queries uit te voeren alsook minstens één complexere Prolog constructie ondersteunen. 

Je bent volledig vrij om de programmeertaal waarin je de Prolog-interpreter maakt zelf te kiezen. Let er echter op dat je code beoordeeld zal worden op leesbaarheid en dat je de implementatie moet kunnen uitleggen tijdens je verdediging. Gebruik van esoterische programmeertalen, zoals gesuggereerd tijdens het hoorcollege, wordt daarom ten zeerste afgeraden.

In dit document geven we een overzicht van de basis functionaliteit van deze interpreter. 
Verder geven we de vereisten waaraan je project moet voldoen.

## Doel van de Opgave
Het doel van de opgave is om een basisimplementatie van een Prolog-interpreter maken, we testen hiermee dat je kennis hebt van de volgende Prolog concepten. 

* Unificatie.
* Backtracking en cut.
* Pattern matching.
* Arithmetic.
* Eén Prolog uitbreiding (zie later in de opgave). 

Zorg er dan ook voor dat je extra aandacht aan deze concepten geeft in je implementatie. 


## Interface

Om ervoor te zorgen dat we alle interpreters op dezelfde manier kunnen testen zullen we werken met input/output testen, je implementatie moet dan ook de juiste interface ondersteunen. 

Veronderstel dat de onderstaande code  in een bestand `test.pl` staat. 

```
:- initialization(main). 

main :- write(10),
        nl,
		write(hello(world)).
```

Dan zal je interpreter *gpl* moeten gestart kunnen worden op de command line als volgt:

```
gpl -s test.pl 
10
hello(world)
```

In onze testen zullen we interpreter testen door *swipl* uit te voeren als volgt:

```
swipl  -t halt -q -s test.pl
10
hello(world)
```
Onze test zal dan simpelweg bekijken of de uitvoer van *gpl* overeenkomt met deze van *swipl*.



## Functionaliteit

Hieronder volgt een lijst met de meest voor de hand liggende functionaliteiten die je moet ondersteunen, voor meer informatie kan je altijd de Prolog documentatie raadplegen. Twijfel je tijdens de implementatie of bepaalde functionaliteiten nodig zijn voor de basisimplementatie? Neem dan gerust contact met ons op.    

**Run modes**

Je interpreter moet minimaal de volgende twee modi ondersteunen:

- [ ] een script mode (gpl -s)
- [ ] een read-eval-print loop (`gpl --repl` of `gpl -r`)


**Input/Output/Initialisatie**

Deze functionaliteit is bijzonder belangrijk gezien de automatische testen hiervan gebruik zullen maken. Het gebruik van `initalization` is om Goal op te roepen direct na het inladen van de file, SWIPL heeft ook ondersteuning voor saved states, dit moet je niet implementeren. 

- [ ] write(+Term) 
- [ ] read(-Term)
- [ ] :- initialization(:Goal) 


**Control Constructs**

- [ ] `,/2`
- [ ] `;/2`
- [ ] `fail/0` 
- [ ] `!/0` 
- [ ] `forall/2` 
 
**term unification**

- [ ] `=/2` 
- [ ] `\=/2` 

**term comparison**

- [ ] `==/2` 
- [ ] `\==/2`


**arithmetics**

- [ ] `is/2` (implementeer minstens, `+, -, *, \`)
- [ ] `between/3` 
- [ ] `succ/2` 

**database operations**

- [ ] `assert/1` 
- [ ] `asserta/1` 
- [ ] `assertz/1` 
- [ ] `retract/1` 
- [ ] `retractall/1` 


## Uitbreidingen

Implementeer minstens één van de onderstaande uitbreidingen. 
Je mag meerdere uitbreidingen implementeren voor extra punten, maar extra punten worden alleen toegekend als de uitbreidingen voldoende goed zijn uitgevoerd. Het is dus beter om één uitbreiding perfect te implementeren dan drie uitbreidingen half te implementeren, in dat geval zal je geen extra punten verdienen.

### Lijsten

Een leuke uitbreiding voor je Prolog-interpreter is de ondersteuning van lijsten. Lijsten zijn een van de fundamentele datastructuren in Prolog, en het uitbreiden van je interpreter met extra lijstfunctionaliteiten kan je programma’s veel krachtiger maken. Voeg lijsten toe aan de interpreter met pattern matching syntax, voorzie daarbij ook de volgende lijst predicaten:  

- [ ] `bagof/3` 
- [ ] `findall/3` 
- [ ] `setof/3` 

Zorg voor een set van testen die je predicaten testen.  


### DCG support 

Een interessante uitbreiding voor je Prolog-interpreter is de ondersteuning van Definite Clause Grammars (DCG), een ingebouwde manier om grammatica’s en parsers te definiëren in Prolog. DCG’s worden vaak gebruikt voor het verwerken van natuurlijke taal en het bouwen van parsers voor programmeertalen. Hint: Het boek legt uit hoe je DCG's syntax kan omzetten naar traditionele Prolog regels.

Voor meer info raadpleeg de Prolog documentatie [DCG](https://www.swi-prolog.org/pldoc/man?section=DCG).


### Meta abstracties
Een krachtige uitbreiding voor je Prolog-interpreter is de ondersteuning van meta-abstracties, waarmee Prolog-code zichzelf kan inspecteren. Meta-abstracties maken het mogelijk om programma’s te schrijven die andere Prolog-programma’s manipuleren, analyseren of zelfs uitbreiden. Implementeer hiervoor minstens de volgende predicaten: 

- [ ] `functor(?Term, ?Name, ?Arity)`
- [ ] `arg(?Arg, +Term, ?Value)`
- [ ] `?Term =.. ?List` 
- [ ] `clause/2` (ga na of een regel bestaat)

Zorg voor een test van meta predicaten zodat je delimited continuations meta circulair kan implementeren zoals beschreven in [Delimited Continuations for Prolog](https://www.swi-prolog.org/download/publications/iclp2013.pdf). 
Het is ook toegelaten om delimited continuations in de interpreter te implementeren voor extra punten, afhankelijk van je basis implementatie zal dit bijzonder complex zijn. 


### Eigen idee? 

Heb je een innovatief idee om Prolog uit te breiden? Wil je experimenteren met nieuwe concepten binnen logisch programmeren of functionaliteit toevoegen die de mogelijkheden van je interpreter vergroot? Dan is dit jouw kans om een eigen uitbreiding te ontwerpen. 

Voor deze opdracht mag je dan zelf een unieke feature bedenken die jouw Prolog-interpreter verrijkt. Dit kan een praktische verbetering zijn, een experimentele toevoeging, of een creatief concept dat nieuwe toepassingen verkent, zolang het technisch onderbouwd en goed geïmplementeerd is. 

Houd er rekening mee dat je eigen uitbreiding vooraf goedgekeurd moet worden. Om goedkeuring te krijgen, volstaat het om een korte beschrijving van je uitbreiding per e-mail op te sturen (naar de hoofdlesgever en praktijkassistent). Let op: als je een uitbreiding implementeert zonder voorafgaande goedkeuring, kun je hier helaas geen punten voor verdienen.

Een interessante uitbreiding voor je Prolog-interpreter zou het implementeren van goed gedefinieerde input-output (I/O)-primitieven kunnen zijn die efficiënt omgaan met backtracking. In de standaard Prolog-implementaties wordt I/O vaak op een onhandige manier afgehandeld, wat problemen kan opleveren bij backtracking. 

## Niet functionele eisen

Naast de basisfunctionaliteit vragen we enkele niet functionele eisen waar je project aan
dient te voldoen. Deze niet functionele eisen zijn even belangrijk als de functionele eisen
van het project.

- de code moet goed gedocumenteerd zijn, er moet commentaar geschreven bij de moeilijkere delen van je code. 
- je code moet getest zijn, dit wil zeggen dat je voor elke functionaliteit zelf een test schrijft zodat je zeker bent dat de basis functionaliteit werkt.
- je project moet testbaar zijn aan de hand van input/output.

## Verslag

We verwachten een bondig verslag die de algemene oplossingsstrategie van je project beschrijft. 
Voeg aan je verslag je code toe met lijnnummers zodat je in de uitleg van je verslag kan verwijzen naar de relevante delen van je code. Je bent zelf vrij hoe je dit verslag organiseert maar we verwachten op zijn minst de volgende onderdelen:

- Inleiding
- Beschrijving van je interpreter 	
	- architectuur 
	- data representatie (hoe stel je de databank voor)
	- evaluatie strategie (hoe evalueer je queries)
- Beschrijving van je uitbreidingen
- Conclusie


## Mondelinge verdediging

Op het einde van het semester zal je ook jouw project mondeling moeten voorstellen.
De dag van de verdedigingen zal later worden meegedeeld.

# Indienen

Het indienen van het project gebeurd via **GitHub Classroom**.

## GitHub Classroom

Om te starten met het project, klik je op de invite link voor GitHub Classroom die je op Ufora kan vinden onder *Inhoud > Project*.
Vervolgens zal je jouw naam moeten kiezen uit de lijst van ingeschreven studenten.
Daarna krijg je een eigen fork van de opgave repository.

Als hiermee iets fout loopt, contacteer ons dan zo snel mogelijk.

> [!IMPORTANT]
> De laatste commit op de branch `main` van je fork op de deadline geldt als je indiening.

## Vorm 
Je project moet volgende structuur hebben:

  - `src/` bevat alle broncode (inclusief `gpl`).
  - `tests/` alle testcode.
  - `documentatie/verslag.pdf` bevat de elektronische versie van je verslag. In
    deze map kun je ook eventueel extra bijlagen plaatsen.

Je directory structuur ziet er dus ongeveer zo uit:

```
    |
    |-- documentatie/
    |   -- verslag.pdf
    |-- src/
    |   |-- gpl (uitvoerbaar script)
    |   -- je broncode
    `-- tests/
        -- je testcode
```

## Deadline

Het project moet ingediend zijn op 9 mei 2025 om 20:00 CEST.

# Algemene richtlijnen

  - Schrijf efficiënte code, maar ga niet over-optimaliseren: **geef de
    voorkeur aan elegante, goed leesbare code**. Kies zinvolle namen
    voor predicaten en variabelen en voorzie voldoende commentaar.
  - Het project wordt gequoteerd op **10** van de 20 te behalen punten
    voor dit vak. Als de helft niet wordt behaald, is je eindscore het
    minimum van je examencijfer en je score op het project.
  - Dit is een individueel project en dient dus door jou persoonlijk
    gemaakt te worden. **Het is ten strengste verboden code uit te
    wisselen**, op welke manier dan ook. Het overnemen van code
    beschouwen we als fraude (van **beide** betrokken partijen) en zal
    in overeenstemming met het examenreglement behandeld worden. Het
    overnemen of aanpassen van code gevonden op internet is ook **niet
    toegelaten** en wordt gezien als fraude.
  - Vragen worden mogelijks **niet** meer beantwoord tijdens de laatste
    week voor de finale deadline.

# Vragen

Als je vragen hebt over de opgave of problemen ondervindt, dan kun je je
vraag stellen via [mail](mailto:tom.lauwaerts@ugent.be).
Stuur geen screenshots van code, maar link naar de code in je GitHub repository.
Indien mogelijk/toepasbaar, vermeld ook een “[minimal breaking
example](https://stackoverflow.com/help/minimal-reproducible-example)”.

