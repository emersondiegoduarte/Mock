package br.treinamento;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


public class EntidadeNegocioTest {

	private EntidadeNegocio entidadeNegocio;
	private EntidadeDAOInterface entidadeDAO;
	@Rule
	public  ExpectedException exception = ExpectedException.none();


	@Before
	public void setUp() throws Exception {
		entidadeDAO = EasyMock.createMock(EntidadeDAOInterface.class);
		entidadeNegocio = new EntidadeNegocio();
		entidadeNegocio.setPersistencia(entidadeDAO);
	}

	@After
	public void tearDown() throws Exception {
		EasyMock.reset(entidadeDAO);
	}
	
	@Test
	public void testValidarCamposObrigatorios() throws Exception {
		Entidade entidade;
		Entidade entidadeExpected;
		Entidade entidadeEntrada;
		
		// Cenário 1: Salvamento com sucesso.
		entidadeEntrada = getEntidadeValida();
		
		entidadeExpected = getEntidadeValida();
		entidadeExpected.setId(1L);
		
		EasyMock.reset(entidadeDAO);
		EasyMock.expect(entidadeDAO.verificarUnicidadeNome(entidadeEntrada)).andReturn(true);
		EasyMock.expect(entidadeDAO.salvar(entidadeEntrada)).andReturn(entidadeExpected);
		EasyMock.replay(entidadeDAO);
		
		entidade = entidadeNegocio.salvar(entidadeEntrada);
		
		assertNotNull("Cenário 1: Salvamento com sucesso.", entidade.getId());
		
		EasyMock.verify(entidadeDAO);
		
		// Cenário 2: Tenta salvar com campo nome não preenchido.
		entidadeEntrada = getEntidadeValida();
		entidadeEntrada.setNome(null);
		
		EasyMock.reset(entidadeDAO);
		EasyMock.replay(entidadeDAO);
		
		exception.expect(Exception.class);
		exception.expectMessage("O nome é obrigatório");
		entidade = entidadeNegocio.salvar(entidadeEntrada);
			
			
		EasyMock.verify(entidadeDAO);
		
		// Cenário 3: Tenta salvar com campo número do documento não preenchido.
		entidadeEntrada = getEntidadeValida();
		entidadeEntrada.setNumeroDocumento(null);
		
		EasyMock.reset(entidadeDAO);
		EasyMock.replay(entidadeDAO);
		exception.expect(Exception.class);
		exception.expectMessage("O número do documento deve ser maior que zero");
		entidade = entidadeNegocio.salvar(entidadeEntrada);
			
		EasyMock.verify(entidadeDAO);
		
		// Cenário 4: Tenta salvar com campo tipo do documento não preenchido.
		entidadeEntrada = getEntidadeValida();
		entidadeEntrada.setTipoDocumento(null);
		
		EasyMock.reset(entidadeDAO);
		EasyMock.replay(entidadeDAO);
		
		try {
			entidade = entidadeNegocio.salvar(entidadeEntrada);
			
			fail("Cenário 4: Tenta salvar com campo tipo do documento não preenchido. O campo tipo do documento não deve estar preenchido.");
		} catch (Exception e) {
			assertEquals("Cenário 4: Tenta salvar com campo tipo do documento não preenchido.", "O tipo do documento é obrigatório", e.getMessage());
		}
		
		EasyMock.verify(entidadeDAO);
		
		// Cenário 5: Tenta salvar com data inicial preenchida e data final não preenchida (peréodo incompleto).
		entidadeEntrada = getEntidadeValida();
		entidadeEntrada.setDataFinal(null);
		
		EasyMock.reset(entidadeDAO);
		EasyMock.replay(entidadeDAO);
		
		try {
			entidade = entidadeNegocio.salvar(entidadeEntrada);
			
			fail("Cenário 5: Tenta salvar com data inicial preenchida e data final não preenchida (período incompleto). O campo data final não deve estar preenchido.");
		} catch (Exception e) {
			assertEquals("Cenário 5: Tenta salvar com data inicial preenchida e data final não preenchida (período incompleto).", "O período deve ser informado por completo", e.getMessage());
		}
		
		EasyMock.verify(entidadeDAO);
	}
	
