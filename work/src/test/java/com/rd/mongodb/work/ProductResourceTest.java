package com.rd.mongodb.work;

import com.rd.mongodb.work.models.dto.ProductDTO;
import com.rd.mongodb.work.repositories.ProductRepository;
import com.rd.mongodb.work.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ProductResourceTest {
	private static final String BASE_URI = "/api/products/";

	@MockBean
	private ProductService productService;

	@MockBean
	private ProductRepository productRepository;

	@MockBean
	private ProductDTO product;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void test_Listar_Produtos_Com_Sucesso() throws Exception {
		 Pageable pageable  = PageRequest.of(0, 12, Sort.Direction.valueOf("ASC"), "id");
		 when(productService.findAllPaged(pageable)).thenReturn(Page.empty());

		 mockMvc.perform(get(BASE_URI)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.param("page", "0")
				.param("linesPerPage", "24")
				.param("direction", "ASC")
				.param("orderBy", "id"))
				.andExpect(status().isOk());
	}

	@Test
	public void test_Filtrar_Produto_Por_Parametros_Q_MinPrice_MaxPrice() throws Exception {
		when(productService.findSearch(any(),  anyString(),  anyDouble(), anyDouble() ))
				.thenReturn(Page.empty());

		mockMvc.perform(get(BASE_URI + "/search")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.param("page", "0")
				.param("linesPerPage", "24")
				.param("direction", "ASC")
				.param("orderBy", "id"))
				.andExpect(status().isOk());
	}

	@Test
	public void test_Buscar_Produto_Por_Id_Com_Sucesso() throws Exception {
		when(productService.findById(anyLong()))
				.thenReturn(any());

		 mockMvc.perform(get(BASE_URI)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.param("id", "1"))
				.andExpect(status().isOk());
	}

	@Test
	public void test_Cadastrar_Produto_Com_Sucesso() throws Exception {
		String product = "{\"name\": \"Produto teste foo bar\", \"description\": \"Descrição do Produto foo bar\", \"price\": 198.30}";

		when(productService.insert(any()))
				.thenReturn(any());

		mockMvc.perform(post(BASE_URI)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(product))
				.andExpect(status().isCreated());
	}

	@Test
	public void test_Editar_Produto_Com_Sucesso() throws Exception {
		String product = "{\"name\": \"Produto teste foo\", \"description\": \"Descrição do Produto foo\", \"price\": 298.30}";

		when(productService.update(anyLong(), any()))
				.thenReturn(any(), any());

		mockMvc.perform(put(BASE_URI + "{id}", "1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(product))
				.andExpect(status().isOk());
	}

	@Test
	public void test_Excluir_Produto_Com_Sucesso() throws Exception {
		 mockMvc.perform(delete(BASE_URI + "{id}", "23")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}