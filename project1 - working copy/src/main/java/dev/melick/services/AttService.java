package dev.melick.services;

import dev.melick.models.Attachment;
import dev.melick.repositories.AttRepo;

public class AttService {

    private static AttRepo ar = new AttRepo();

    public Integer createAttachment(Integer attBy, String fileName, String attText){

        Attachment att = new Attachment();

        att.setAttBy(attBy);
        att.setFileName(fileName);
        att.setFileExt(".txt");
        att.setFile(attText);

        Attachment a =ar.create(att);

        return a.getAttId();

    }
}