	@Test
	public void testValidarRegras() throws Exception {
		Entidade entidade;
		Entidade entidadeExpected;
		Entidade entidadeEntrada;
		Calendar calendario;
		
		// Cenário 1: Salvamento com sucesso.
		entidadeEntrada = getEntidadeValida();
		
		entidadeExpected = getEntidadeValida();
		entidadeExpected.setId((long) 1);
		
		EasyMock.reset(entidadeDAO);
		EasyMock.expect(entidadeDAO.verificarUnicidadeNome(entidadeEntrada)).andReturn(true);
		EasyMock.expect(entidadeDAO.salvar(entidadeEntrada)).andReturn(entidadeExpected);
		EasyMock.replay(entidadeDAO);
		
		entidade = entidadeNegocio.salvar(entidadeEntrada);
		
		assertNotNull("Cenário 1: Salvamento com sucesso.", entidade.getId());
		
		EasyMock.verify(entidadeDAO);
		
		// Cenário 2: Tenta salvar com campo nome com nome contendo mais do que 30 caracteres (erro).
		entidadeEntrada = getEntidadeValida();
		entidadeEntrada.setNome("Anderson Marinho da Silva e Silva");
		
		EasyMock.reset(entidadeDAO);
		EasyMock.replay(entidadeDAO);
		
		try {
			entidade = entidadeNegocio.salvar(entidadeEntrada);
			
			fail("Cenário 2: Tenta salvar com campo nome com nome contendo mais do que 30 caracteres. O campo nome não deve ter mais que 30 caracteres.");
		} catch (Exception e) {
			assertEquals("Cenário 2: Tenta salvar com campo nome com nome contendo mais do que 30 caracteres.", "O nome não pode ter mais que 30 caracteres", e.getMessage());
		}
		
		EasyMock.verify(entidadeDAO);
		
		// Cenário 3: Tenta salvar com campo nome com nome contendo menos do que 5 caracteres (erro).
		entidadeEntrada = getEntidadeValida();
		entidadeEntrada.setNome("Ana");
		
		EasyMock.reset(entidadeDAO);
		EasyMock.replay(entidadeDAO);
		
		try {
			entidade = entidadeNegocio.salvar(entidadeEntrada);
			
			fail("Cenário 3: Tenta salvar com campo nome com nome contendo menos do que 5 caracteres. O campo nome não deve ter menos que 5 caracteres.");
		} catch (Exception e) {
			assertEquals("Cenário 3: Tenta salvar com campo nome com nome contendo menos do que 5 caracteres.", "O nome não pode ter menos que 5 caracteres", e.getMessage());
		}
		
		EasyMock.verify(entidadeDAO);
		
		// Cenário 4: Tenta salvar com campo número do documento menor ou igual a zero (erro).
		entidadeEntrada = getEntidadeValida();
		entidadeEntrada.setNumeroDocumento((long)-1);
		
		EasyMock.reset(entidadeDAO);
		EasyMock.replay(entidadeDAO);
		
		try {
			entidade = entidadeNegocio.salvar(entidadeEntrada);
			
			fail("Cenário 4: Tenta salvar com campo número do documento menor ou igual a zero (erro). O campo número do documento deve ser menor ou igual a zero.");
		} catch (Exception e) {
			assertEquals("Cenário 4: Tenta salvar com campo número do documento menor ou igual a zero (erro).", "O número do documento deve ser maior que zero", e.getMessage());
		}
		
		EasyMock.verify(entidadeDAO);
		
		// Cenário 5: Tenta salvar com campo data inicial menor que a data atual (erro).
		entidadeEntrada = getEntidadeValida();
		calendario = Calendar.getInstance();
		calendario.add(Calendar.DAY_OF_MONTH, -1);
		entidadeEntrada.setDataInicial(calendario.getTime());
		
		EasyMock.reset(entidadeDAO);
		EasyMock.replay(entidadeDAO);
		
		try {
			entidade = entidadeNegocio.salvar(entidadeEntrada);
			
			fail("Cenário 5: Tenta salvar com campo data inicial menor que a data atual (erro). O campo data inicial deve ser menor que a data atual.");
		} catch (Exception e) {
			assertEquals("Cenário 5: Tenta salvar com campo data inicial menor que a data atual (erro).", "A data inicial não pode ser menor que a data atual", e.getMessage());
		}
		
		EasyMock.verify(entidadeDAO);
		
		// Cenário 6: Tenta salvar com campo data final menor que a data inicial (erro).
		entidadeEntrada = getEntidadeValida();
		calendario = Calendar.getInstance();
		calendario.add(Calendar.DAY_OF_MONTH, -1);
		entidadeEntrada.setDataFinal(calendario.getTime());
		
		EasyMock.reset(entidadeDAO);
		EasyMock.replay(entidadeDAO);
		
		try {
			entidade = entidadeNegocio.salvar(entidadeEntrada);
			
			fail("Cenário 6: Tenta salvar com campo data final menor que a data inicial (erro). O campo data final deve ser menor que a data inicial.");
		} catch (Exception e) {
			assertEquals("Cenário 6: Tenta salvar com campo data final menor que a data inicial (erro).", "A data final não pode ser menor que a data inicial", e.getMessage());
		}
		
		EasyMock.verify(entidadeDAO);
		
		// Cenário 7: Tenta salvar com campo tipo do documento inválido (erro).
		entidadeEntrada = getEntidadeValida();
		entidadeEntrada.setTipoDocumento(4);
		
		EasyMock.reset(entidadeDAO);
		EasyMock.replay(entidadeDAO);
		
		exception.expect(Exception.class);
		exception.expectMessage("Tipo de documento inválido");
		entidade = entidadeNegocio.salvar(entidadeEntrada);
		EasyMock.verify(entidadeDAO);
		
		// Cenário 8: Tenta salvar com campo e-mail inválido (erro).
		entidadeEntrada = getEntidadeValida();
		entidadeEntrada.setEmail("meuemailcom");
		
		EasyMock.reset(entidadeDAO);
		EasyMock.replay(entidadeDAO);
		exception.expect(Exception.class);
		exception.expectMessage("Endereço de email inválido");
		entidade = entidadeNegocio.salvar(entidadeEntrada);
		EasyMock.verify(entidadeDAO);
	}
	
