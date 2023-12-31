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

            stage('Preparing the artifact') {
                // when { 
                //     expression { env.TAG_NAME != null } 
                //     }
                steps {
                    sh ''' 
                        mvn clean package
                        mv target/${component}-1.0.jar ${component}.jar
                        zip -r ${component}-${TAG_NAME}.zip ${component}.jar
                    '''
                }
            }
            

        }
    }
}

