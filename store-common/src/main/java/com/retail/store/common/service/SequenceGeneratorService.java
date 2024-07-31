package com.retail.store.common.service;

import com.retail.store.common.model.DbSequence;

public interface SequenceGeneratorService {

    <T extends DbSequence> long generateSequence(String sequenceName, Class<T> dbSequence);
}