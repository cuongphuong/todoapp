package vn.nals.demo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import vn.nals.demo.entity.Work;

@Repository("workRepository")
public interface WorkRepository extends JpaRepository<Work, Integer> {

    @Query(value = "SELECT t FROM Work t WHERE t.deleteFlg = 0")
    Page<Work> findByPage(Pageable pageable);
}
