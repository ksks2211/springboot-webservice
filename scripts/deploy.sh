#! /bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=springboot-webservice


echo "> Build 파일 복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY


echo "> Current PID"
CURRENT_PID=$(pgrep -fl springboot-web-project | grep java | awk '{print $1}')
echo "pid : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
	echo "> 구동중인 애플리케이션 없음"
else
	echo "> Kill -15 $CURRENT_PID"
	kill -15 $CURRENT_PID
	sleep 5
fi

echo "> New Appication Deploy..."
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1 )
echo "> JAR_NAME: $JAR_NAME"

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

nohup java -jar -Dspring.config.location=classpath:/application.properties,classpath:/application-real.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-real-db.properties  -Dspring.profiles.active=real $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &