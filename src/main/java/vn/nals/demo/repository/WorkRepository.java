package vn.nals.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.nals.demo.entity.Work;

@Repository("workRepository")
public interface WorkRepository extends JpaRepository<Work, Integer> {

}
