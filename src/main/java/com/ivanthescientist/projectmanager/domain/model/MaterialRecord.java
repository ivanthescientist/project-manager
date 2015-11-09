package com.ivanthescientist.projectmanager.domain.model;

import javax.persistence.*;

@Entity
@Table(name = "material_records")
public class MaterialRecord {
    @Id
    @Column
    @GeneratedValue
    protected long id;

    @ManyToOne(targetEntity = Task.class)
    protected Task task;

    @Column
    protected String name;

    @Column
    protected String description;

    public MaterialRecord() {}

    public MaterialRecord(Task task, String name, String description) {
        this.task = task;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public Task getTask()
    {
        return task;
    }

    public void edit(String name, String description)
    {
        this.name = name;
        this.description = description;
    }
}
