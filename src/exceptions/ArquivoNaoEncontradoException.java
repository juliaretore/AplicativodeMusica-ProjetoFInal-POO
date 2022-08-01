package exceptions;

public class ArquivoNaoEncontradoException extends Exception{
	public ArquivoNaoEncontradoException() {
		super("Arquivo não encontrado.");
	}
}
