Textauszug der Garfield Pipe Strip Analyse: https://youtu.be/NAh9oLs67Cw

Anforderungen:
 - Mindestens OpenJDK 11 (mit neuster BlueJ-Version enthalten) -> läuft nicht auf den uralten Schulrechnern
 - Für die Erweiterung des Wörterbuchs wird eine Internetverbindung benötigt
 - Bei zu vielen automatisierten Online-Übersetzungsanfragen kann die eigene IP-Adresse geblockt werden (d.h. keine weiteren Übersetzungen mehr möglich, für immer)
Startpunkt: Funktion main() in Klasse Main
Ausführen ohne BlueJ (falls es Probleme gibt):
 - Eine Kommandozeile in diesem Ordner öffnen (Explorer->Rechtsklick->PowerShell hier öffnen)
 - Folgendes einfügen und ausführen: "dir /s /B *.java > sources.txt"
 - Danach die Java-Dateien kompilieren mit: "javac @sources.txt"
 - Danach das Programm ausführen mit: "java Main"
Programmbeschreibung:
 - Zuerst kommt eine kleine Demonstration meiner Binärbaum-Implementation
 - Der Text in der Datei Text.txt wird beim Programmstart eingelesen und in die Datei Translation.txt übersetzt
 - Nutzung von Übersetzungsdaten aus dem Internet und lokal gespeicherten Übersetzungen (in der cache.txt Datei)
 - Danach kann noch interaktiv per Kommandozeile das Wörterbuch um bestimmte Wörter abgefragt/erweitert werden
Viel Spaß :-)
