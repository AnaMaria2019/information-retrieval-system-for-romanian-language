Acesta a fost continutul initial al fisierului readme.

Versiuni Utilizate:
- Java: java version "1.8.0_281"
- Apache Lucene: 8.11.0
- tika-core: 2.1.0
- tika-parsers: 2.1.0
- tika-serialization: 2.1.0
- tika-parser-pdf-module: 2.1.0
- tika-parser-microsoft-module: 2.1.0

Instructiuni de rulare:
- Mai intai se ruleaza clasa IndexFiles cu argumentulul "--open-mode=CREATE" sau "--open-mode=APPEND" care indica modul in care indexer-ul va fi creat - aceasta clasa creeaza un index in directorul "index" (daca nu exista directorul, atunci acesta va fi creat) pentru documentele aflate in directorul "files";
- Apoi se apeleaza clasa SearchFiles cu urmatorii parametrii: primul trebuie sa fie "--query-path", iar al doilea path-ul catre fisierul ce contine query-ul dupa care se doreste a fi efectuata cautarea (in cazul meu, query-ul se afla in fisierul cu acelasi nume "query.txt");

Mentiuni:
- Documentele pentru indexare trebuie adaugate in directorul "files";
- Fisierele cu extensia .txt trebuie sa fie salvate cu encoding-ul "UTF-8"