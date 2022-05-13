package vn.nals.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import vn.nals.demo.common.Constans;
import vn.nals.demo.entity.Work;
import vn.nals.demo.repository.WorkRepository;

@Service("workService")
public class WorkService {

    @Autowired
    private WorkRepository workRepository;

    public List<Work> findAll(int pageNumber) {
        Sort sortable = Sort.by("startDate").ascending();
        PageRequest pageable = PageRequest.of(pageNumber,
                Constans.DEFAULT_PAGE_SIZE, sortable);
        Page<Work> workList = this.workRepository.findAll(pageable);

        return workList.toList();
    }

    public Work saveWork(Work work) {
        return this.workRepository.saveAndFlush(work);
    }
    
    public void deleteWork(Work work) {
        this.workRepository.delete(work);
    }

    public Work findOne(Integer workId) {
        Optional<Work> work = this.workRepository.findById(workId);
        if (work.isPresent()) {
            return work.get();
        }
        return null;
    }
}
