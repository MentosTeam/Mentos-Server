REPOSITORY=/home/ubuntu/app/mentos
PROJECT_NAME=Mentos-Server

cd $REPOSITORY/$PROJECT_NAME/

echo "> Git Pull"

git pull origin main

echo "> Start Building Project"

sudo ./gradlew clean build

echo "> Change Directory to Test"

cd $REPOSITORY

echo "> Copy Build Files"

cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "> Check Current Application Pids"

# pgrep : process id만 추출
# -f : process 이름으로 검색
CURRENT_PID=$(pgrep -f mentos)

echo "> Current Application Pids : $CURRENT_PID"

if [ -z "$CURRENT_PID" ] ; then
        echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."

else
        echo "> kill -9 $CURRENT_PID"
        kill -9 $CURRENT_PID
        sleep 20
fi

echo "> Deploy New Application"

# 새로 실행할 jar파일 찾아서
# 여러 jar파일 중 가장 마지막(최신) 파일을 변수에 저장
JAR_NAME=$(ls $REPOSITORY/ | grep 'mentos' | tail -n 1)

echo "> JAR NAME :$JAR_NAME"

nohup java -jar $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &