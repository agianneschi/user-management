echo "###############################################################################################################"
echo "1. Copy jar from target"
echo "  cp ../code/target/usermanagement-*jar ."
cp ../code/target/usermanagement-*jar .

echo "###############################################################################################################"
echo "2. Docker build"
echo "  docker build --tag=user-management:1.0.0.0 ../code/"
docker build --tag=user-management:1.0.0.0 ../code/

echo "###############################################################################################################"
echo "3. Stop previously container "
echo "  docker stop user-management"
docker stop user-management

echo "###############################################################################################################"
echo "4. Remove previously container "
echo "  docker rm user-management"
docker rm user-management

echo "###############################################################################################################"
echo "5. Run new container "
echo "  docker run --name user-management -d -p 8080:8080 user-management:1.0.0.0"
docker run --name user-management -d -p 8080:8080 user-management:1.0.0.0
