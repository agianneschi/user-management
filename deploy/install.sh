echo "###############################################################################################################"
echo "1. Excecute mvn package"
echo "  mvn -f ../code/pom.xml clean package -Dmaven.test.skip"
mvn -f ../code/pom.xml clean package -Dmaven.test.skip


echo "###############################################################################################################"
echo "2. Copy jar from target"
echo "  cp ../code/target/usermanagement-*jar ."
cp ../code/target/usermanagement-*jar .

echo "###############################################################################################################"
echo "3. Docker build"
echo "  docker build --tag=user-management:1.0.0.0 ../code/"
docker build --tag=user-management:1.0.0.0 ../code/

echo "###############################################################################################################"
echo "4. Stop previously container "
echo "  docker stop user-management"
docker stop user-management

echo "###############################################################################################################"
echo "5. Remove previously container "
echo "  docker rm user-management"
docker rm user-management

echo "###############################################################################################################"
echo "6. Run new container "
echo "  docker run --name user-management -d -p 8080:8080 user-management:1.0.0.0"
docker run --name user-management -d -p 8080:8080 user-management:1.0.0.0
