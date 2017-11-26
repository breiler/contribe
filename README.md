
# Arbetsprov för Contribe

## Introduktion
Uppgiften lämnar egentligen för många frågor för att kunna börja implementera.

Jag antar att interfacet, som beskrivs i uppgiften, är kontraktet som webb-utvecklarna ska integrera mot. Den innehåller varken kundkorgs- eller orderhantering vilket i sig tyder på att kontraktet behöver uppdateras innan frontend-teamet börjar med sin implementation. Sedan känns statushanteringen med arrayer lite omodernt och borde istället kunna hanteras med Exceptions eller särskilda REST-resurser. I verkligheten hade det behövts en workshop med frontend-teamet för att ta reda på deras behov. 

Så jag tar mig stora friheter och gör avsteg från kraven och utforskar möjligheterna med ett REST-baserat API istället för RPC.

## Teknikval

Implementationen av applikationen har gjorts i [Spring Boot](https://projects.spring.io/spring-boot/) då denna innehåller massor med funktioner, bra dokumentation, enkel att paketera och har en väl fungerande community.

[SQLite](https://www.sqlite.org/) för DBMS då den kan köras inbäddat med applikationen och kräver ingen extra installtion/konfiguration

[Lombok](https://projectlombok.org/) för att få getters/setters, equals, hashCode, builders och konstruktorer. Detta snabbar upp utvecklingen och det blir mindre kod att underhålla. Kräver dock att man har ett tillägg för lombok i sin IDE.

[Apache Commons](https://commons.apache.org/) för att enkelt hantera filströmmar och strängar

[Swagger](https://swagger.io/) istället för en exempelklient. Denna är bra för såväl konsumenter, utvecklare och testare som genom denna får tillgång till API:ets dokumentation och kan enkelt funktionerna direkt i webbläsaren. Jag ser i och med denna inte behovet av en fristående testklient.

## Köra

Kör applikationen genom följande:
 * Kompilera applikationen: ```mvn clean package```
 * Kör JAR-filen:  ```java -jar target/contribe-1.0-SNAPSHOT-exec.jar``

## Funktioner

Alla funktioner är åtkomliga via Swagger:  http://localhost:8080/swagger-ui.html

### Söka på böcker
Söka på böcker görs genom REST-resursen: ```GET /api/books?query={söksträng}```
Sökningen görs genom ett enkelt SQL-filter som söker i på poster där fälten för författare eller bokens titel innehåller söksträngen. Sökfiltret bryr sig inte om gemener eller versaler.

[Se API-dokumentation i Swagger](http://localhost:8080/swagger-ui.html#!/Books/findBooksUsingGET) 


### Skapa kundvagn
En användare kan skapa en kundvagn genom REST-resursen: ```POST /api/carts```
Kundvagnen skapas upp och returneras tillsammans med ett unikt id. Id:t till kundvagnen kan sedan användas för att lägga till böcker och för att göra själva beställningen.

[Se API-dokumentation i Swagger](http://localhost:8080/swagger-ui.html#!/Carts/createUsingPOST_1) 


### Lägga till böcker till kundvagn
En användare kan lägga till böcker till sin kundvagn genom REST-resursen: ```POST /api/carts/{cartId}```
Boken och hur många man vill ha specificeras genom följande JSON:
```json
{
  "bookId": 1,
  "quantity": 1
}
```

[Se API-dokumentation i Swagger](http://localhost:8080/swagger-ui.html#!/Carts/createUsingPOST) 


### Se innehållet i sin kundvagn
En användare kan hämta hela sin kundvagn genom REST-resursen: ```GET /api/carts/{cartId}```
Denna returnerar information om boken och antal. Den räknar även ihop antalet artiklar och summan för hela kundvagnen.

[Se API-dokumentation i Swagger](http://localhost:8080/swagger-ui.html#!/Carts/fetchAllUsingGET) 


### Lägga en order
En användare kan lägga en order av sin kundvagn genom REST-resursen ```POST /api/carts/{cartId}/order```
Denna kontrollerar först att det finns tillräckligt med böcker på lagret för att kunna genomföra ordern (annars slängs ett fel). 
Därefter räknas antalet ner på alla artiklar på lagret.

[Se API-dokumentation i Swagger](http://localhost:8080/swagger-ui.html#!/Orders/createOrderFromCartUsingPOST) 


### Lägga till böcker till sortimentet
En användare kan lägga till böcker till sortimentet genom REST-resursen ```POST /api/books```
Information om boken ska innehålla författare, titel och ett pris:
```json
{
  "author": "Philip K Dick",
  "price": 19.99,
  "title": "Do Androids Dream Of Electric Sheep?"
}
```

[Se API-dokumentation i Swagger](http://localhost:8080/swagger-ui.html#!/Books/createUsingPOST) 


### Ändra böckers lagerstatus
En användare kan ändra lagerstatus för en bok genom REST-resursen ```PUT /api/stock/{bookId}```
Funktionen sätter lagerstatusen till det antal man skickar in enligt följande exemepel:
```json
{
  "quantity": 1
}
```



