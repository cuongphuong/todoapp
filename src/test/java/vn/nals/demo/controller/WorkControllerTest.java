package vn.nals.demo.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import vn.nals.demo.common.DateUtil;
import vn.nals.demo.entity.Work;
import vn.nals.demo.entity.WorkInput;
import vn.nals.demo.repository.WorkRepository;

@SpringBootTest
public class WorkControllerTest {

    @Autowired
    private WorkController workController;

    @Autowired
    private WorkRepository workRepository;

    @BeforeEach
    public void init() {
        this.createDataTest();
    }

    @AfterEach
    public void teardown() {
        this.workRepository.deleteAll();
    }

    @Test
    public void testGetListWork_pageIndexIsInCorrect() {
        ResponseEntity<Object> response = this.workController .getListWork("test");
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody(), "Page number is in correct");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetListWork_findByPagging() {
        // Check data page 1
        ResponseEntity<Object> response = this.workController.getListWork("0");
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        List<Work> list = (List<Work>) response.getBody();
        Assertions.assertEquals(list.size(), 5);

        Work firstItem = list.get(0);
        Assertions.assertEquals(firstItem.getStatus(), 1);
        Assertions.assertEquals(firstItem.getWorkName(), "work 0");
        Assertions.assertEquals(firstItem.getDeleteFlg(), 0);
        Assertions.assertEquals(
                DateUtil.toYYYYMMDDHH24MISS(firstItem.getStartDate()), "20220514000000");
        Assertions.assertEquals(
                DateUtil.toYYYYMMDDHH24MISS(firstItem.getEndDate()), "20220515000000");

        Work lastItem = list.get(4);
        Assertions.assertEquals(lastItem.getStatus(), 1);
        Assertions.assertEquals(lastItem.getWorkName(), "work 4");
        Assertions.assertEquals(lastItem.getDeleteFlg(), 0);
        Assertions.assertEquals(
                DateUtil.toYYYYMMDDHH24MISS(lastItem.getStartDate()), "20220518000000");
        Assertions.assertEquals(
                DateUtil.toYYYYMMDDHH24MISS(lastItem.getEndDate()), "20220519000000");

        // Check data page 2
        response = this.workController.getListWork("1");
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        list = (List<Work>) response.getBody();
        Assertions.assertEquals(list.size(), 5);

        firstItem = list.get(0);
        Assertions.assertEquals(firstItem.getStatus(), 1);
        Assertions.assertEquals(firstItem.getWorkName(), "work 5");
        Assertions.assertEquals(firstItem.getDeleteFlg(), 0);
        Assertions.assertEquals(
                DateUtil.toYYYYMMDDHH24MISS(firstItem.getStartDate()), "20220519000000");
        Assertions.assertEquals(
                DateUtil.toYYYYMMDDHH24MISS(firstItem.getEndDate()), "20220520000000");

        lastItem = list.get(4);
        Assertions.assertEquals(lastItem.getStatus(), 1);
        Assertions.assertEquals(lastItem.getWorkName(), "work 9");
        Assertions.assertEquals(lastItem.getDeleteFlg(), 0);
        Assertions.assertEquals(
                DateUtil.toYYYYMMDDHH24MISS(lastItem.getStartDate()), "20220523000000");
        Assertions.assertEquals(
                DateUtil.toYYYYMMDDHH24MISS(lastItem.getEndDate()), "20220524000000");
    }

    @Test
    public void testCreateWork() {
        String workName = String.format("%51s", "a");
        WorkInput workInput = new WorkInput();
        workInput.setWorkName(workName);
        workInput.setStatus("a");
        workInput.setStartDate("yyyy/mm/dd");
        workInput.setEndDate("yyyy/mm/dd");
    }

