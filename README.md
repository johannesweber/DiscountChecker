# DiscountChecker
rep igt ss16

#HI

--

Tomcat 8
Java 1.8
MySQL 5.6.26
alles andere ist in der POM zu find3n

Das Dumb für die Datenbank ist in src/main/webapp/dumb zu finden

Examples:

Schicke an "http://localhost:8080/DiscountChecker/api/v1/matcher/match/15/45" ein GET Request um das BPMN mit der Id 15 mit dem WADL Dokument mit der Id 45 zu vergleichen bzw. für jeden Task bzw. Step eine geeignete Methode im WADL zu finden

Schicke ein POST Request an "http://localhost:8080/DiscountChecker/api/v1/matcher/bpmn/examplePath1" um das BPMN mit dem
angegebenen Pfad zu analysieren

Das gleiche kann man auch mit einem WADL Dokument machen indem man ein POST Request an "http://localhost:8080/DiscountChecker/api/v1/matcher/wadl/examplePath1"

Das Programm ist im Moment noch so ausgelegt, dass nicht der angegebene "examplePath1" genommen wird sondern die Beispieldateien in src/main/webapp

Der Pfad wird direkt in der Klasse "MatcherServlet" festgelegt.