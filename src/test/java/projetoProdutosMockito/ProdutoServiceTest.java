package projetoProdutosMockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sistemaProdutosMockito.globalException.ProdutoNaoEncontradoException;
import sistemaProdutosMockito.produto.Produto;
import sistemaProdutosMockito.produto.ProdutoRepository;
import sistemaProdutosMockito.produto.ProdutoService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    // CT01 - Produto válido
    @Test
    void deveSalvarProdutoValido() {
        Produto produto = new Produto("Mouse", 49.90, 10);
        when(repository.salvar(produto)).thenReturn(produto);

        Produto resultado = service.salvar(produto);

        assertNotNull(resultado);
        assertEquals("Mouse", resultado.getNome());
        verify(repository).salvar(produto);
    }

    // CT02 - Preço negativo
    @Test
    void naoDeveSalvarProdutoComPrecoNegativo() {
        Produto produto = new Produto("Mouse", -10.0, 10);

        assertThrows(IllegalArgumentException.class,
                () -> service.salvar(produto));

        verify(repository, never()).salvar(any());
    }

    // CT03 - Preço igual a zero
    @Test
    void naoDeveSalvarProdutoComPrecoZero() {
        Produto produto = new Produto("Mouse", 0.0, 10);

        assertThrows(IllegalArgumentException.class,
                () -> service.salvar(produto));

        verify(repository, never()).salvar(any());
    }

    // CT04 - Nome vazio
    @Test
    void naoDeveSalvarProdutoComNomeVazio() {
        Produto produto = new Produto("", 49.90, 10);

        assertThrows(IllegalArgumentException.class,
                () -> service.salvar(produto));

        verify(repository, never()).salvar(any());
    }

    // CT05 - Nome nulo
    @Test
    void naoDeveSalvarProdutoComNomeNulo() {
        Produto produto = new Produto(null, 49.90, 10);

        assertThrows(IllegalArgumentException.class,
                () -> service.salvar(produto));

        verify(repository, never()).salvar(any());
    }

    // CT06 - Quantidade negativa
    @Test
    void naoDeveSalvarProdutoComQuantidadeNegativa() {
        Produto produto = new Produto("Mouse", 49.90, -1);

        assertThrows(IllegalArgumentException.class,
                () -> service.salvar(produto));

        verify(repository, never()).salvar(any());
    }

    // CT07 - Quantidade igual a zero (Aceitar)
    @Test
    void deveSalvarProdutoComQuantidadeZero() {
        Produto produto = new Produto("Mouse", 49.90, 0);
        when(repository.salvar(produto)).thenReturn(produto);

        Produto resultado = service.salvar(produto);

        assertEquals(0, resultado.getQuantidade());
        verify(repository).salvar(produto);
    }

    // CT08 - Buscar produto existente (ID 1)
    @Test
    void deveBuscarProdutoExistente() {
        Produto produto = new Produto("Mouse", 49.90, 10);
        when(repository.buscarPorId(1L)).thenReturn(produto);

        Produto resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals("Mouse", resultado.getNome());
        verify(repository).buscarPorId(1L);
    }

    // CT09 - Buscar produto inexistente (ID 99)
    @Test
    void deveLancarExcecaoAoBuscarProdutoInexistente() {
        when(repository.buscarPorId(99L)).thenReturn(null);

        assertThrows(ProdutoNaoEncontradoException.class,
                () -> service.buscarPorId(99L));

        verify(repository).buscarPorId(99L);
    }

    // CT10 - Falha de conexão ao salvar
    @Test
    void deveLancarExcecaoQuandoFalharConexaoAoSalvar() {
        Produto produto = new Produto("Mouse", 49.90, 10);
        doThrow(new RuntimeException("Falha na conexão"))
                .when(repository).salvar(any(Produto.class));

        assertThrows(RuntimeException.class,
                () -> service.salvar(produto));

        verify(repository).salvar(produto);
    }
}
