package com.rekognizevote.infrastructure.adapter;

import com.rekognizevote.domain.model.User;
import com.rekognizevote.domain.port.UserRepository;
import com.rekognizevote.domain.valueobject.Email;
import com.rekognizevote.domain.valueobject.UserId;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

@Repository
public class DynamoUserRepository implements UserRepository {
    private final DynamoDbAsyncTable<User> table;

    public DynamoUserRepository(DynamoDbEnhancedAsyncClient dynamoClient) {
        this.table = dynamoClient.table("Users", TableSchema.fromBean(User.class));
    }

    @Override
    public Mono<User> save(User user) {
        return Mono.fromFuture(table.putItem(user))
            .thenReturn(user);
    }

    @Override
    public Mono<User> findById(UserId id) {
        Key key = Key.builder().partitionValue(id.value()).build();
        return Mono.fromFuture(table.getItem(key));
    }

    @Override
    public Mono<User> findByEmail(Email email) {
        return Mono.fromFuture(
            table.index("email-index")
                .query(QueryConditional.keyEqualTo(
                    Key.builder().partitionValue(email.value()).build()))
        ).flatMap(result -> 
            Mono.justOrEmpty(result.items().stream().findFirst())
        );
    }

    @Override
    public Mono<Boolean> existsByEmail(Email email) {
        return findByEmail(email)
            .map(user -> true)
            .defaultIfEmpty(false);
    }

    @Override
    public Mono<Void> delete(UserId id) {
        Key key = Key.builder().partitionValue(id.value()).build();
        return Mono.fromFuture(table.deleteItem(key))
            .then();
    }
}