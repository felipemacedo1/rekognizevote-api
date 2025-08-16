# RekognizeVote API - MVP Setup Guide

## 🎯 **Status do MVP**

### ✅ **Implementado**
- ✅ Sistema de autenticação completo (register, login, refresh, me)
- ✅ Estrutura de polls e candidatos
- ✅ Sistema de votação com validações
- ✅ Upload de imagens via S3 pre-signed URLs
- ✅ Configuração AWS (DynamoDB, S3, Rekognition)
- ✅ Controllers REST completos
- ✅ Verificação facial (mock para MVP)

### ⚠️ **Pendente para Produção**
- ❌ Implementação real do AWS Rekognition
- ❌ Repositories DynamoDB (usando mocks)
- ❌ Extração de userId do JWT
- ❌ Validação de acesso a polls privadas
- ❌ Rate limiting e segurança
- ❌ Testes unitários

---

## 🚀 **Quick Start**

### 1. **Subir Ambiente Local**
```bash
# Subir DynamoDB Local e LocalStack
docker-compose up -d

# Aguardar serviços iniciarem
sleep 10

# Inicializar tabelas
chmod +x scripts/init-dynamodb.sh
./scripts/init-dynamodb.sh

# Executar aplicação
mvn spring-boot:run
```

### 2. **Testar Endpoints**

#### Registro de Usuário
```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "email": "joao@example.com",
    "password": "123456"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@example.com",
    "password": "123456"
  }'
```

#### Listar Polls
```bash
curl -X GET http://localhost:8080/api/v1/polls \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Obter URL de Upload
```bash
curl -X GET "http://localhost:8080/api/v1/upload/presigned-url?type=face_evidence" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### Votar
```bash
curl -X POST http://localhost:8080/api/v1/polls/poll-1/vote \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "candidateId": "candidate-1",
    "faceImageUrl": "https://s3-url-from-upload"
  }'
```

---

## 📋 **Próximos Passos para Produção**

### **FASE 1: Repositories Reais (2-3 dias)**
1. Implementar `DynamoPollRepository`
2. Implementar `DynamoVoteRepository`
3. Completar `DynamoUserRepository`
4. Configurar mapeamento DynamoDB Enhanced

### **FASE 2: Segurança (1-2 dias)**
1. Implementar extração de userId do JWT
2. Middleware de autenticação
3. Validação de acesso a polls privadas
4. Rate limiting

### **FASE 3: AWS Rekognition (2-3 dias)**
1. Implementar comparação facial real
2. Armazenar face vectors no registro
3. Configurar threshold de confiança
4. Error handling para falhas de verificação

### **FASE 4: Testes e Deploy (2-3 dias)**
1. Testes unitários para services
2. Testes de integração
3. Deploy AWS (Lambda + API Gateway)
4. Configuração de produção

---

## 🏗️ **Arquitetura Atual**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Controllers   │    │    Services     │    │   Repositories  │
│                 │    │                 │    │                 │
│ AuthController  │───▶│ UserCommandSvc  │───▶│ UserRepository  │
│ PollController  │    │ VotingService   │    │ PollRepository  │
│ VoteController  │    │ FaceVerifSvc    │    │ VoteRepository  │
│ UploadController│    │ S3Service       │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│      DTOs       │    │   Domain Model  │    │   AWS Services  │
│                 │    │                 │    │                 │
│ LoginRequest    │    │ User, Poll      │    │ DynamoDB        │
│ VoteRequest     │    │ Vote, Candidate │    │ S3              │
│ PollResponse    │    │ Value Objects   │    │ Rekognition     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

---

## 🔧 **Configuração de Desenvolvimento**

### **Variáveis de Ambiente**
```yaml
# application-dev.yml
aws:
  region: us-east-1
  dynamodb:
    endpoint: http://localhost:8000
  s3:
    endpoint: http://localhost:4566
    bucket: rekognizevote-dev
  rekognition:
    endpoint: http://localhost:4566

jwt:
  secret: dev-secret-key
  expiration: 3600000
```

### **Portas Utilizadas**
- **8080**: API Spring Boot
- **8000**: DynamoDB Local
- **4566**: LocalStack (S3 + Rekognition)

---

## 📱 **Integração com Android**

O app Android já está **PRONTO** e aguardando apenas:

1. **Base URL**: Configurar para `http://localhost:8080/api/v1/`
2. **Endpoints**: Todos implementados conforme documentação
3. **Fluxo**: Registro → Login → Listar Polls → Votar → Resultados

### **Fluxo de Votação Completo**
1. App captura selfie com CameraX
2. App solicita pre-signed URL (`/upload/presigned-url`)
3. App faz upload direto para S3
4. App envia voto (`/polls/{id}/vote`) com URL da imagem
5. Backend verifica face e registra voto
6. App recebe confirmação

---

## 🎉 **MVP Funcional**

Com esta implementação, você tem um **MVP funcional** que:

- ✅ Autentica usuários com JWT
- ✅ Lista polls ativas
- ✅ Permite votação com upload de imagem
- ✅ Simula verificação facial
- ✅ Integra com o app Android existente

**Tempo estimado para produção completa: 8-12 dias**