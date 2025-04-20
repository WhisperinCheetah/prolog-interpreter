# Frequently Asked Questions

1. Hoe moet ik aan het project beginnen?

> Belangrijk is het doel voor ogen te houden, en duidelijk te onthouden wat belangrijk is. De Prolog interpreter steunt op twee zaken: unificatie + backtracking.
> Het implementeren van deze twee componenten is het belangrijkste deel van het project. Laat je interne representatie van termen hier ook vanaf hangen, en niet van je parser!

> Begin met deze twee zaken goed te snappen, en je zal zien dat deze mits goeie planning in verrassend weinig lijnen kunnen geschreven worden.

> Daarnaast is ook je parser een belangrijke component, maar parsers zijn overal te vinden en daarom bestaan er dus veel bibliotheken voor. Gebruiken deze bibliotheken en vindt niet het wiel opnieuw uit.

2. Mag ik GitHub actions gebruiken?

> Ja, maar de GitHub runners van de indien repo zijn enkel beschikbaar voor de assistenten. Je kan dus enkel GitHub actions gebruiken op een eigen private fork van de indien repo. Op de indien repo zelf kan je geen GitHub actions gebruiken, tenzij je een eigen runnen opzet (neem dan contact op met de assistent).

3. Mag ik libraries gebruiken?

> Ja! Algemene libraries mag je zeker gebruiken. Bibliotheken die delen van de prolog interpreter implementeren zijn uiteraard niet toegelaten. Als je twijfelt over een library, dan kan je steeds de assistent mailen voor toestemming.

4. Mag ik de interpreter in Prolog implementeren?

> Nee.

5. Moeten we haakjes ondersteunen voor disjunctie? Bijvoorbeeld: Vader = anakin , (father(Vader, leia) ; father(Vader, luke)).

> Nee.

6. Moeten we in de REPL ook choice points ondersteunen?

> Ja, je moet ook choice points ondersteunen op dezelfde manier als swipl, waarbij je gpl laat verder zoeken met `;`.

7. Moeten we ook `append` ondersteunen voor de lijst uitbreiding?

> Ja, de drie predicaten de we oplijsten in de opgave zijn boven de standaard operaties op lijsten: append en member.

8. Hoe moeten we unificatie implementeren?

> Als je nog moeite hebt met het unificatie algorithme, kijk dan zeker eerst opnieuw naar de slides en je lesnota's. In de oefeningenreeks van week 3 hebben we ook een [uitgebreide oefening](https://dodona.be/en/courses/4816/series/54910/activities/930425534/) gemaakt over het unificatie algorithme, waarbij we een occurs check toevoegden. Deze oefening gaf een implementatie van het unificatie algorithme voor twee termen in Prolog.
>
> Daarnaast kan je ook het boek "The Art of Prolog" raadplegen (beschikbaar als fysiek en ebook in de [universiteitsbibliotheek](https://lib.ugent.be/en/catalog?q=the+art+of+prolog)). In dit boek wordt het unificatie algorithme uitgebreid besproken in hoofdstuk 4.

9. Wordt ons project automatisch getest?

> Ja, in de opgave repo vind je enkele voorbeeld programma's uit de automatische testen onder de map `examples`.
> Er is tevens een docker image ter beschikking met de exacte configuratie die wij zullen gebruiken om je project te testen.

10. Wordt de output enkel exact vergeleken met die van swipl?

> De automatische testen maken vooral gebruik van het `write(predicaat)`, waarbij we de output exact zullen vergelijken. Indien voor uitzonderlijke gevallen je output wel juist is, maar toch verschilt van swipl (door een verschil in het unificatiealgoritme) dan zullen wij daar rekening mee houden bij het verbeteren.

