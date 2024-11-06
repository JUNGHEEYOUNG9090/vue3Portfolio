package com.vue3Test.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vue3Test.demo.model.PostsModel;



@Repository
public interface PostsRepository extends JpaRepository<PostsModel, Long> {
    
	@Query(value = "SELECT d.id, d.title, d.update_dt, 'devLog' AS type FROM dev_log d " +
            "UNION ALL " +
            "SELECT image_id as id, i.image_src AS title, i.update_dt, 'image' AS type FROM imagelistinfo i " +
            "ORDER BY update_dt DESC",
    countQuery = "SELECT (SELECT COUNT(*) FROM dev_log) + (SELECT COUNT(*) FROM imagelistinfo)",
    nativeQuery = true)
	Page<Object[]> findAllPosts(Pageable pageable);
}
