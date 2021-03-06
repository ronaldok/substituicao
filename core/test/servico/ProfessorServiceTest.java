/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servico;

import datamapper.PopulateDB;
import datamapper.ProfessorJpaController;
import datamapper.exceptions.NonexistentEntityException;
import dominio.Professor;
import modelo.ProfessorModel;
import servico.ProfessorService;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rick
 */
public class ProfessorServiceTest {
    
    private ProfessorService serviceEmTeste;
    
    @Before
    public void setUp() throws NonexistentEntityException, Exception {
        
        PopulateDB.fullSetupDB("prosub", "root", "");
        serviceEmTeste = new ProfessorService();
        
    }
    
    @After
    public void tearDown() {
    }


    @Test
    public void testeDeveListarTodosOsProfessores() {
        
        List<ProfessorModel> modelos = serviceEmTeste.ListarProfessores();
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pro_subPU");
        ProfessorJpaController controller = new ProfessorJpaController(emf);
        
        List<Professor> professores = controller.findProfessorEntities();
        
        Assert.assertEquals(professores.size(), modelos.size());
        Assert.assertEquals("Calebe", modelos.get(0).Nome);
        Assert.assertEquals("Ana Claudia", modelos.get(1).Nome);
    }
    
    @Test
    public void testeDeveAcharProfessorPorNome(){
        
        ProfessorModel modelo = serviceEmTeste.obterProfessorPorNome("ariovaldsssonnnn");
        
        Assert.assertNull(modelo);
        
        modelo = serviceEmTeste.obterProfessorPorNome("Calebe");
        
        Assert.assertNotNull(modelo);
        
    }
    
    @Test
    public void testeDeveListarProfessoresPossiveisDadoUmPeriodoDeAusenciaEAulasDeProfessorAusente(){
        
        //02/06/2013 até 04/06/2013
        
        DateTime inicio = new DateTime(2013, 06, 02, 0, 0);
        DateTime fim = new DateTime(2013, 06, 04, 0, 0);
        
        Interval periodo = new Interval(inicio, fim);
        
        List<ProfessorModel> professoresDisponiveis = serviceEmTeste.listarProfessoresCompativeisComAusenteNoPeriodo("Calebe", periodo);
        
        Assert.assertEquals(2, professoresDisponiveis.size());
        
        ProfessorModel prof1 = serviceEmTeste.obterProfessorPorNome("Ana Claudia");
        
        ProfessorModel prof2 = serviceEmTeste.obterProfessorPorNome("Gaston");
        
        Assert.assertTrue(professoresDisponiveis.contains(prof1));
        Assert.assertTrue(professoresDisponiveis.contains(prof2));
        
    }
    
}