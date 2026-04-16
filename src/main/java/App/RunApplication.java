package App;

import Utils.JPAUtils;
import jakarta.persistence.EntityManager;

public class RunApplication {
    public static void main(String[] args) {
        EntityManager em = JPAUtils.getEntityManager();
    }
}
