def lintChecks(component){
    sh "echo Installing Maven"
    // sh "sudo yum install maven -y"
    // sh "mvn checkstyle:checkstyle"
    sh " echo lint checks completed for ${component}.....!!!!!"
}


def call(component)
{
    pipeline {
        agent any

        environment {
            //SONAR = credentials('sonar')
            SONAR_URL = "172.31.0.179"
            NEXUS = credentials('NEXUS')
            NEXUS_URL ="172.31.0.167"

        }
        stages{
            stage('Lint checks'){
                steps {
                    script
                    {
                        lintChecks(component)
                    }
                }
            }

            stage('Sonar checks'){
                steps {
                    script
                    {
                        env.ARGS= "-Dsonar.java.binaries=target/"
                        common.sonarChecks(component)
                    }
                }
            }
            stage('Test Cases') {
                    parallel {
                        stage('Unit Tests'){
                            steps {
                                sh "echo Unit Testing ......."
                            }
                        }

                        stage('Integration Tests'){
                            steps {
                                sh "echo Integration Testing ......."
                            }
                        }

                        stage('Functional Tests'){
                            steps {
                                sh "echo Functional Testing ......."
                            }
                        }
                    }
            }

            stage('Artifact Validation On Nexus') {
                when { 
                    expression { env.TAG_NAME != null } 
                    }
                steps {
                    sh "echo checking whether artifact exists of not. If it doesnt exist then only proceed with Preparation and Upload"
                    script {
                        env.UPLOAD_STATUS=sh(returnStdout: true, script: "curl -L -s http://3.89.195.68:8081/service/rest/repository/browse/shipping/ | grep ${component}-${TAG_NAME}.zip || true" )
                    }
                }
            }

            stage('Preparing the artifact') {
                when { 
                    expression { env.TAG_NAME != null } 
                    expression { env.UPLOAD_STATUS == "" }
                    }
                steps {
                    sh "mvn clean package"
                    sh "mv target/${component}-1.0.jar ${component}.jar"
                    sh "zip -r ${component}-${TAG_NAME}.zip ${component}.jar"

                }
            }

            stage('Uploading the artifact') {
                when { 
                    expression { env.TAG_NAME != null } 
                    expression { env.UPLOAD_STATUS == "" }
                    }
                steps {
                    sh "curl -f -v -u ${NEXUS_USR}:${NEXUS_PSW} --upload-file ${component}-${TAG_NAME}.zip http://${NEXUS_URL}:8081/repository/${component}/${component}-${TAG_NAME}.zip"
                }
            }
        }
    }
}

