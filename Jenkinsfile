pipeline {
  agent {
    node {
      label 'main'
    }
  }
  stages {
    stage('checkout') {
      steps {
        checkout(
          [$class: 'GitSCM', 
           branches: [[name: '*/studDevel']], 
           doGenerateSubmoduleConfigurations: false, 
           extensions: [], 
           submoduleCfg: [], 
           userRemoteConfigs: [[url: 'https://github.com/id23cat/DataMiningCombiner.git']]])
      }
    }
    
    stage('build') {
      steps {
        sh 'mvn -Dmaven.test.failure.ignore=true package'
    }
  }
}
