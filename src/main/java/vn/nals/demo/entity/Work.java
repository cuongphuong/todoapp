package vn.nals.demo.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "tb_work")
@NamedQuery(name = "Work.findAll", query = "SELECT t FROM Work t")
public class Work implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int WORK_NAME_MAX_LENGTH = 50;

    public enum WorkStatus {
        PLANNING(0), DOING(1), COMPLETE(2);
        int status;

        public int getStatus() {
            return this.status;
        }

        WorkStatus(int status) {
            this.status = status;
        }
    }

    @Id
    @Column(name = "work_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_date")
    private Date endDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_date")
    private Date startDate;

    private int status;

    @Column(name = "work_name")
    private String workName;

    @Column(name = "delete_flg")
    private int deleteFlg;

    public Work() {
    }

    public Work(int workId, Date endDate, Date startDate, int status,
            String workName, int deleteFlg) {
        super();
        this.workId = workId;
        this.endDate = endDate;
        this.startDate = startDate;
        this.status = status;
        this.workName = workName;
        this.deleteFlg = deleteFlg;
    }

    public int getWorkId() {
        return this.workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWorkName() {
        return this.workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public int getDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(int deleteFlg) {
        this.deleteFlg = deleteFlg;
    }
}