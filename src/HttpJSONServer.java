import java.io.*;
import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;

//Classe de Servidor JSON (Server Class)
public class HttpJSONServer {
	
	//Método Main
	//Determina o ponto de início de execução de qualquer aplicação Java. Toda classe pode ter um método main.
	public static void main(String[] args) throws Exception {

		//Etapa - Configuração da conexão - Define a porta(port) onde o servidor vai ficar esperando/escuntando requisições vindas dos clientes (listening)
        //Cria uma instância HttpServer que será vinculada ao InetSocketAddress especificado
		HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0); 
		
		//Etapa - Configuração da conexão
        //Cria o contexto Http
        //Um HttpContext representa um mapeamento de um caminho(/json) de URI para um manipulador(HandlerJSON) neste HttpServer
        //Indica um recurso no servidor - nesse caso um recurso dinâmico que é um JSON
        server.createContext("/json", new HandlerJSON()); 
		
		//Etapa - Configuração da conexão
		//Define o objeto Executor deste servidor, no caso define um executor nulo
        server.setExecutor(null);
		
		//Exibi mensagem para indicar o start do servidor
        System.out.println("Iniciando um servidor HTTP na porta: 8001"); //Imprime mensagem indicando start do servidor 

		//Iniciar o servidor
		server.start();
	}
	
	//Etapas - Recebe e Processa a Requisição
	//Manipulador (Handler) de Requisições (Requests)
	static class HandlerJSON implements HttpHandler {
		
		//Etapas - Recebe e Processa a Requisição
		//Método de Manipulação (handle method)
		public void handle(HttpExchange t) throws IOException {
            
            //Imprime mensagem indicando qual recurso foi requisitado
			System.out.println("Chamou o recurso /json na porta 8001"); 

			//Etapa - Acessa/criar o recurso especificado na requisição
			//Construtor de String - Para facilitar a criação do JSON
            //Recurso Dinâmico
            StringBuilder response = new StringBuilder();
			
			//Etapa - Acessa/criar o recurso especificado na requisição
            //Acrescenta aos response os valores passados por parâmetro - Cria a String com o JSON
            //Caracteres de Escape - https://codegym.cc/groups/posts/escaping-characters-java
            response.append("{\"responseTexte\":\"Hello World\"}");

			//Etapa - Constrói a Resposta	
			//Define alguns "cabeçalhos" da resposta (response) Http - No caso, define o tipo do conteúdo (MIME Type)
			t.getResponseHeaders().set(
				"Content-Type",
				"application/json"
			);
			
			//Etapa - Constrói a Resposta
			//Indica que a resposta vai ter o código (status code) 200 do HTTP e o tamanho da resposta
			t.sendResponseHeaders(200, response.length());

			//Etapa - Envia Resposta	
			//Escreve a resposta JSON no OutPutStream
			OutputStream os = t.getResponseBody();
			os.write(response.toString().getBytes());
			os.close();
		}
	}

}