	@Test
	public void testSalvar() throws Exception {
		Entidade entidade;
		Entidade entidadeExpected;
		Entidade entidadeEntrada;
		
		testValidarCamposObrigatorios();
		testValidarRegras();
		
		// Cenário 1: Tenta salvar, mas unidicidade retorna false.
		entidadeEntrada = getEntidadeValida();
		
		EasyMock.reset(entidadeDAO);
		EasyMock.expect(entidadeDAO.verificarUnicidadeNome(entidadeEntrada)).andReturn(false);
		EasyMock.replay(entidadeDAO);
		exception.expect(Exception.class);
		exception.expectMessage("Já existe entidade cadastrada com este nome.");
		entidade = entidadeNegocio.salvar(entidadeEntrada);
		EasyMock.verify(entidadeDAO);
		
		// Cenário 2: Tenta salvar, salvamento ocorre com sucesso.
		entidadeEntrada = getEntidadeValida();
		
		entidadeExpected = getEntidadeValida();
		entidadeExpected.setId((long) 1);
		
		EasyMock.reset(entidadeDAO);
		EasyMock.expect(entidadeDAO.verificarUnicidadeNome(entidadeEntrada)).andReturn(true);
		EasyMock.expect(entidadeDAO.salvar(entidadeEntrada)).andReturn(entidadeExpected);
		EasyMock.replay(entidadeDAO);
		
		entidade = entidadeNegocio.salvar(entidadeEntrada);
		
		assertNotNull("Cenário 1: Salvamento com sucesso.", entidade.getId());
		
		EasyMock.verify(entidadeDAO);
	}
	
