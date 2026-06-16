pipeline {
    agent any

    stages {
		tools {
        maven 'Maven3'
    	}

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn clean test'
            }
        }
    }

    post {
    	 always {
    	 	echo '***********************************'
            junit 'target/surefire-reports/*.xml'
            echo '***********************************'
        }
        
        success {
        	echo '***********************************'
            echo '*********Build Successful*********'
            echo '***********************************'
        }

        failure {
            echo '***********************************'
            echo '***********Build Failed***********'
            echo '***********************************'
        }
    }
}