#!/bin/bash

# Script para inicializar tabelas DynamoDB localmente
# Execute após subir o LocalStack/DynamoDB Local

ENDPOINT="http://localhost:8000"
REGION="us-east-1"

echo "🚀 Inicializando tabelas DynamoDB..."

# Criar tabela Users
aws dynamodb create-table \
    --endpoint-url $ENDPOINT \
    --region $REGION \
    --table-name Users \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
        AttributeName=email,AttributeType=S \
    --key-schema \
        AttributeName=id,KeyType=HASH \
    --global-secondary-indexes \
        IndexName=email-index,KeySchema=[{AttributeName=email,KeyType=HASH}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=5,WriteCapacityUnits=5} \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5

echo "✅ Tabela Users criada"

# Criar tabela Polls
aws dynamodb create-table \
    --endpoint-url $ENDPOINT \
    --region $REGION \
    --table-name Polls \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
        AttributeName=status,AttributeType=S \
    --key-schema \
        AttributeName=id,KeyType=HASH \
    --global-secondary-indexes \
        IndexName=status-index,KeySchema=[{AttributeName=status,KeyType=HASH}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=5,WriteCapacityUnits=5} \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5

echo "✅ Tabela Polls criada"

# Criar tabela Votes
aws dynamodb create-table \
    --endpoint-url $ENDPOINT \
    --region $REGION \
    --table-name Votes \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
        AttributeName=pollId,AttributeType=S \
        AttributeName=userId,AttributeType=S \
    --key-schema \
        AttributeName=id,KeyType=HASH \
    --global-secondary-indexes \
        IndexName=pollId-userId-index,KeySchema=[{AttributeName=pollId,KeyType=HASH},{AttributeName=userId,KeyType=RANGE}],Projection={ProjectionType=ALL},ProvisionedThroughput={ReadCapacityUnits=5,WriteCapacityUnits=5} \
    --provisioned-throughput \
        ReadCapacityUnits=5,WriteCapacityUnits=5

echo "✅ Tabela Votes criada"

# Inserir dados de exemplo
echo "📝 Inserindo dados de exemplo..."

# Poll de exemplo
aws dynamodb put-item \
    --endpoint-url $ENDPOINT \
    --region $REGION \
    --table-name Polls \
    --item '{
        "id": {"S": "poll-1"},
        "title": {"S": "Eleição Presidencial 2024"},
        "description": {"S": "Escolha seu candidato preferido"},
        "status": {"S": "ACTIVE"},
        "isPrivate": {"BOOL": false},
        "startDate": {"S": "2024-01-01T00:00:00Z"},
        "endDate": {"S": "2024-12-31T23:59:59Z"},
        "candidates": {"L": [
            {"M": {
                "id": {"S": "candidate-1"},
                "name": {"S": "João Silva"},
                "description": {"S": "Candidato A"},
                "imageUrl": {"S": "https://example.com/joao.jpg"},
                "voteCount": {"N": "0"}
            }},
            {"M": {
                "id": {"S": "candidate-2"},
                "name": {"S": "Maria Santos"},
                "description": {"S": "Candidata B"},
                "imageUrl": {"S": "https://example.com/maria.jpg"},
                "voteCount": {"N": "0"}
            }}
        ]},
        "createdBy": {"S": "admin"},
        "createdAt": {"S": "2024-01-01T00:00:00Z"}
    }'

echo "✅ Dados de exemplo inseridos"
echo "🎉 Inicialização completa!"