	@Test
	public void testGetById() throws Exception {
		Entidade entidade;
		Entidade entidadeExpected;
		
		testValidarCamposObrigatorios();
		testValidarRegras();
		
		// Cenário 1: Usuério retornado com suceso.
		entidadeExpected = getEntidadeValida();
		entidadeExpected.setId(1L);
		
		EasyMock.reset(entidadeDAO);
		EasyMock.expect(entidadeDAO.getById(1L)).andReturn(entidadeExpected);
		EasyMock.replay(entidadeDAO);
		
		entidade = entidadeNegocio.getById(1L);
		
		assertEquals(entidadeExpected.getNome(), entidade.getNome());
		
		EasyMock.verify(entidadeDAO);
		
		// Cenário 2: Usuário não encontrado.
		entidadeExpected = null;
		
		EasyMock.reset(entidadeDAO);
		EasyMock.expect(entidadeDAO.getById(2L)).andReturn(entidadeExpected);
		EasyMock.replay(entidadeDAO);
		entidade = entidadeNegocio.getById(2L);
		EasyMock.verify(entidadeDAO);
	}
	
	@Test
	public void testAlterar() throws Exception {
		Entidade entidade;
		Entidade entidadeExpected;
		Entidade entidadeEntrada;
		Entidade entidadeExpectedGetById;
		
		testValidarCamposObrigatorios();
		testValidarRegras();
		testGetById();
		
		
		// Cenário 1: Usuério alterado com sucesso.
		entidadeEntrada = getEntidadeValida();
		entidadeEntrada.setEmail("outro@email.com");
		entidadeEntrada.setId(1L);
		entidadeEntrada.setDataFinal(new Date());
		entidadeEntrada.setDataInicial(new Date());
		entidadeEntrada.setNumeroDocumento(1234566L);
		entidadeEntrada.setTipoDocumento(2);
		
		entidadeExpectedGetById = getEntidadeValida();
		entidadeExpectedGetById.setId(1L);
		entidadeExpectedGetById.setDataFinal(new Date());
		entidadeExpectedGetById.setDataInicial(new Date());
		entidadeExpectedGetById.setNumeroDocumento(1234566L);
		entidadeExpectedGetById.setTipoDocumento(2);
		
		entidadeExpected = getEntidadeValida();
		entidadeExpected.setEmail("outro@email.com");
		entidadeExpected.setId(1L);
		entidadeExpected.setDataFinal(new Date());
		entidadeExpected.setDataInicial(new Date());
		entidadeExpected.setNumeroDocumento(1234566L);
		entidadeExpected.setTipoDocumento(2);
		
		EasyMock.reset(entidadeDAO);
		EasyMock.expect(entidadeDAO.getById(1L)).andReturn(entidadeExpectedGetById);
		EasyMock.expect(entidadeDAO.alterar(entidadeEntrada)).andReturn(entidadeExpected);
		EasyMock.replay(entidadeDAO);
		entidade = entidadeNegocio.alterar(entidadeEntrada);
		assertNotNull(entidade.getId());
		EasyMock.verify(entidadeDAO);
		
		// Cenário 2: Usuério com o nome alterado (erro).
		entidadeEntrada = getEntidadeValida();
		entidadeEntrada.setEmail("email@email.com");
		entidadeEntrada.setNome("Teste");
		entidadeEntrada.setId(1L);
		
		entidadeExpectedGetById = getEntidadeValida();
		entidadeExpectedGetById.setEmail("email@email.com");
		entidadeExpectedGetById.setNome("Teste");
		entidadeExpectedGetById.setId(1L);
		
		EasyMock.reset(entidadeDAO);
		EasyMock.expect(entidadeDAO.getById(1L)).andReturn(entidadeExpectedGetById);
		EasyMock.replay(entidadeDAO);
		exception.expect(Exception.class);
		exception.expectMessage("Não é possível alterar o nome da entidade");
		entidadeEntrada.setNome("Diego");
		testValidarCamposObrigatorios();
		entidade = entidadeNegocio.alterar(entidadeEntrada);
		EasyMock.verify(entidadeDAO);
	}
	
