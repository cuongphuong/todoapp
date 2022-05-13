package vn.nals.demo.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.nals.demo.common.DateUtil;
import vn.nals.demo.entity.Work;
import vn.nals.demo.entity.Work.WorkStatus;
import vn.nals.demo.entity.WorkInput;
import vn.nals.demo.service.WorkService;

@RestController
@RequestMapping("/api")
public class WorkController {

    @Autowired
    private WorkService workService;

    @GetMapping("/works/{pageStr}")
    public ResponseEntity<Object> getListWork(@PathVariable String pageStr) {
        Integer pageIndex = NumberUtils.toInt(pageStr);
        if (!NumberUtils.isCreatable(pageStr)) {
            return ResponseEntity.status(400).body("Page number is in correct");
        }
        return ResponseEntity.status(200).body(this.workService.findAll(pageIndex));
    }

    @PostMapping("/work")
    public ResponseEntity<Object> createWork(@RequestBody WorkInput workInput)
            throws ParseException {
        List<String> errors = this.validate(workInput);
        Map<String, Object> result = new HashMap<>();
        result.put("status", true);

        if (!errors.isEmpty()) {
            result.put("errorMessage", errors);
            return ResponseEntity.status(400).body(errors);
        }

        Work work = new Work();
        work.setWorkName(workInput.getWorkName());
        work.setStartDate(DateUtil.strToDate(workInput.getStartDate()));
        work.setEndDate(DateUtil.strToDate(workInput.getEndDate()));
        work.setStatus(NumberUtils.toInt(workInput.getStatus()));
        work.setDeleteFlg(0);
        return ResponseEntity.status(200).body(this.workService.saveWork(work));
    }

    @PutMapping("/work/{workId}")
    public ResponseEntity<Object> updateWork(@RequestBody WorkInput workInput, @PathVariable Integer workId) throws ParseException {
        List<String> errors = this.validate(workInput);

        if (null == workId) {
            return ResponseEntity.status(400).body("Work ID is not valid");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("status", true);
        if (!errors.isEmpty()) {
            result.put("errorMessage", errors);
            return ResponseEntity.status(400).body(errors);
        }

        Work work = this.workService.findOne(workId);
        if (null == work) {
            return ResponseEntity.status(400).body("Work is not existed");
        }

        work.setWorkName(workInput.getWorkName());
        work.setStartDate(DateUtil.strToDate(workInput.getStartDate()));
        work.setEndDate(DateUtil.strToDate(workInput.getEndDate()));
        work.setStatus(NumberUtils.toInt(workInput.getStatus()));
        work.setDeleteFlg(0);
        return ResponseEntity.status(200).body(this.workService.saveWork(work));
    }

    @DeleteMapping("/work/{workId}")
    public ResponseEntity<Object> deleteWork(@PathVariable Integer workId)
            throws ParseException {
        if (null == workId) {
            return ResponseEntity.status(400).body("Work ID is not valid");
        }

        Work work = this.workService.findOne(workId);
        if (null == work) {
            return ResponseEntity.status(400).body("Work is not existed");
        }

        this.workService.deleteWork(work);
        return ResponseEntity.status(200).body(StringUtils.EMPTY);
    }

    private List<String> validate(WorkInput work) {
        List<String> errorList = new ArrayList<String>();

        if (work.getWorkName().getBytes().length > Work.WORK_NAME_MAX_LENGTH) {
            errorList.add(String.format("Input work name with %s byte length",
                    Work.WORK_NAME_MAX_LENGTH));
        }

        if (!DateUtil.isDefaultDate(work.getStartDate()) || !DateUtil.isDefaultDate(work.getEndDate())) {
            errorList.add(String.format("Require date format %s", DateUtil.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
        }

        if (!NumberUtils.isCreatable(work.getStatus())
                || !Arrays.asList(WorkStatus.values()).stream().anyMatch(p -> p .getStatus() == Integer.parseInt(work.getStatus()))) {
            errorList.add("Status in " + Arrays.asList(WorkStatus.values()) .stream().map(p -> p.getStatus()) .collect(Collectors.toList()));
        }
        return errorList;
    }

    @ExceptionHandler({ParseException.class})
    public ResponseEntity<String> handleParseException(Exception e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
