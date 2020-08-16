import java.awt.Container;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class main {

	public static void main(String[] args) throws IOException, InterruptedException, SQLException {

		Connection connection = null;          //atributo do tipo Connection
	try {

	// Carregando o JDBC Driver padrão

	String driverName = "com.mysql.jdbc.Driver";

	Class.forName(driverName);

	// Configurando a nossa conexão com um banco de dados//

	        String serverName = "localhost";    //caminho do servidor do BD

	        String mydatabase = "Fras-le";        //nome do seu banco de dados

	        String url = "jdbc:mysql://" + serverName + "/" + mydatabase;

	        String username = "root";        //nome de um usuário de seu BD

	        String password = "123456";      //sua senha de acesso

	        connection = DriverManager.getConnection(url, username, password);



	        //Testa sua conexão//
	        String status = "Não conectou...";
	        
	        if (connection != null) {

	            status = ("STATUS--->Conectado com sucesso!");

	        } else {

	            status = ("STATUS--->Não foi possivel realizar conexão");

	        }

	        } catch (ClassNotFoundException e) {  //Driver não encontrado



	            System.out.println("O driver expecificado nao foi encontrado.");

	        } catch (SQLException e) {

	        		//Não conseguindo se conectar ao banco

	            System.out.println("Nao foi possivel conectar ao Banco de Dados.");

	        }
		for ( ; ; ) { //um for infinito
			atualizarCargas(connection); // puxa atualizar cargas que por sua vez puxa obterDdados que tem a SQL stetement
			Thread.sleep(15000); // para por 15 segundos
		}
	} // fecha metodo principal
	
	public static void atualizarCargas(Connection conn) throws IOException, InterruptedException, SQLException {
		Connection con = conn; // passa para um novo parametro
			Path path = Paths.get("/Users/julianosoder/Desktop/Fras-le-offline/dados.txt"); // aqui vai o caminho onde fica o arquivo txt
		        
			 List<String> linhas = Files.readAllLines(path);

		        for(int i =0;i<linhas.size();i++) { // vai passar todas as linhas do arquivo
		        	if(linhas.get(i).contains("var cT")) { 
		        		String[] string = linhas.get(i).split("=");
		        		linhas.remove(i);
		        		String[] string2 = string[1].split(";"); 
		        		String cargas = obterDados(con).get(i)[4]; // executa a busca no banco e retorna o array dentro do arraylist na posicao i e 4 (cargas turno)
		        		linhas.add(i, string[0]+"="+cargas+";");  
		        	}
		        	System.out.println(linhas.get(i));
		        	Files.write(path, linhas);
		        }
		       //Thread.sleep(11000);
	}
	
	public static ArrayList<String[]> obterDados(Connection conn) throws SQLException {
		 // procedimentos para obter os dados de uma tabela
		  Statement stmt = conn.createStatement();
		  String query = "SELECT * FROM Prensas_siblo_SGBD"; // aqui e so editar com o nome da tabela do banco da fras-le siblo prensas
		  ResultSet rs = stmt.executeQuery(query);
		  ArrayList<String[]> array = new ArrayList();
		  while(rs.next()){ 
			  String[] object = null;
			  // alterar o nome das colunas das tabelas
			 String maquina = rs.getString("numero_maquina");
		    String op = rs.getString("numro_op_telha");
		    String cargasRealizadas = rs.getString("cargas_reaizadas_op");
		    String cargasTurno = rs.getString("cargas_realizadas_turno");
		    String material = rs.getString("material");
		    String peso = rs.getString("peso_telha");
		    String quantidadeTotal = rs.getString("quantidade_orden");
		    object[0] = maquina;
		    object[1] = op;
		    object[2] = cargasRealizadas;
		    object[3] = cargasTurno;
		    object[4] = material;
		    object[5] = peso;
		    object[6] = quantidadeTotal;
		    array.add(object);
		  }
		return array;
		  // fim procedimento para obter os dados
		
	}
} // fecha funcao
