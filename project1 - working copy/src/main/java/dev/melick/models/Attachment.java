package dev.melick.models;

import java.util.Date;

/*
*  @Entity tells hibertnate that this class corresponds to a table in the DB
*  @Table allows us to provide extra information for our table, mainly the name of the table
* */

public class Attachment {

    private Integer attId;

    private Integer attBy;

    private String fileName;

    private String file;

    private String fileExt;

    private Date attDate;

    public Attachment(){}

    public Attachment(Integer attId, Integer attBy, String fileName, String file, String fileExt, Date attDate) {
        this.attId = attId;
        this.attBy = attBy;
        this.fileName = fileName;
        this.file = file;
        this.fileExt = fileExt;
        this.attDate = attDate;
    }

    public Integer getAttId() {
        return attId;
    }

    public void setAttId(Integer attId) {
        this.attId = attId;
    }

    public Integer getAttBy() {
        return attBy;
    }

    public void setAttBy(Integer attBy) {
        this.attBy = attBy;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileExt() {
        return fileExt;
    }

    public void setFileExt(String fileExt) {
        this.fileExt = fileExt;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Date getAttDate() {
        return attDate;
    }

    public void setAttDate(Date attDate) {
        this.attDate = attDate;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "attId=" + attId +
                ", attBy=" + attBy +
                ", fileName='" + fileName + '\'' +
                ", file='" + file + '\'' +
                ", fileExt='" + fileExt + '\'' +
                ", attDate=" + attDate +
                '}';
    }
}
