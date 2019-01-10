pipeline {
  agent any
  stages {
    stage('test') {
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
  }
}
