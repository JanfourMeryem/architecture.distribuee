# Architecture distribuée — TP2

Deux API REST Spring Boot illustrent la gestion d’étudiants et de cours, la communication HTTP entre services et leur déploiement avec Docker Compose.

| Service | Port local | Rôle |
| --- | --- | --- |
| `ServiceEtudiants` | `8081` | Créer, consulter, modifier et supprimer des étudiants. |
| `ServiceCours` | `8082` | Créer, consulter et supprimer des cours, puis afficher le nom de l’étudiant associé. |

Un cours conserve l’identifiant de l’étudiant dans `etudiantId`. Quand on appelle `GET /cours/enrichis`, `ServiceCours` récupère le nom auprès de `ServiceEtudiants`. Si l’étudiant est introuvable ou si son service est indisponible, le cours reste dans la réponse avec `etudiantNom: "INCONNU"`. Les données sont conservées en mémoire et disparaissent au redémarrage du service qui les gère.

## Structure du dépôt

```text
ServiceEtudiants/   API étudiants et tests Maven
ServiceCours/       API cours, client HTTP et tests Maven
tests/postman/      Collections Postman des services et scénarios de la partie D
docs/               Rapport du TP2 et captures
docker-compose.yml  Déploiement local des deux services
```

## Démarrage avec Docker

Prérequis — Java 17, Maven 3 et Docker avec Compose. Démarrer Docker, puis exécuter ces commandes à la racine du dépôt. Les ports 8081 et 8082 doivent être libres.

```sh
mvn -f ServiceEtudiants/pom.xml clean package
mvn -f ServiceCours/pom.xml clean package
docker compose up --build
```

Les deux commandes Maven compilent les applications et exécutent leurs tests. Compose démarre les services et configure l’adresse interne de `ServiceEtudiants` pour que `ServiceCours` puisse le joindre. À l’arrêt, utiliser `Ctrl+C` dans le terminal Compose ou lancer `docker compose down` dans un autre terminal.

## Routes principales

| Service | Méthode et route | Résultat attendu |
| --- | --- | --- |
| Étudiants | `GET /etudiants`, `GET /etudiants/{id}` | `200 OK` pour une lecture réussie |
| Étudiants | `POST /etudiants` | `201 Created` |
| Étudiants | `PUT /etudiants/{id}` | `200 OK` |
| Étudiants | `DELETE /etudiants/{id}` | `204 No Content` |
| Cours | `GET /cours`, `GET /cours/{id}` | `200 OK` pour une lecture réussie |
| Cours | `POST /cours` | `200 OK` |
| Cours | `DELETE /cours/{id}` | `200 OK` |
| Cours | `GET /cours/enrichis` | `200 OK`, avec `etudiantNom` |

Les listes sont accessibles sur [localhost:8081/etudiants](http://localhost:8081/etudiants), [localhost:8082/cours](http://localhost:8082/cours) et [localhost:8082/cours/enrichis](http://localhost:8082/cours/enrichis).

## Tests et démonstration

1. Importer la [collection de la partie D](tests/postman/TP2-Partie-D.postman_collection.json) dans Postman. Dans Postman Web, sélectionner **Desktop Agent** pour joindre les services locaux.
2. Exécuter le dossier **Services démarrés** dans l’ordre. Il couvre la création, la lecture, l’enrichissement et l’effet de la suppression d’un étudiant.
3. Pour la simulation de panne, recréer Sara et son cours avec les requêtes de création, puis vérifier que le cours enrichi contient `Sara`. Exécuter `docker compose stop etudiants` et envoyer la requête du dossier **Panne**. Le cours doit rester accessible avec `etudiantNom: "INCONNU"`. Relancer ensuite le service avec `docker compose start etudiants`.

Les collections [ServiceEtudiants](tests/postman/ServiceEtudiants.postman_collection.json) et [ServiceCours](tests/postman/ServiceCours.postman_collection.json) permettent de rejouer les tests par service. Le [rapport du TP2](docs/TP2-Rapport.pdf) contient les trois captures de l’exécution Docker.
