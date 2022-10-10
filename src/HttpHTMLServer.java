import java.io.*;
import com.sun.net.httpserver.*;
import java.net.InetSocketAddress;

//Classe de Servidor HTML (Server Class)
public class HttpHTMLServer {
	
	//Método Main
	//Determina o ponto de início de execução de qualquer aplicação Java. Toda classe pode ter um método main.
	public static void main(String[] args) throws Exception {

		//Etapa - Configuração da conexão - Define a porta(port) onde o servidor vai ficar esperando/escuntando requisições vindas dos clientes (listening)
        //Cria uma instância HttpServer que será vinculada ao InetSocketAddress especificado
		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0); 
		
		//Etapa - Configuração da conexão
        //Cria um contexto Http
        //Um HttpContext representa um mapeamento de um caminho(/) de URI para um manipulador(HandlerHTML) neste HttpServer
        //Indica um recurso no servidor - nesse caso um recurso dinâmico que é um HTML
        server.createContext("/", new HandlerHTML()); 

        //Etapa - Configuração da conexão
        //Cria um contexto Http
        //Indica um recurso no servidor - nesse caso um recurso dinâmico que é um HTML
        //Esse HTML vai receber um parâmetro através de Query String que vai ser incorporado ao HTML
        server.createContext("/teste", new HandlerHTMLQueryString()); 
		
		//Etapa - Configuração da conexão
		//Define o objeto Executor deste servidor, no caso define um executor nulo
        server.setExecutor(null);
		
		//Exibi mensagem para indicar o start do servidor
        System.out.println("Iniciando um servidor HTTP na porta: 8000"); //Imprime mensagem indicando start do servidor 

		//Iniciar o servidor
		server.start();
	}
	
    //Etapas - Recebe e Processa a Requisição
	//Manipulador (Handler) de Requisições (Requests)
	static class HandlerHTML implements HttpHandler {
		
		//Etapas - Recebe e Processa a Requisição
		//Método de Manipulação (handle method)
		public void handle(HttpExchange t) throws IOException {
            
            //Imprime mensagem indicando qual recurso foi requisitado
			System.out.println("Chamou o recurso / na porta 8000"); 

			//Etapa - Acessa/criar o recurso especificado na requisição
			//Construtor de String - Para facilitar a criação do HTML
            //Recurso Dinâmico
            StringBuilder response = new StringBuilder();

            //Etapa - Acessa/criar o recurso especificado na requisição
            //Acrescenta aos response os valores passados por parâmetro - Cria a String com o HTML    
			response.append("<!DOCTYPE html>")
            .append("<html>")
                .append("<head>")
                    .append("<title>")
                        .append("Primeiro Servidor")
                    .append("</title>")
                    .append("</head>")
                    .append("<body>")
                        .append("<h1>")
                            .append("2º Servidor")
                        .append("</h1>")
                    .append("</body>")
            .append("</html>");

			//Etapa - Constrói a Resposta	
			//Define alguns "cabeçalhos" da resposta (response) Http - No caso, define o tipo do conteúdo (MIME Type)
			t.getResponseHeaders().set(
				"Content-Type",
				"text/html"
			);
			
			//Etapa - Constrói a Resposta
			//Indica que a resposta vai ter o código (status code) 200 do HTTP e o tamanho da resposta
			t.sendResponseHeaders(201, response.length());

			//Etapa - Envia Resposta	
			//Escreve a resposta HTML no OutPutStream
			OutputStream os = t.getResponseBody();
			os.write(response.toString().getBytes());
			os.close();
		}
	}

    //Etapas - Recebe e Processa a Requisição
	//Manipulador (Handler) de Requisições (Requests)
	static class HandlerHTMLQueryString implements HttpHandler {
		
		//Etapas - Recebe e Processa a Requisição
		//Método de Manipulação (handle method)
		public void handle(HttpExchange t) throws IOException {
            
            //Imprime mensagem indicando qual recurso foi requisitado
			System.out.println("Chamou o recurso /teste na porta 8000"); 

            //Define uma variável que vai receber o valor passado por parâmetro na Query String da URI da requisição
            String valorParametroQueryString = ""; 
			
            //Etapa - Processa a Requisição
            try {
                //Extrai o parametro e seu valor da URI enviada na requisição 
                if("GET".equals(t.getRequestMethod())) { 
                    valorParametroQueryString = t.getRequestURI().toString().split("\\?")[1].split("=")[1];
                } 
            } catch (Exception ex) {
                valorParametroQueryString = "uma execção ao não mandar o nome na Query String";
            }

			//Etapa - Acessa/criar o recurso especificado na requisição
			//Construtor de String - Para facilitar a criação do HTML
            //Recurso Dinâmico
            StringBuilder response = new StringBuilder();

            //Etapa - Acessa/criar o recurso especificado na requisição
            //Acrescenta aos response os valores passados por parâmetro - Cria a String com o HTML    
			response.append("<!DOCTYPE html>")
            .append("<html>")
                .append("<head>")
                    .append("<title>")
                        .append("Primeiro Servidor")
                    .append("</title>")
                    .append("</head>")
                    .append("<body>")
                        .append("<h1>")
                            .append("Olá, eu sou ")
                            .append(valorParametroQueryString)
                        .append("</h1>")
                    .append("</body>")
            .append("</html>");

			//Etapa - Constrói a Resposta	
			//Define alguns "cabeçalhos" da resposta (response) Http - No caso, define o tipo do conteúdo (MIME Type)
			t.getResponseHeaders().set(
				"Content-Type",
				"text/html"
			);
			
			//Etapa - Constrói a Resposta
			//Indica que a resposta vai ter o código (status code) 200 do HTTP e o tamanho da resposta
			t.sendResponseHeaders(200, response.length());

			//Etapa - Envia Resposta	
			//Escreve a resposta HTML no OutPutStream
			OutputStream os = t.getResponseBody();
			os.write(response.toString().getBytes());
			os.close();
		}
	}

}
