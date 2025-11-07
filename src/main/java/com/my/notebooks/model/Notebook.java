package com.my.notebooks.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "notebooks")
public class Notebook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "manufacturer_name", nullable = false)
    private String manufacturerName;

    @Column(name = "notebook_name_code", nullable = false, unique = true)
    private String notebookNameCode;

    @Column(name = "page_count")
    private int pageCount;

    @Column(name = "cover_type")
    private String coverType;

    @Column(name = "country")
    private String country;

    @Column(name = "circulation")
    private int circulation;

    @Column(name = "page_layout")
    private String pageLayout;

    public Notebook() {
    }

    public Notebook(String manufacturerName, String notebookNameCode, int pageCount, String coverType, String country, int circulation, String pageLayout) {
        this.manufacturerName = manufacturerName;
        this.notebookNameCode = notebookNameCode;
        this.pageCount = pageCount;
        this.coverType = coverType;
        this.country = country;
        this.circulation = circulation;
        this.pageLayout = pageLayout;
    }

    public Notebook(Long id, String manufacturerName, String notebookNameCode, int pageCount, String coverType, String country, int circulation, String pageLayout) {
        this.id = id;
        this.manufacturerName = manufacturerName;
        this.notebookNameCode = notebookNameCode;
        this.pageCount = pageCount;
        this.coverType = coverType;
        this.country = country;
        this.circulation = circulation;
        this.pageLayout = pageLayout;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getNotebookNameCode() {
        return notebookNameCode;
    }

    public void setNotebookNameCode(String notebookNameCode) {
        this.notebookNameCode = notebookNameCode;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getCoverType() {
        return coverType;
    }

    public void setCoverType(String coverType) {
        this.coverType = coverType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCirculation() {
        return circulation;
    }

    public void setCirculation(int circulation) {
        this.circulation = circulation;
    }

    public String getPageLayout() {
        return pageLayout;
    }

    public void setPageLayout(String pageLayout) {
        this.pageLayout = pageLayout;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notebook notebook = (Notebook) o;
        return Objects.equals(id, notebook.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}