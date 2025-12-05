pipeline {
    agent any

    tools {
        jdk 'jdk'
        maven 'maven'
    }

    environment {
        terraformDir       = 'terraform'
 clusterName        = 'my_last_cluster' // Nom du cluster Kubernetes
        kubeConfigPath     = 'C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\test_aws\\.kube\\config' // Chemin du kubeconfig
        awsCredentialsId   = 'aws-credentials' // ID des credentials AWS dans Jenkins
        registry           = "yathrebsamaali/docker-spring-boot" // Nom de l'image Docker
        registryCredential = 'dockerhub'    // ID credential DockerHub dans Jenkins
        dockerImage        = ''             // Variable qui stockera l'image Docker
        PROMETHEUS_URL     = 'http://localhost:9090' // URL Prometheus
        GRAFANA_URL        = 'http://localhost:3000' // URL Grafana
        PATH               = "C:\\Users\\Pc\\terraform;${env.PATH}" // Chemin Terraform si nécessaire
    }

    stages {

        // ===============================
        stage('GIT CHECKOUT') {
            steps {
                echo '📥 CLONAGE DU DÉPÔT GIT...'
                git branch: 'YathrebSamaali-2MPAWI-G6',
                    url: 'https://github.com/Chaimaabidii/2MPAWI-G6-Station-Ski.git'
            }
        }

        // ===============================
        stage('MVN CLEAN') {
            steps {
                echo '🏗️ COMPILATION DU PROJET...'
                bat 'mvn clean package -DskipTests'
            }
        }

        // ===============================
        stage('UNIT TESTS') {
            steps {
                script {
                    echo '🧪 EXÉCUTION DES TESTS UNITAIRES...'
                    try {
                        bat 'mvn test'
                    } catch (err) {
                        echo '⚠ TESTS ÉCHOUÉS → PIPELINE MARQUÉ UNSTABLE.'
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }

        // ===============================
        stage('MVN SONARQUBE') {
            steps {
                echo '🔍 ANALYSE SONARQUBE...'
                withCredentials([string(credentialsId: 'sonar', variable: 'SONAR_TOKEN')]) {
                    bat """
                        mvn sonar:sonar ^
                        -Dsonar.projectKey=gestion-station-skii ^
                        -Dsonar.host.url=http://localhost:9000 ^
                        -Dsonar.login=%SONAR_TOKEN%
                    """
                }
            }
        }

        // ===============================
        stage('PUBLISH TO NEXUS') {
            steps {
                echo '📦 PUBLICATION SUR NEXUS...'
                bat 'mvn deploy -DskipTests'
            }
        }

        // ===============================
        stage('BUILDING DOCKER IMAGE') {
            steps {
                script {
                    echo '🐳 CONSTRUCTION DE L’IMAGE DOCKER...'
                    dockerImage = docker.build("${registry}:${BUILD_NUMBER}", ".")
                    echo '📤 PUSH DE L’IMAGE SUR DOCKERHUB...'
                    docker.withRegistry('', registryCredential) {
                        dockerImage.push("${BUILD_NUMBER}")
                        dockerImage.push('latest')
                    }
                }
            }
        }

        // ===============================
        stage('DEPLOY OUR IMAGE') {
            steps {
                script {
                    echo '🚀 DÉMARRAGE DU CONTENEUR LOCAL...'
                    bat """
                        docker stop springboot-container || exit 0
                        docker rm springboot-container || exit 0
                        docker pull ${registry}:latest
                        docker run -d -p 8080:8080 --name springboot-container ${registry}:latest
                    """
                }
            }
        }

        // ===============================
        stage('VERIFY PROMETHEUS METRICS') {
            steps {
                echo '📊 VÉRIFICATION DES MÉTRIQUES PROMETHEUS...'
                powershell """
                    try {
                        \$resp = Invoke-WebRequest -Uri '${PROMETHEUS_URL}/api/v1/targets' -UseBasicParsing
                        Write-Host '✅ PROMETHEUS ACCESSIBLE'
                    } catch {
                        Write-Host '⚠️ PROMETHEUS NON ACCESSIBLE'
                    }

                    try {
                        \$resp2 = Invoke-WebRequest -Uri 'http://localhost:8080/actuator/prometheus' -UseBasicParsing
                        Write-Host '✅ METRICS ENDPOINT DISPONIBLE'
                    } catch {
                        Write-Host '⚠️ METRICS ENDPOINT NON DISPONIBLE'
                    }
                """
            }
        }

        // ===============================
        stage('VERIFY GRAFANA') {
            steps {
                echo '📈 VÉRIFICATION DE GRAFANA...'
                powershell """
                    try {
                        \$resp = Invoke-WebRequest -Uri '${GRAFANA_URL}/api/health' -UseBasicParsing
                        Write-Host '✅ GRAFANA ACCESSIBLE'
                    } catch {
                        Write-Host '⚠️ GRAFANA NON ACCESSIBLE'
                    }
                """
            }
        }

        // ===============================
        stage('TEST AWS CREDENTIALS') {
            steps {
                withCredentials([file(credentialsId: awsCredentialsId, variable: 'AWS_CREDENTIALS_FILE')]) {
                    script {
                        echo '🔑 TEST DES CREDENTIALS AWS...'
                        def awsCreds = readFile(AWS_CREDENTIALS_FILE).trim().split("\n")
                        env.AWS_ACCESS_KEY_ID     = awsCreds.find { it.startsWith("aws_access_key_id") }.split("=")[1].trim()
                        env.AWS_SECRET_ACCESS_KEY = awsCreds.find { it.startsWith("aws_secret_access_key") }.split("=")[1].trim()
                        env.AWS_SESSION_TOKEN     = awsCreds.find { it.startsWith("aws_session_token") } ?
                                                    awsCreds.find { it.startsWith("aws_session_token") }.split("=")[1].trim() : ""
                        echo "✅ AWS_ACCESS_KEY_ID: ${env.AWS_ACCESS_KEY_ID}"

                        echo '📦 TEST DE LA CONNEXION AWS...'
                        bat 'aws sts get-caller-identity'
                    }
                }
            }
        }

        // ===============================
        stage('SETUP TERRAFORM') {
            steps {
                dir(terraformDir) {
                    script {
                        echo '📂 INITIALISATION DE TERRAFORM...'
                        bat 'terraform init'
                        bat 'terraform validate'
                        bat 'terraform apply -auto-approve'
                        echo '✅ TERRAFORM APPLIQUÉ AVEC SUCCÈS'
                    }
                }
            }
        }

        // ===============================
        stage('UPDATE KUBECONFIG') {
            steps {
                script {
                    echo '☸️ MISE À JOUR DU KUBECONFIG...'
                    bat "IF NOT EXIST C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\test_aws\\.kube mkdir C:\\ProgramData\\Jenkins\\.jenkins\\workspace\\test_aws\\.kube"
                    bat "IF EXIST ${kubeConfigPath} del /F ${kubeConfigPath}"
                    bat "aws eks update-kubeconfig --name ${clusterName} --region us-east-1 --kubeconfig ${kubeConfigPath}"
                    echo "✅ KUBECONFIG MIS À JOUR"
                }
            }
        }

        // ===============================
        stage('DEPLOY TO AWS KUBERNETES') {
            steps {
                script {
                    env.KUBECONFIG = kubeConfigPath
                    echo '🔹 VÉRIFICATION DES NŒUDS KUBERNETES...'
                    bat 'kubectl get nodes'

                    echo '📦 DÉPLOIEMENT DES RESSOURCES KUBERNETES...'
                    bat """
                    IF EXIST deployment.yaml (
                        kubectl apply -f deployment.yaml
                    ) ELSE (
                        echo '❌ deployment.yaml INTRUVABLE'
                    )

                    IF EXIST service.yaml (
                        kubectl apply -f service.yaml
                    ) ELSE (
                        echo '❌ service.yaml INTRUVABLE'
                    )
                    """
                    echo '✅ DEPLOYMENT TERMINÉ'
                }
            }
        }

    } // end stages

    post {
        always {
            echo '🧹 NETTOYAGE...'
            bat 'docker image prune -f'
            junit '**/target/surefire-reports/*.xml'
        }
        success {
            echo '✅ PIPELINE TERMINÉE AVEC SUCCÈS.'
        }
        unstable {
            echo '⚠ PIPELINE INSTABLE — TESTS ÉCHOUÉS.'
        }
        failure {
            echo '❌ PIPELINE ÉCHOUÉE — VÉRIFIE LES LOGS.'
        }
    }
}
