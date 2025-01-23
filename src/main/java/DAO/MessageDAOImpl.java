package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAOImpl implements MessageDAO {
    public Message createMessage(Message message){
        try{
            Connection connection = ConnectionUtil.getConnection();
            String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?,?,?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3,message.getTime_posted_epoch());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                message.setMessage_id(rs.getInt(1));
            }

            return message;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Message getMessageById(int id){
        try{
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE message_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),rs.getLong("time_posted_epoch"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        try{
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message";
            Statement stmt = connection. createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    public boolean updateMessage(Message message){
        try{
            Connection connection = ConnectionUtil.getConnection();
            String sql = "UPDATE message SET message_text = ?, time_posted_epoch = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, message.getMessage_text());
            ps.setLong(2,message.getTime_posted_epoch());
            ps.setInt(3, message.getMessage_id());
            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMessage(int id){
        try{
            Connection connection = ConnectionUtil.getConnection();
            String sql = "DELETE FROM message WHERE message_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public List<Message> getMessagesByUser(int userId) {
        List<Message> messages = new ArrayList<>();
        try{
            Connection connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,userId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                messages.add(new Message(rs.getInt("message_id"),rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")));
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

}