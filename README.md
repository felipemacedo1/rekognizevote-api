# RekognizeVote API

Sistema de votação seguro com verificação facial usando AWS Rekognition.

## 🏗️ Arquitetura

- **Hexagonal Architecture** (Ports & Adapters)
- **Domain-Driven Design** (DDD)
- **CQRS** (Command Query Responsibility Segregation)
- **Event-Driven Architecture**
- **Reactive Programming** com Spring WebFlux

## 🚀 Tecnologias

- **Java 21** + **Spring Boot 3.2**
- **Spring WebFlux** (Programação Reativa)
- **Spring Security** + **JWT**
- **AWS SDK v2** (DynamoDB, S3, Rekognition)
- **Docker** + **LocalStack** para desenvolvimento

## 📡 Endpoints

### Autenticação
- `POST /api/v1/auth/register` - Registro de usuário
- `POST /api/v1/auth/login` - Login
- `GET /api/v1/auth/me` - Dados do usuário atual

### Polls
- `GET /api/v1/polls` - Listar polls (com filtro por status)
- `GET /api/v1/polls/{id}` - Detalhes de uma poll
- `POST /api/v1/polls/{id}/vote` - Votar com verificação facial

### Upload
- `GET /api/v1/upload/presigned-url` - URL para upload de imagem

## 📘 Documentação da API

A documentação completa dos endpoints está disponível via **Swagger UI** após iniciar a aplicação:

```
http://localhost:8080/swagger-ui.html
```

## 🔧 Desenvolvimento

### Pré-requisitos
- Java 21
- Docker & Docker Compose
- Maven

### Executar Localmente

```bash
# Subir serviços AWS locais
docker-compose up -d

# Executar aplicação
mvn spring-boot:run
```

### Configuração

```yaml
# application.yml
aws:
  region: us-east-1
  dynamodb:
    endpoint: http://localhost:8000  # LocalStack
  s3:
    endpoint: http://localhost:4566  # LocalStack
    bucket: rekognizevote-dev
  rekognition:
    endpoint: http://localhost:4566  # LocalStack

jwt:
  secret: dev-secret-key-change-in-production
  expiration: 3600000  # 1 hour
  refresh-expiration: 2592000000  # 30 days
```

## 🧪 Testes

```bash
# Executar testes
mvn test

# Executar com cobertura
mvn test jacoco:report
```

## 📊 Monitoramento

- **Actuator**: `/actuator/health`, `/actuator/metrics`
- **Prometheus**: `/actuator/prometheus`
- **Logs estruturados** com correlation IDs

## 🔒 Segurança

- **JWT** com RS256
- **Certificate Pinning** suportado
- **Rate Limiting** configurável
- **Validação de entrada** em todos os endpoints
- **CORS** configurado para mobile apps

## 🏃♂️ Deploy

### Docker
```bash
docker build -t rekognizevote-api .
docker run -p 8080:8080 rekognizevote-api
```

### AWS
- Deploy via **AWS Lambda** + **API Gateway**
- **DynamoDB** para persistência
- **S3** para armazenamento de imagens
- **Rekognition** para verificação facial

## 📱 App Android

Este projeto é a API backend para o app Android RekognizeVote que já está em produção com:
- Clean Architecture + MVVM
- Material Design 3
- CameraX para captura de imagem
- Certificate Pinning
- Testes unitários e de UI

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request