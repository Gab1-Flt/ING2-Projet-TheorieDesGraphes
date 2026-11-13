<div align="center">

# Urban Waste Collection Optimizer

**Graph-theory-based routing and scheduling engine for municipal waste management**

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=flat&logo=openjdk&logoColor=white)](#)
[![Algorithms](https://img.shields.io/badge/Algorithms-Dijkstra%20%7C%20Euler%20%7C%20Prim%20MST%20%7C%20Welsh--Powell-2563EB?style=flat)](#)
[![Dependencies](https://img.shields.io/badge/Dependencies-Zero%20External-16A34A?style=flat)](#)
[![Interface](https://img.shields.io/badge/Interface-CLI-475569?style=flat)](#)

[English](#overview) • [Français](#aperçu)

</div>

---

## Overview

Built in pure Java with zero external dependencies, this project models an urban road network as a **weighted graph** to solve real-world municipal waste collection problems: shortest-path dispatching, full-network street traversal, multi-stop collection tours, dynamic road disruptions, and conflict-free collection scheduling.

The interactive CLI separates responsibilities between two operational roles: **Municipality (`Collectivité`)** and **Collection Company (`Entreprise`)**.

## Core Features

### Municipality (`Collectivité`)
- **City Map Ingestion** — Loads the municipal road network (`Plan.txt`) into an indexed graph of intersections, weighted street arcs, and house addresses.
- **Live Traffic Updates** — Dynamically blocks closed streets (automatically bypassed in route calculations) or inserts new streets with custom intersections, distances, and addresses.
- **Waste Volume Reporting** — Displays daily and cumulative collected waste (in kg) based on capacity-constrained sector schedules.
- **Collection Point Registration** — Registers up to 10 drop-off collection points by street number and name for dedicated tours.

### Collection Company (`Entreprise`)
- **Bulky Waste Pickup (`Encombrants`)**
  - *Single address*: Shortest path from the depot (`Int_001`) to a resident's address.
  - *Multi-address tour*: Optimized sequential pickup route across up to 10 requested addresses.
- **Door-to-Door Bin Collection (`Poubelles`)** — Computes a complete traversal passing through every street in the network from the depot.
- **Collection Points Tour (`Points de collecte`)** — Offers two selectable routing strategies: **Nearest Neighbor (TSP)** and **Minimum Spanning Tree (Prim) + DFS**.
- **Collection Day Scheduling (`Jours de collecte`)** — Groups intersections into geographic sectors (`S1`–`S4`) and assigns non-adjacent collection days under two models:
  - *Unconstrained*: Minimizes total collection days so neighboring sectors are never collected simultaneously.
  - *Fleet & Capacity Constrained*: Factors in truck capacity `C` and fleet size `N` (daily cap `C × N`), splitting high-volume sectors across multiple days when needed.

## Algorithms & Architecture

| Problem | Graph Theory Concept | Implementation |
| :--- | :--- | :--- |
| Point-to-point bulky pickup | **Dijkstra's Algorithm** (priority queue, skips blocked arcs) | [`Reseau.java`](src/Reseau.java) |
| Multi-stop tour (Nearest Neighbor) | **TSP Nearest Neighbor Heuristic** over Dijkstra distances | [`Reseau.java`](src/Reseau.java), [`TSPPointsCollecte.java`](src/TSPPointsCollecte.java) |
| Multi-stop tour (MST-based) | **Prim's MST + DFS Preorder Traversal** | [`Reseau.java`](src/Reseau.java), [`TSPPointsCollecte.java`](src/TSPPointsCollecte.java) |
| Door-to-door street coverage | **Eulerian Circuit** (stack-based Hierholzer traversal) | [`Reseau.java`](src/Reseau.java) |
| Unconstrained day scheduling | **Welsh-Powell Graph Coloring** (largest-degree-first) | [`GrapheSecteurs.java`](src/GrapheSecteurs.java) |
| Capacitated day scheduling | **Constrained Graph Coloring** with sector splitting | [`GrapheSecteurs.java`](src/GrapheSecteurs.java) |

<details>
<summary><strong>Project Structure &amp; Map Format</strong></summary>

```text
├── src/
│   ├── Main.java               # Entry point
│   ├── AffichageMenu.java      # Interactive CLI & sector graph builder
│   ├── Plan.java               # Semicolon-delimited map parser
│   ├── Reseau.java             # Road graph, Dijkstra, Euler, TSP & Prim MST
│   ├── Intersection.java       # Graph vertex
│   ├── Arc.java                # Weighted, blockable street edge
│   ├── Adresse.java            # Indexed street address
│   ├── Itineraire.java         # Computed route & total distance
│   ├── GrapheSecteurs.java     # Sector graph, Welsh-Powell & capacitated planning
│   ├── Secteur.java            # Sector vertex (waste load & assigned day)
│   └── TSPPointsCollecte.java  # Intersection-level TSP & MST solver
├── Plan.txt / Plan1.txt        # Municipal road network datasets
└── test.txt / test2.txt        # Test networks & sector scheduling input
```

**Map file format (`;` delimited):**
```text
StreetName;StartIntersection;EndIntersection;Distance;Address1;Address2;...
```

</details>

## Quick Start

**Compile and run from the repository root (Java 17+ / JDK 23):**

```bash
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

> You can also open the repository directly in **IntelliJ IDEA** and run `Main.java`.

---

## Aperçu

Développé en **Java pur** sans aucune dépendance externe, ce projet modélise un réseau routier urbain sous forme de **graphe pondéré** afin de résoudre des problématiques concrètes de logistique municipale : plus court chemin, couverture complète de voirie, tournées multi-points, gestion d'imprévus de circulation et planification de collectes sans conflits.

L'interface en ligne de commande distingue deux profils utilisateurs : la **Collectivité** et l'**Entreprise** de collecte.

## Fonctionnalités

### Collectivité
- **Soumission du plan communal** — Charge le réseau routier (`Plan.txt`) dans un graphe indexé d'intersections, d'arcs pondérés (rues) et d'adresses postales.
- **Modifications de circulation en direct** — Permet de bloquer une rue barrée (automatiquement contournée lors des calculs) ou d'ajouter une nouvelle rue avec ses intersections, sa distance et ses numéros d'adresse.
- **Suivi des déchets récoltés** — Affiche les quantités collectées par jour et le total global (en kg) à partir des tournées planifiées sous contrainte de capacité.
- **Localisation des points de collecte** — Enregistre jusqu'à 10 adresses de points d'apport volontaire pour le calcul des tournées dédiées.

### Entreprise
- **Collecte des encombrants**
  - *Particulier unique* : Plus court chemin entre le dépôt (`Int_001`) et l'adresse demandée.
  - *Tournée multi-particuliers* : Enchaînement optimisé jusqu'à 10 adresses.
- **Collecte des poubelles (porte-à-porte)** — Calcule un parcours complet passant par l'ensemble des rues du réseau depuis le dépôt.
- **Tournée des points de collecte** — Propose deux approches au choix : **Plus Proche Voisin (TSP)** et **Arbre Couvrant Minimal (Prim) + parcours en profondeur (DFS)**.
- **Planification des jours de collecte** — Découpe la commune en secteurs (`S1`–`S4`) reliés par voisinage selon deux hypothèses :
  - *Sans contrainte de capacité* : Minimise le nombre de jours afin que deux secteurs voisins ne soient jamais collectés le même jour.
  - *Avec contrainte de capacité et de flotte* : Prend en compte la capacité `C` d'un camion et le nombre de camions `N` (plafond journalier `C × N`), en fractionnant un secteur sur plusieurs jours si nécessaire.

## Algorithmes & Architecture

| Problématique | Concept de théorie des graphes | Implémentation |
| :--- | :--- | :--- |
| Trajet vers un encombrant | **Algorithme de Dijkstra** (file de priorité, ignore les arcs bloqués) | [`Reseau.java`](src/Reseau.java) |
| Tournée multi-adresses (glouton) | **Heuristique du Plus Proche Voisin (TSP)** basée sur Dijkstra | [`Reseau.java`](src/Reseau.java), [`TSPPointsCollecte.java`](src/TSPPointsCollecte.java) |
| Tournée multi-adresses (arbre) | **MST de Prim + parcours préordre DFS** | [`Reseau.java`](src/Reseau.java), [`TSPPointsCollecte.java`](src/TSPPointsCollecte.java) |
| Passage dans toutes les rues | **Circuit Eulérien** (parcours itératif par pile) | [`Reseau.java`](src/Reseau.java) |
| Planning sans capacité | **Coloration de Welsh-Powell** (tri par degré décroissant) | [`GrapheSecteurs.java`](src/GrapheSecteurs.java) |
| Planning avec capacité & flotte | **Coloration sous contrainte** avec fractionnement des secteurs | [`GrapheSecteurs.java`](src/GrapheSecteurs.java) |

<details>
<summary><strong>Structure du projet &amp; format des plans</strong></summary>

```text
├── src/
│   ├── Main.java               # Point d'entrée
│   ├── AffichageMenu.java      # Menu CLI interactif & construction des secteurs
│   ├── Plan.java               # Parseur de fichiers de plan (séparateur ;)
│   ├── Reseau.java             # Graphe routier, Dijkstra, Euler, TSP & MST de Prim
│   ├── Intersection.java       # Sommet du graphe
│   ├── Arc.java                # Arête/arc pondéré et blocable
│   ├── Adresse.java            # Adresse indexée sur un arc
│   ├── Itineraire.java         # Itinéraire calculé & distance totale
│   ├── GrapheSecteurs.java     # Graphe de secteurs, Welsh-Powell & planification
│   ├── Secteur.java            # Sommet secteur (volume & jour attribué)
│   └── TSPPointsCollecte.java  # Solveur TSP & MST sur intersections
├── Plan.txt / Plan1.txt        # Jeux de données du réseau communal
└── test.txt / test2.txt        # Réseaux de test & données de secteurs
```

**Format d'une ligne dans les fichiers de plan (`;`) :**
```text
NomRue;Intersection1;Intersection2;Distance;Adresse1;Adresse2;...
```

</details>

## Lancement rapide

**Compiler et exécuter depuis la racine du dépôt (Java 17+ / JDK 23) :**

```bash
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

> Le projet peut également être ouvert directement dans **IntelliJ IDEA** et lancé via `Main.java`.
