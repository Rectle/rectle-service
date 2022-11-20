# Service for rectle

## Example of running mysql db locally

``` 
docker run 
--name mysqldb 
-p 3308:3306 
-e MYSQL_ROOT_PASSWORD=root 
-e MYSQL_DATABASE=rectledb 
-e MYSQL_USER=user 
-e MYSQL_PASSWORD=user 
-d mysql:8 
```

## Deploy
