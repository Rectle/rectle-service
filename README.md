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

## Deploy (change version)
```
docker build -t image .
docker tag image europe-central2-docker.pkg.dev/rectle-app/rectle-container/rectle-service:0.0.1
docker push europe-central2-docker.pkg.dev/rectle-app/rectle-container/rectle-service:0.0.1
```
### In case u need to authenticate use 
* gcloud auth login 
* gcloud auth configure-docker

## Running project locally
Add environment variables like this:
* GOOGLE_APPLICATION_CREDENTIALS='path to json credentials'

