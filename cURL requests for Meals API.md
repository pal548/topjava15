### TopJava Backend API reference 

#### API for Meals of User

All operations on meals is for logged in user.

##### 1. Get All meals 
```
curl http://localhost:8080/topjava/rest/meals
```

##### 2. Get single meal 
```
curl http://localhost:8080/topjava/rest/meals/100006
```

##### 3. Crete meal 
```
curl -X POST \
   http://localhost:8080/topjava/rest/meals \
   -H 'Content-Type: application/json' \
   -d '{
         "dateTime": "2018-05-31T20:03:00",
         "description": "Ужин",
         "calories": 510
     }'
```

##### 4. Update meal 
```
curl -X PUT \
  http://localhost:8080/topjava/rest/meals/100014 \
  -H 'Content-Type: application/json' \
  -d '{
	"id": 100014,
    "dateTime": "2018-05-31T20:08:00",
    "description": "Ужин",
    "calories": 512
}'
```
(note that id in URL and id in JSON must be identical)

##### 5. Delete meal

```
curl -X DELETE http://localhost:8080/topjava/rest/meals/100014
```
