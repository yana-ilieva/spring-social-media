package com.example.social_media.repository;

import com.example.social_media.model.Post;
import com.example.social_media.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Set<Optional<Post>> findByOwner(User owner);
}
