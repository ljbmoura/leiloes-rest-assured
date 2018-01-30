package br.com.caelum.leilao.teste;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.xml.XmlPath;

import br.com.caelum.leilao.modelo.Usuario;

public class UsuarioTest {

	private static Usuario usuario1Esperado;
	private static Usuario usuario2Esperado;

	@BeforeClass
	public static void inicializacaoClasse() {

		RestAssured.basePath = "http://localhost:8080";
		
		usuario1Esperado = new Usuario(1L, "Mauricio Aniche", "mauricio.aniche@caelum.com.br");
		usuario2Esperado = new Usuario(2L, "Guilherme Silveira", "guilherme.silveira@caelum.com.br");
	}
	
	@Before
	public void inicializacaoTeste() {
	}

	@Test
	public void deveRetornarDoisUsuariosEspecificandoFormatoPorQueryString() {

		XmlPath path = RestAssured
				.given()
					.queryParam("_format", "xml")
				.get("usuarios")
				.andReturn().xmlPath();

		Usuario usuario1Obtido = path.getObject("list.usuario[0]", Usuario.class);
		assertThat(usuario1Obtido, equalTo(usuario1Esperado));

		Usuario usuario2Obtido = path.getObject("list.usuario[1]", Usuario.class);
		assertThat(usuario2Obtido, equalTo(usuario2Esperado));
	}
	
	public void deveRetornarDoisUsuariosEspecificandoFormatoPorHeader() {

		XmlPath path = RestAssured
			.given()
				.header("Accept", "application/xml")
			.get("usuarios")
			.andReturn().xmlPath();

		List<Usuario> usuarios = path.getList("list.usuario", Usuario.class);
		
		Usuario usuario1Obtido = usuarios.get(0);
		assertThat(usuario1Obtido, equalTo(usuario1Esperado));

		Usuario usuario2Obtido = usuarios.get(1);
		assertThat(usuario2Obtido, equalTo(usuario2Esperado));	
	} 

}