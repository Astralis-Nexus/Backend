# Problemformulering

Virksomheden ønsker at udvikle et internt admin-panel, der skal erstatte deres nuværende løsning i Google Drive, som ofte fører til tab af information. Panelet skal gøre det lettere for medarbejdere at administrere udlånte konti, adgangskoder samt dagens to-do-liste og eventuelt andre administrative opgaver.

Løsningen skal bestå af:
- En **PostgreSQL-database** til sikker og struktureret lagring af data.
- En **backend i Java** med et REST API, der håndterer forretningslogik og databehandling.
- En **frontend i React**, hvor React fungerer som en skabelon, mens alt indhold styres dynamisk via API'et. Dette sikrer en generisk og fleksibel løsning.

### Rollebaseret adgangsstyring
Systemet skal inkludere **rollebaseret adgangsstyring**, hvor kun administratorer kan oprette nye brugere og tildele roller. Hver rolle skal have specifikke adgangsrettigheder og funktionaliteter for at sikre en struktureret og sikker brug af panelet.

### Mål
Målet er at skabe en mere **sikker, effektiv og brugervenlig platform**, der forhindrer informationssletning og gør det lettere for medarbejderne at håndtere daglige administrative opgaver.
