package com.ivanthescientist.projectmanager.domain.model;

import com.ivanthescientist.projectmanager.domain.SecuredEntity;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "time_records")
public class TimeRecord implements SecuredEntity {
    @Id
    @Column
    @GeneratedValue
    protected long id;

    @ManyToOne(targetEntity = Task.class)
    protected Task task;

    @Column
    protected Date startTime;

    @Column
    protected Date endTime;

    public long getId() {
        return id;
    }

    public TimeRecord()
    {
        startTime = new Date();
    }

    public TimeRecord(Task task, Date startTime)
    {
        Assert.isTrue(startTime.getTime() <= new Date().getTime());

        this.startTime = startTime;
        this.task = task;
    }

    public void close(Date endTime)
    {
        Assert.isNull(this.endTime);
        Assert.isTrue(endTime.getTime() <= new Date().getTime());

        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }
}
