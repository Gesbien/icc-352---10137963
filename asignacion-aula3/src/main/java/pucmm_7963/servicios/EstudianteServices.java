package pucmm_7963.servicios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import pucmm_7963.encapsulaciones.Estudiante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ejemplo de servicio patron Singleton
 */
public class EstudianteServices extends GestionDb{

    private static EstudianteServices instancia;

    /**
     * Constructor privado.
     */
    private EstudianteServices(){
        super(Estudiante.class);

    }

    public static EstudianteServices getInstancia(){
        if(instancia==null){
            instancia = new EstudianteServices();
        }
        return instancia;
    }

    public List<Estudiante> consultaNativa(){
        EntityManager em = getEntityManager();
        Query query = em.createNativeQuery("select * from estudiante ", Estudiante.class);
        //query.setParameter("nombre", apellido+"%");
        List<Estudiante> lista = query.getResultList();
        return lista;
    }

    public Estudiante getEstudiantePorMatricula(int matricula){
        EntityManager em = getEntityManager();
        Query query = em.createQuery("select e from Estudiante e where e.matricula like: matricula");
        query.setParameter("matricula", matricula);
        List<Estudiante> lista = query.getResultList();
        Estudiante est = lista.get(0);
        return est;
    }

}
