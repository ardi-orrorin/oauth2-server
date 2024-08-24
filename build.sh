clear

echo proejct build start....
echo

version=$1
docker_id=$2
#platform=linux/amd64,linux/arm64
platform=linux/amd64

echo 'start oauth2 server project build start....'

./gradlew build -Pversion=$version

echo 'start oauth2 server-anamensis project build success....'

server_file_name='oauth2-'$version''

echo

echo 'docker build start....'

echo

echo 'docker latest build start....'

docker buildx build --platform $platform --push --build-arg='JAR='$server_file_name -t $docker_id/oauth2-server:latest  -f oauth2.Dockerfile  .

echo 'docker latest build success....'

echo

echo 'oauth2-server-docker-compose build start....'

docker buildx build --platform $platform --push --build-arg='JAR='$server_file_name -t $docker_id/oauth2-server:$version -f oauth2.Dockerfile  .

echo 'oauth2-server-docker-compose build success....'

echo 'docker build success....'

echo

echo 'build success....'