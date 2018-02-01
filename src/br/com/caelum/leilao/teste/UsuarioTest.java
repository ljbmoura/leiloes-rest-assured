package br.com.caelum.leilao.teste;

import static com.jayway.restassured.RestAssured.basePath;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Header;

import br.com.caelum.leilao.modelo.Leilao;
import br.com.caelum.leilao.modelo.Usuario;

public class UsuarioTest {

	private static Usuario mauricio;
	private static Usuario guilherme;

	@BeforeClass
	public static void inicializacaoClasse() {

		basePath = "http://localhost:8080";
		
		mauricio = new Usuario(1L, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");
		guilherme = new Usuario(2L, "Guilherme Silveira", "guilherme.silveira@caelum.com.br");
	}
	
	@Before
	public void inicializacaoTeste() {
	}

	@Test
	public void deveRetornarDoisUsuariosEspecificandoFormatoPorQueryString() {

		XmlPath path = 
				given()
					.queryParam("_format", "xml") // poderia ser usado "parameter" no lugar de "queryParam"	 
				.get("usuarios")
				.andReturn().xmlPath();

		Usuario usuario1Obtido = path.getObject("list.usuario[0]", Usuario.class);
		assertThat(usuario1Obtido, equalTo(mauricio));

		Usuario usuario2Obtido = path.getObject("list.usuario[1]", Usuario.class);
		assertThat(usuario2Obtido, equalTo(guilherme));
	}
	
	@Test
	public void deveRetornarDoisUsuariosEspecificandoFormatoPorHeader() {

		XmlPath path = 
			given()
				.header("Accept", "application/xml")
			.get("usuarios")
			.andReturn().xmlPath();

		List<Usuario> usuarios = path.getList("list.usuario", Usuario.class);
		
		Usuario usuario1Obtido = usuarios.get(0);
		assertThat(usuario1Obtido, equalTo(mauricio));

		Usuario usuario2Obtido = usuarios.get(1);
		assertThat(usuario2Obtido, equalTo(guilherme));	
	} 

	@Test
	public void deveRetornarUsuarioPeloId() {
		// http://localhost:8080/usuarios/show?usuario.id=1&_format=json
		JsonPath path = given()
			.header("Accept", "application/json")
//			.parameter("usuario.id", 1)
			.queryParam("usuario.id", 1)
		.get("usuarios/show")
		.andReturn().jsonPath();
		
		Usuario usuarioObtido = path.getObject("usuario", Usuario.class);
		
		assertThat(usuarioObtido, equalTo(mauricio));
	}
	
	@Test
	public void deveRetornarLeilaoPeloId() {
//		/leiloes/show?leilao.id=1&_format=json
		JsonPath path = given()
		.header(new Header("Accept", "application/json"))
		.parameter("leilao.id", 1)
		.get("leiloes/show")
		.andReturn()
		.jsonPath();
		
		Leilao leilaoObtido = path.getObject("leilao", Leilao.class);
		
		assertThat(leilaoObtido.getUsuario(), equalTo(mauricio));
		
		Leilao leilaoGeladeira = new Leilao((long) 1, "Geladeira", 800.00d, mauricio, false);
		assertThat(leilaoObtido, equalTo(leilaoGeladeira));
	}
	
	@Test
	public void deveRetornarTotalLeiloes() {
		XmlPath path = given()
		.header("Accept", "application/xml")
		.get("leiloes/total")
		.andReturn()
		.xmlPath();
		
		int qtdLeiloes = path.getInt("int");
		
		assertEquals(2, qtdLeiloes);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}