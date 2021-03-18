 # File Storage REST service

 Application that allows us to store files in the cloud, categorize them with tags and search through them.
 
 Technologies:
 
 - Spring Boot
 - Elasticsearch
 
 ## Installation

 ```bash
  cd src/scripts
  chmod +x build.sh run.sh
```

build project 

```bash
  ./build.sh
```

and run project


```bash
  ./run.sh
```
 ## Endpoints
1. Upload
   
   POST /file


2. Delete file
   
   DELETE  /file/{ID}


3. Assign tags to file
   
   POST /file/{ID}/tags


4. Remove tags from file
   
   DELETE /file/{ID}/tags



5. List files with pagination optionally filtered by tags 
   
   GET /file?tags=tag1,tag2,tag3&page=2&size=3

 ### Properties

```
    server.port = 8081

    elasticsearch.host = localhost
    elasticsearch.port = 9200
```
