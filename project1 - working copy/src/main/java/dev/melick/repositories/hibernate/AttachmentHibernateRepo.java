package dev.melick.repositories.hibernate;

import dev.melick.models.Attachment;
import dev.melick.utils.hibernate.connection.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class AttachmentHibernateRepo implements CrudRepository{

    //HibernateUtil hu = new HibernateUtil();

    @Override
    public Object add(Object o) {
        return null;
    }

    @Override
    public Object getById(Integer id) {
        // need to get a Session
        Session session = HibernateUtil.getSession();
        Attachment a = session.get(Attachment.class, id);
        session.close();
        return a;

    }

    @Override
    public List getAll() {

        Session session = HibernateUtil.getSession();
        return null;

    }

    @Override
    public void update(Object o) {

    }

    @Override
    public void delete(Integer id) {

    }
}
