package repository.intf;

import entity.Thue;
import java.util.List;

public interface ThueRepository {
    List<Thue> layListThue();
    boolean themThue(Thue t);
    boolean capNhatThue(Thue t);
    boolean xoaThue(String maThue);
    List<Thue> timKiem(String tuKhoa);
}