    @Test
    public void testCreateWork_workNameThan50Character() throws Exception {
        String workName = String.format("%51s", "a");
        WorkInput workInput = new WorkInput();
        workInput.setWorkName(workName);
        workInput.setStatus("1");
        workInput.setStartDate("2022/05/13 00:00:00");
        workInput.setEndDate("2022/05/14 00:00:00");
        ResponseEntity<Object> response = this.workController.createWork(workInput);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody().toString(), "[Input work name with 50 byte length]");
    }

    @Test
    public void testCreateWork_workNameThan50Charater_fullSizeCharacter() throws Exception {
        String workName = String.format("%48s", "a") + "ａ";
        WorkInput workInput = new WorkInput();
        workInput.setWorkName(workName);
        workInput.setStatus("1");
        workInput.setStartDate("2022/05/13 00:00:00");
        workInput.setEndDate("2022/05/14 00:00:00");
        ResponseEntity<Object> response = this.workController.createWork(workInput);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody().toString(), "[Input work name with 50 byte length]");
    }
    
    public void testCreateWork_statusIsNotNumber() throws Exception {
        WorkInput workInput = new WorkInput();
        workInput.setWorkName("test");
        workInput.setStatus("a");
        ResponseEntity<Object> response = this.workController.createWork(workInput);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody().toString(), "[Status in [0, 1, 2]]");
    }
    
    @Test
    public void testCreateWork_statusIsNotExisted() throws Exception {
        WorkInput workInput = new WorkInput();
        workInput.setWorkName("test");
        workInput.setStatus("3");
        workInput.setStartDate("2022/05/13 00:00:00");
        workInput.setEndDate("2022/05/14 00:00:00");
        ResponseEntity<Object> response = this.workController.createWork(workInput);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody().toString(), "[Status in [0, 1, 2]]");
    }
    
    @Test
    public void testCreateWork_startDateIsInCorrectFormat() throws Exception {
        WorkInput workInput = new WorkInput();
        workInput.setWorkName("test");
        workInput.setStatus("1");
        workInput.setStartDate("yyyy/mm/dd");
        ResponseEntity<Object> response = this.workController.createWork(workInput);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody().toString(), "[Require date format yyyy/MM/dd HH:mm:ss]");
    }

    @Test
    public void testCreateWork_endDateIsInCorrectFormat() throws Exception {
        WorkInput workInput = new WorkInput();
        workInput.setWorkName("test");
        workInput.setStatus("1");
        workInput.setStartDate("yyyy/mm/dd");
        ResponseEntity<Object> response = this.workController.createWork(workInput);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody().toString(), "[Require date format yyyy/MM/dd HH:mm:ss]");
    }

    @Test
    public void testCreateWork_dataIsValid() throws Exception {
        WorkInput workInput = new WorkInput();
        workInput.setWorkName("test_success");
        workInput.setStatus("1");
        workInput.setStartDate("2022/05/13 00:00:00");
        workInput.setEndDate("2022/05/14 00:00:00");
        ResponseEntity<Object> response = this.workController.createWork(workInput);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Work workCreated = (Work) response.getBody();
        Assertions.assertEquals(workCreated.getWorkName(), "test_success");
        Assertions.assertEquals(workCreated.getStatus(), 1);
        Assertions.assertEquals(workCreated.getDeleteFlg(), 0);
        Assertions.assertEquals(
                DateUtil.toYYYYMMDDHH24MISS(workCreated.getStartDate()), "20220513000000");
        Assertions.assertEquals(
                DateUtil.toYYYYMMDDHH24MISS(workCreated.getEndDate()), "20220514000000");
    }

    @Test
    public void testUpdateWork_workNameThan50Character() throws Exception {
        String workName = String.format("%51s", "a");
        WorkInput workInput = new WorkInput();
        workInput.setWorkName(workName);
        workInput.setStatus("1");
        workInput.setStartDate("2022/05/13 00:00:00");
        workInput.setEndDate("2022/05/14 00:00:00");
        ResponseEntity<Object> response = this.workController.updateWork(workInput, 10);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody().toString(), "[Input work name with 50 byte length]");
    }
    // .v.v
    /* Test update with validator same as create above */
 
    @Test
    public void testUpdateWork_workIdIsNull() throws ParseException {
        // Start update
        WorkInput workInput = new WorkInput();
        workInput.setWorkName("test_update_success");
        workInput.setStatus("1");
        workInput.setStartDate("2022/05/13 00:00:00");
        workInput.setEndDate("2022/05/14 00:00:00");

        ResponseEntity<Object> response = this.workController .updateWork(workInput, null);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody(), "Work ID is not valid");
    }

    @Test
    public void testUpdateWork_workIdIsNotExisted() throws ParseException {
        // Start update
        WorkInput workInput = new WorkInput();
        workInput.setWorkName("test_update_success");
        workInput.setStatus("1");
        workInput.setStartDate("2022/05/13 00:00:00");
        workInput.setEndDate("2022/05/14 00:00:00");

        ResponseEntity<Object> response = this.workController .updateWork(workInput, 0); // Not existed
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(response.getBody(), "Work is not existed");
    }

    @Test
    public void testUpdateWork_dataIsValid() throws Exception {
        // Prepare a work
        Date startDate = DateUtil.getDate(2022, 5, 10);
        Date endDate = DateUtil.getDate(2022, 5, 11);

        Work work = new Work();
        work.setWorkName("work_was_update");
        work.setStatus(1);
        work.setDeleteFlg(0);
        work.setStartDate(startDate);
        work.setEndDate(endDate);
        this.workRepository.saveAndFlush(work);

        // Start update
        WorkInput workInput = new WorkInput();
        workInput.setWorkName("test_update_success");
        workInput.setStatus("1");
        workInput.setStartDate("2022/05/13 00:00:00");
        workInput.setEndDate("2022/05/14 00:00:00");
        ResponseEntity<Object> response = this.workController.updateWork(workInput, work.getWorkId());

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Work workCreated = (Work) response.getBody();
        Assertions.assertEquals(workCreated.getWorkName(), "test_update_success");
        Assertions.assertEquals(workCreated.getStatus(), 1);
        Assertions.assertEquals(workCreated.getDeleteFlg(), 0);
        Assertions.assertEquals(
                DateUtil.toYYYYMMDDHH24MISS(workCreated.getStartDate()), "20220513000000");
        Assertions.assertEquals(
                DateUtil.toYYYYMMDDHH24MISS(workCreated.getEndDate()), "20220514000000");
    }

    @Test
    public void testDeleteWork_workIdIsNull() throws Exception {
        // Delete
        ResponseEntity<Object> response = this.workController.deleteWork(null);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDeleteWork_workIdIsNotExisted() throws Exception {
        // Delete
        ResponseEntity<Object> response = this.workController.deleteWork(0);
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDeleteWork_deleteSuccess() throws Exception {
        // Prepare a work
        Date startDate = DateUtil.getDate(2022, 5, 10);
        Date endDate = DateUtil.getDate(2022, 5, 11);

        Work work = new Work();
        work.setWorkName("work_is_deleted");
        work.setStatus(1);
        work.setDeleteFlg(0);
        work.setStartDate(startDate);
        work.setEndDate(endDate);
        this.workRepository.saveAndFlush(work);

        // Delete
        ResponseEntity<Object> response = this.workController.deleteWork(work.getWorkId());
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    private void createDataTest() {
        Date startDate = DateUtil.getDate(2022, 5, 14);
        Date endDate = DateUtil.getDate(2022, 5, 15);

        for (int i = 0; i < 10; i++) {
            Work work = new Work();
            work.setWorkName("work " + i);
            work.setStatus(1);
            work.setDeleteFlg(0);
            work.setStartDate(startDate);
            work.setEndDate(endDate);
            this.workRepository.saveAndFlush(work);
            startDate = DateUtil.getMoveDay(1, startDate);
            endDate = DateUtil.getMoveDay(1, endDate);
        }
    }
}
