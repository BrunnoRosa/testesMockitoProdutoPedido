package projetoProdutosMockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sistemaProdutosMockito.globalException.EstoqueInsuficienteException;
import sistemaProdutosMockito.globalException.ProdutoNaoEncontradoException;
import sistemaProdutosMockito.pedido.Pedido;
import sistemaProdutosMockito.pedido.PedidoRepository;
import sistemaProdutosMockito.pedido.PedidoService;
import sistemaProdutosMockito.produto.Produto;
import sistemaProdutosMockito.produto.ProdutoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private PedidoService service;

    // Cobre os cenários PED01 e PED02
    @Test
    void deveCriarPedidoComSucesso() {
        Produto produto = new Produto("Mouse", 49.90, 10);
        when(produtoRepository.buscarPorId(1L)).thenReturn(produto);

        Pedido pedido = service.criarPedido(1L, 2);

        assertEquals(99.80, pedido.getValorTotal(), 0.001);
        verify(produtoRepository, times(1)).buscarPorId(1L);
        verify(pedidoRepository, times(1)).salvar(any(Pedido.class));
    }

    // Cobre os cenários PED03 e PED08
    @Test
    void naoDeveCriarPedidoComQuantidadeZero() {
        assertThrows(IllegalArgumentException.class,
                () -> service.criarPedido(1L, 0));

        // PED08: Garante que não salvou
        verify(pedidoRepository, never()).salvar(any());
    }

    // Cobre os cenários PED04 e PED08
    @Test
    void naoDeveCriarPedidoComQuantidadeNegativa() {
        assertThrows(IllegalArgumentException.class,
                () -> service.criarPedido(1L, -3));

        // PED08: Garante que não salvou
        verify(pedidoRepository, never()).salvar(any());
    }

    // Cobre os cenários PED05 e PED08
    @Test
    void deveLancarExcecaoQuandoProdutoInexistente() {
        when(produtoRepository.buscarPorId(99L)).thenReturn(null);

        assertThrows(ProdutoNaoEncontradoException.class,
                () -> service.criarPedido(99L, 2));

        // PED08: Garante que não salvou
        verify(pedidoRepository, never()).salvar(any());
    }

    // Cobre os cenários PED06 e PED08
    @Test
    void deveLancarExcecaoQuandoQuantidadeMaiorQueEstoque() {
        Produto produto = new Produto("Mouse", 49.90, 10);
        when(produtoRepository.buscarPorId(1L)).thenReturn(produto);

        assertThrows(EstoqueInsuficienteException.class,
                () -> service.criarPedido(1L, 11));

        // PED08: Garante que não salvou
        verify(pedidoRepository, never()).salvar(any());
    }

    // Cobre o cenário PED07
    @Test
    void deveAceitarPedidoComQuantidadeIgualAoEstoque() {
        // Valor de fronteira
        Produto produto = new Produto("Mouse", 49.90, 10);
        when(produtoRepository.buscarPorId(1L)).thenReturn(produto);

        Pedido pedido = service.criarPedido(1L, 10);

        assertEquals(499.00, pedido.getValorTotal(), 0.001);
        verify(produtoRepository, times(1)).buscarPorId(1L);
        verify(pedidoRepository, times(1)).salvar(any(Pedido.class));
    }
}