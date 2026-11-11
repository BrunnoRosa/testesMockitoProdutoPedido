package sistemaProdutosMockito.globalException;

public class ErroAoSalvarProdutoException extends RuntimeException {
    public ErroAoSalvarProdutoException(String message) {
        super(message);
    }
}
