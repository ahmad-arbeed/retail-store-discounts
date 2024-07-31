package com.retail.store.common.service;

import com.retail.store.common.model.DbSequence;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {

    private final MongoOperations mongoOperations;

    @Override
    public <T extends DbSequence> long generateSequence(String sequenceName, Class<T> dbSequence) {

        final var query = Query.query(Criteria.where("_id").is(sequenceName));
        final var update = new Update().inc("sequence", 1);
        final var options = FindAndModifyOptions.options()
            .returnNew(true)
            .upsert(true);

        final var sequence = mongoOperations.findAndModify(query, update, options, dbSequence);

        return Optional.ofNullable(sequence)
            .map(DbSequence::getSequence)
            .orElse(1L);
    }
}