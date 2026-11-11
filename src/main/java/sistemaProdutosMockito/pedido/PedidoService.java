package sistemaProdutosMockito.pedido;

import sistemaProdutosMockito.globalException.EstoqueInsuficienteException;
import sistemaProdutosMockito.globalException.ProdutoNaoEncontradoException;
import sistemaProdutosMockito.produto.Produto;
import sistemaProdutosMockito.produto.ProdutoRepository;

public class PedidoService {

    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    public PedidoService(ProdutoRepository produtoRepository,
                         PedidoRepository pedidoRepository) {
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public Pedido criarPedido(Long produtoId, int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                    "Quantidade deve ser maior que zero");
        }
        Produto produto = produtoRepository.buscarPorId(produtoId);
        if (produto == null) {
            throw new ProdutoNaoEncontradoException(
                    "Produto não encontrado: " + produtoId);
        }
        if (quantidade > produto.getQuantidade()) {
            throw new EstoqueInsuficienteException("Estoque insuficiente");
        }
        double total = produto.getPreco() * quantidade;
        Pedido pedido = new Pedido(produto, quantidade, total);
        pedidoRepository.salvar(pedido);
        return pedido;
    }
}

