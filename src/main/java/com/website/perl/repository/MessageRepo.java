package com.website.perl.repository;

import com.website.perl.data.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo  extends CrudRepository<Message, Long> {

    List<Message> findByTag(String tag);
}
