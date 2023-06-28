# Tenpo Challenge

### Métodos

`POST /calculate` Recibe 2 números, los suma y a ese resultado le suma un porcentaje retornado desde otro servicio.

```
curl --location 'http://localhost:8085/tenpo/numbers/calculate' \
--header 'Content-Type: application/json' \
--data '{
    "x": 45,
    "y": 16
}'
```

`GET /history` Retorna los registros de cada request y respuesta que recibió el servicio.

```
curl --location 'http://localhost:8085/tenpo/history?page=0&size=2'
```

Dentro del proyecto hay un archivo llamado `tenpo.postman_collection.json` tiene ambos llamados y ejemplos de casos exitosos y error.

### Base de datos
Cada request y respuesta del servicio se guarda en una tabla llamada `history`.
Según el endpoint llamado, en cada registro se guarda un `type`:

- `CALCULATE` para POST /calculate
- `HISTORY` para GET /history

Para el caso de GET /history, en la respuesta se guarda la cantidad de resultados obtenidos.

```
{numberOfElements: 2}
```

### Iniciar el servicio
Ejecutar el siguiente comando:

```
docker-compose up
```

Los puertos expuestos en el `docker-compose.yml` deben coincidir con los puertos definidos en el `application.properties`

### Pruebas unitarias

Ejecutar lo siguiente en el directorio donde esté ubicado el servicio. Está paginado así que debe indicar la pagina y cantidad de resultados que desea
```
cd tenpo/
mvn test
```

### Notas

#### Propiedades

Describo alguna propiedades usadas

- `request.limit` Solicitudes permitidas por minuto.
- `webclient.max.attempts` Cantidad máxima de intentos en caso de error al consultar el servicio externo.
- `webclient.seconds.delay` Intervalo entre intentos, en segundos. 
- `cache.expire.time` Cantidad de minutos en que expiran los registros en caché.
- `cache.max.size` Tamaño máximo de la cache.

#### Prueba local
Si desea correr este proyecto de forma local, primero debe bajar la imagen de postgres

```
docker pull postgres
```

Y luego iniciar el contenedor
```
docker run --name tenpo-postgres \
-e POSTGRES_PASSWORD=secretclave -e POSTGRES_DB=tenpo \
-p 5432:5432 \
-d postgres
```

Y la property a editar es

```
spring.r2dbc.url=r2dbc:postgresql://localhost:5432/tenpo
```

