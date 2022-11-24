# Service for rectle

## Example of running mysql db locally

``` 
docker run 
--name mysqldb 
-p 3306:3306 
-e MYSQL_ROOT_PASSWORD=root 
-e MYSQL_DATABASE=rectledb 
-e MYSQL_USER=user 
-e MYSQL_PASSWORD=user 
-d mysql:8 
```

## Deploy (temporary)
```
docker build -t image .
docker tag image eu.gcr.io/rectle-platform/rectle-container
docker push eu.gcr.io/rectle-platform/rectle-container
```
### In case u need to authenticate use 
* gcloud auth login 
* gcloud auth configure-docker

