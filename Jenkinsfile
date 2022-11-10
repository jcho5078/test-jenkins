node {
  stage('========== Clone repository ==========') {
    steps {
        echo 'clonning repository'
        git url: 'https://github.com/jcho5078/testRds2.git',
            branch: 'master'
            credentialsId: 'jenkins-token'
    }
    post {
        success {
            echo 'success clone'
        }
        failure {
            error 'fail clone'
        }
    }
  }
  stage('========== Build image ==========') {
    echo 'Build Docker'
    script {
        docker Image = docker.build "asia.gcr.io/graphite-ruler-366202/testRds:${BUILD_NAME}"
    }
    post {
        failure {
            error 'build fail'
        }
    }
  }
  stage('========== Push image ==========') {
    agent any
    steps {
        echo 'push image'
        script {
            docker.withRegistry('', jcho5078){
                dockerImage.push("1.0")
            }
        }
    }
    post {
        failure {
            error 'push fail'
        }
    }
  }
}