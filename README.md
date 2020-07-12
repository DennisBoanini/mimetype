# MimeType verificator
![Java CI with Maven](https://github.com/DennisBoanini/mimetype/workflows/Java%20CI%20with%20Maven/badge.svg?branch=develop)

L'applicazione realizzata è composta di due profili **dev** e **demo**. 

### Profilo dev
Il profilo **dev** è il profilo attivo di default, quindi eseguendo l'applicazione dall'IDE questo è il profilo che verrrà utilizzato.
Per eseguire l'applicazione da linea di comando, con questo profilo, utilizzare il comando `mvn spring-boot:run -Dspring-boot.run.profiles=dev`.
Il profilo **dev** è impostato per utilizzare il database in memory **H2** che ha, precaricate, le tabelle **MIME_TYPE** e **USERS**. La prima contiene i dati relativi ai mime type accettati, la seconda è una anagrafica utente per la registrazione e login.

### Profilo demo
Il profilo **demo** non è attivo di default, deve quindi essere impostato per eseguire l'applicazione con questo profilo.
Per eseguire l'applicazione da linea di comando, con questo profilo, utilizzare il comando `mvn spring-boot:run -Dspring-boot.run.profiles=demo`.
Il profilo **demo** è impostato per connettersi a DB MySQL, le cui impostazioni di connessione sono presenti nel file *application-demo.properties*.

## Endpoint

Gli endpoint dell'applicazione sono suddivisi in due file **JwtAuthenticationController** e **MimeTypeRestController**. Il primo non è sotto autenticazione, il secondo sì.

### JwtAuthenticationController
E' il controller responsabile della registrazione e autenticazione dell'utente.
**BASE_URL**: */auth*

 - **POST** */register*: Endpoint per la registrazione dell'utente. Il body deve **obbligatoriamente** contenere un oggetto così formato
```
{
	"username": "user", [String]
	"password": "user"  [String]
}
```
Sia *username* che *password* sono obbligatori.
Se la registrazione va a buon fine la risposta sarà *201 Created*
 - **POST** */token*: Endpoint per l'autenticazione utente. Il body deve **obbligattoriamente** contenere username e password, in questo modo
 ```
{
	"username": "user", [String]
	"password": "user"  [String]
}
```
Una risposta positiva sarà un *200 OK* e questo oggetto contenente il token generato
```
{
	"jwtToken":"eyJhbGciOiJIUzUxMiJ0.eyJzdWIiOiJkZW5uaXMiLCJpc3MiOiJEZW1vX0FwcGxpY2F0aW9dauIiwiZXhwIjoxNTk0NTc5ODI5LCJpYXQiOjE1OTQ1NjE4Mjl9.iIzQw5EfVhfdGyK3gC8vXIPVGOII58AvEsFtallB_f9zC62Ddaq8vt8t4FS22PDAFX5jKOufgB0tmRGCkPU5aA",
	"jwtTokenType": "Bearer"

}
```
Tale token è da utilizzare per tutte le altre chiamate.


### MimeTypeRestController
E' il controller per la gestione delle risorse mime type.
**BASE_URL**: */mime-types/v1*

 - **GET**: Restituisce la lista di tutti i mime type presenti su DB
 ```
 [
	 {
		"id": 1,
		"type": "image/jpeg",
		"extension": "jpeg",
		"description": "JPEG images"
	},
	{
		"id": 2,	
		"type": "image/jpeg",
		"extension": "jpg",
		"description": "JPEG images"
	},
	...
]
 ```
 - **POST** */validate-files*: Vuole come param una lista di file (MultipartFile) sui quali effettuare la validazione. La risposta è un oggetto così formato
 ```
 [
	{
		"filename": "test.pdf",
		"validated": true
	},
	{
		"filename": "test.pdf.p7m",
		"validated": true
	},
	{
		"filename": "test.pdhf.p7m",
		"validated": false
	}
]
 ```
 - **GET** */validate-folder?folderPath=xxx*: Prende come param il percorso di una folder da validare. Se la folder non esiste restituisce errore, altrimenti restituisce una risposta paginata
```
{
	"content": [
		{
			"filename": "test.pdhf.p7m",
			"validated": false
		},
		{
			"filename": "test.pdf.p7m",
			"validated": true
		},
		{
			"filename": "test.pdf",
			"validated": true
		},
		{
			"filename": "test-copy.xml.p7m",
			"validated": false
		}
	],
	"pageable": {
		"sort": {
			"sorted": false,
			"unsorted": true,
			"empty": true
		},
		"pageNumber": 0,
		"pageSize": 20,
		"offset": 0,
		"paged": true,
		"unpaged": false
	},
	"totalPages": 1,
	"totalElements": 4,
	"last": true,
	"numberOfElements": 4,
	"number": 0,
	"size": 20,
	"sort": {
		"sorted": false,
		"unsorted": true,
		"empty": true
	},
	"first": true,
	"empty": false
}
```


