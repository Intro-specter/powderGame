# powderGame
Eine einfache 2D Pixel Material Simulation in Java für EF - Informatik HS 24-25

Die Simulation beinhaltet ein interaktives Fenster auf welchem man mit clicken und Tasteneingaben verschiedene Materialien im Raster eintragen kann, welche dann automatisch schrittweise simuliert werden. Diese Materialien werden immer einzeln als Partikel die interagieren simuliert, d.h. werden keine grosse festkörper implementiert.

Mit dieser Implementation möchte ich lernen wie man besser ein flexibles und erweiterbares Projekt plant und gestaltet, indem ich neben dem Programmieren mit UML-Diagrammen und Tests arbeite. 

Die Animation und Aufzeichnung verläuft über JPanel, und die Simulation im Hintergrund wird an einer eindimensionalen ArrayList angewendet.

Eingabeanweisungen: 

Maus:
- Left Click (& Drag): Ausgewähltes Material Plazieren
- Mouse Wheel Scroll: Skalierung des Rasters
- Resize Window: Ändert Dimensionen des Rasters

Tastatur:
- ESC: Quit
- Q: Plazierungsradius verkleinern
- E: Plazierungsradius vergrössern
- R: Alle Partikel löschen
- O: Simulation verschnellern
- P: Simulation verlangsamen
- SPACE: Simulation pausieren
- D: Text Zeigen
- F: Texthintergrund Zeigen
- G: Zusätzliche Grafikprozesse ein-/ausschalten
- 1: Empty
- 2: Sand
- 3: Water
- 4: Cloud
- 5: Stone
- 6: Ice
- 7: Lava

Dieses Projekt wurde von @Intro-specter (CB) gebaut.