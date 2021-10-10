package dev.melick.repositories;

import dev.melick.models.Attachment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import static dev.melick.utils.ConnectionUtil.getConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AttRepo {

    public Attachment create(Attachment a) {

        try(Connection connection = getConnectionUtil().getConnection()){

            String sql = "INSERT INTO attachments (att_by, file_name, file_ext, file) VALUES (?,?,?,?) returning *";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, a.getAttBy());
            ps.setString(2, a.getFileName());
            ps.setString(3, a.getFileExt());
            ps.setString(4, a.getFile());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    a.setAttId(rs.getInt("att_id"));

                    return a;

                }
            }

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Attachment getById(Integer id){

        try(Connection connection = getConnectionUtil().getConnection()){

            String sql = "SELECT * FROM attachments WHERE att_id = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Attachment a = new Attachment();

                    a.setAttId(rs.getInt("att_id"));
                    a.setFile(rs.getString("file"));

                    return a;
                }
            }

        }catch(SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
