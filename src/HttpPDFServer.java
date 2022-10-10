import java.io.*;
import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;

//Classe de Servidor PDF (Server Class)
public class HttpPDFServer {
	
	//Método Main
	//Determina o ponto de início de execução de qualquer aplicação Java. Toda classe pode ter um método main.
	public static void main(String[] args) throws Exception {

		//Etapa - Configuração da conexão - Define a porta(port) onde o servidor vai ficar esperando/escuntando requisições vindas dos clientes (listening)
        //Cria uma instância HttpServer que será vinculada ao InetSocketAddress especificado
		HttpServer server = HttpServer.create(new InetSocketAddress(8002), 0); 
		
		//Etapa - Configuração da conexão
        //Cria o contexto Http
        //Um HttpContext representa um mapeamento de um caminho(/pdf) de URI para um manipulador(handlerPDF) neste HttpServer
        //Indica um recurso no servidor - nesse caso um recurso estático que é um arquivo PDF a ser retornado
        server.createContext("/pdf", new HandlerPDF()); 
		
		//Etapa - Configuração da conexão
		//Define o objeto Executor deste servidor, no caso define um executor nulo
        server.setExecutor(null);
		
		//Exibi mensagem para indicar o start do servidor
        System.out.println("Iniciando um servidor HTTP na porta: 8002"); //Imprime mensagem indicando start do servidor 

		//Iniciar o servidor
		server.start();
	}
	
	//Etapas - Recebe e Processa a Requisição
	//Manipulador (Handler) de Requisições (Requests)
	static class HandlerPDF implements HttpHandler {
		
		//Etapas - Recebe e Processa a Requisição
		//Método de Manipulação (handle method)
		public void handle(HttpExchange t) throws IOException {
            
            //Imprime mensagem indicando qual recurso foi requisitado
			System.out.println("Chamou o recurso /pdf na porta 8002"); 

			//Etapa - Acessar/criar o recurso especificado na requisição
            //Arquivo PDF já existente no sistema de diretórios do servidor web. Recurso estático.
            //Caso o arquivo não exista a resposta do servidor vai ser um erro de resposta vazia (err_empty_response)
			File file = new File ("c:/temp/doc.pdf"); 
			byte [] bytearray  = new byte [(int)file.length()];
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			bis.read(bytearray, 0, bytearray.length);

			//Etapa - Constrói a Resposta	
			//Define alguns "cabeçalhos" da resposta (response) Http - No caso, define o tipo do conteúdo (MIME Type)
			t.getResponseHeaders().set(
				"Content-Type",
				"application/pdf"
			);
			
			//Etapa - Constrói a Resposta
			//Indica que a resposta vai ter o código (status code) 200 do HTTP e o tamanho da resposta
			t.sendResponseHeaders(200, file.length());

			//Etapa - Envia Resposta	
			//Escreve a resposta PDF no OutPutStream
			OutputStream os = t.getResponseBody();
			os.write(bytearray,0,bytearray.length); 
			os.close();
		}
	}

}
