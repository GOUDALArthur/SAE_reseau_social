# Manuel d'utilisation

## Lancement
Pour lancer l'application, il faut d'abord ouvrir 2 terminaux, ensuite lancer dans le 1er terminal le [fichier launchServeur](./launchServeur.sh). Ensuite sur le 2e terminal lancer le [fichier launchClient](./launchClient.sh). Vous pouvez lancer autant de clients dans des terminaux que vous voulez.

## Connexion
Quand un client lance un message `Connexion au serveur localhost sur le port 8080` qui signifie que le client se connecte au serveur.
Entrer le nom d'utilisateur sous le message dédiés.
Si le compte n'existe pas le serveur, va, vous proposez de le créer ; répondez avec O pour oui et N pour non.

Maintenant, que vous avez répondu O Bienvenue sur notre serveur !

## Les différentes commandes
Maintenant, que vous êtes connectés, vous avez plusieurs fonctions possibles.

- #### Envoyer un message
Juste taper le texte et appuyer sur Enter, il sera affiché pour tous vos followers.

- #### S'abonner à un autre utilisateur
Avec la commande `/follow ` afin de voir tout les messages envoyés part cet utilisateur.

- #### Se desabonner d'un utilisateur
Avec la commande `/unfollow ` pour arreter de suivre un utilisateur et donc de ne plus voir ses messages.

- #### Aimer un message
Avec la commande `/like ` quand on reçoit un message un id s'affiche. Avec on peut like le message avec cette commande

- #### Ne plus aimer un message
Avec la commande `/dislike `

- #### Supprimer un de ses messages
Avec la commande `/delete ` pour supprimer son message.

Arthur Goudall
Noam Doucet