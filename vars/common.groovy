def sonarChecks(component){
    sh " echo Starting the quality check..."
    sh  "sonar-scanner -Dsonar.host.url=http://${SONAR_URL}:9000 ${ARGS} -Dsonar.projectKey=${component}  -Dsonar.login=${SONAR_USR} -Dsonar.password=${SONAR_PSW}"
    sh  "curl https://gitlab.com/thecloudcareers/opensource/-/raw/master/lab-tools/sonar-scanner/quality-gate > quality-gata.sh"
    sh  "bash -x quality-gata.sh ${SONAR_USR} ${SONAR_PSW} ${SONAR_URL} ${component}"
    sh " echo sonar checks completed for ${component}.....!!!!!"
}