	@Test
	public void testExcluir() throws Exception {
		Entidade entidadeEntrada;
		
		// Cenário 1: Exclui com sucesso.
		entidadeEntrada = getEntidadeValida();
		entidadeEntrada.setTipoDocumento(2);
		
		EasyMock.reset(entidadeDAO);
		entidadeDAO.excluir(entidadeEntrada);
		EasyMock.expectLastCall().once();
		EasyMock.replay(entidadeDAO);
		
		entidadeNegocio.excluir(entidadeEntrada);
		
		EasyMock.verify(entidadeDAO);
		
		// Cenário 2: Tenta excluir entidade com tipo de documento CPF (erro).
		entidadeEntrada = getEntidadeValida();
		
		EasyMock.reset(entidadeDAO);
		EasyMock.replay(entidadeDAO);
		
		exception.expect(Exception.class);
		exception.expectMessage("Não é possível excluir entidades com CPF");
		entidadeNegocio.excluir(entidadeEntrada);
			
		
		EasyMock.verify(entidadeDAO);
	}
	
	@Test
	public void testGetQuantidadeRegistros() throws Exception {
		int quantidadeRegistrosActual;
		int quantidadeRegistrosExpected = 10;
		
		// Cenário énico: Retorna a quantidade de registos.
		EasyMock.reset(entidadeDAO);
		EasyMock.expect(entidadeDAO.getQuantidadeRegistros()).andReturn(quantidadeRegistrosExpected);
		EasyMock.replay(entidadeDAO);
		quantidadeRegistrosActual = entidadeNegocio.getQuantidadeRegistros();
		assertEquals(quantidadeRegistrosExpected, quantidadeRegistrosActual);
		EasyMock.verify(entidadeDAO);
	}
	
	@Test
	public void testVerificarUnicidadeNome() throws Exception {
		Entidade entidadeEntrada;
		boolean resposta;
		
		// Cenário 1: Entidade é énica.
		entidadeEntrada = getEntidadeValida();
		
		EasyMock.reset(entidadeDAO);
		EasyMock.expect(entidadeDAO.verificarUnicidadeNome(entidadeEntrada)).andReturn(true);
		EasyMock.replay(entidadeDAO);
		resposta = entidadeNegocio.verificarUnicidadeNome(entidadeEntrada);
		assertTrue(resposta);
		
		// Cenário 2: Entidade não é énica.
		entidadeEntrada = getEntidadeValida();
		EasyMock.reset(entidadeDAO);
		EasyMock.expect(entidadeDAO.verificarUnicidadeNome(entidadeEntrada)).andReturn(false);
		EasyMock.replay(entidadeDAO);
		resposta = entidadeNegocio.verificarUnicidadeNome(entidadeEntrada);
		
		assertFalse(resposta);
	}
	
	/**
	 * Gera um objeto de Entidade válido e corretamente preenchido.
	 * 
	 * @return Entidade
	 */
	private Entidade getEntidadeValida() {
		
		Entidade entidade = new Entidade();
		entidade.setNome("Emerson Diego");
		entidade.setDataInicial(new Date("10/02/2014"));
		entidade.setDataFinal(new Date("25/02/2014"));
		entidade.setTipoDocumento(1);
		entidade.setNumeroDocumento(new Long(1234567));
		
		return entidade;
	}

}
