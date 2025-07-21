package dao;

import model.EscapeRoom;

import java.util.List;

public interface EscapeRoomDAO {
    void create(EscapeRoom room);
    List<EscapeRoom> getAll();
    void update(EscapeRoom room);
    void delete(int id);
}
