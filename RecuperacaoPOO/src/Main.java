
import java.sql.*;

public class Main {
    static final String DB_URL = "jdbc:sqlite:mercado.db";

    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection(DB_URL);

            // Criar tabela "produtos" se não existir
            criarTabelaProdutos(conn);

            // Inserir um novo produto
            inserirProduto(conn, new Produto("Arroz", "Alimentos", 5.99, 10));

            // Atualizar o preço de um produto
            atualizarPrecoProduto(conn, "Arroz", 6.99);

            // Excluir um produto
            excluirProduto(conn, "Arroz");

            // Consultar os produtos disponíveis
            consultarProdutos(conn);

        } catch (SQLException se) {
            // Tratamento de erros do JDBC
            se.printStackTrace();
        } finally {
            // Fechando os recursos em um bloco finally
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                // Nada a fazer
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("Fim do programa");
    }

    // Método para criar a tabela "produtos" se não existir
    private static void criarTabelaProdutos(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS produtos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome TEXT," +
                    "categoria TEXT," +
                    "preco REAL," +
                    "quantidade INTEGER)";
            stmt.executeUpdate(sql);
        }
    }

    // Método para inserir um novo produto na tabela
    private static void inserirProduto(Connection conn, Produto produto) throws SQLException {
        String sql = "INSERT INTO produtos (nome, categoria, preco, quantidade) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getCategoria());
            stmt.setDouble(3, produto.getPreco());
            stmt.setInt(4, produto.getQuantidade());
            stmt.executeUpdate();
        }
    }

    // Método para atualizar o preço de um produto na tabela
    private static void atualizarPrecoProduto(Connection conn, String nomeProduto, double novoPreco) throws SQLException {
        String sql = "UPDATE produtos SET preco=? WHERE nome=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, novoPreco);
            stmt.setString(2, nomeProduto);
            stmt.executeUpdate();
        }
    }

    // Método para excluir um produto da tabela
    private static void excluirProduto(Connection conn, String nomeProduto) throws SQLException {
        String sql = "DELETE FROM produtos WHERE nome=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nomeProduto);
            stmt.executeUpdate();
        }
    }

    // Método para consultar os produtos na tabela e imprimir os registros
    private static void consultarProdutos(Connection conn) throws SQLException {
        String sql = "SELECT * FROM produtos";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String categoria = rs.getString("categoria");
                double preco = rs.getDouble("preco");
                int quantidade = rs.getInt("quantidade");

                System.out.println("ID: " + id);
                System.out.println("Nome: " + nome);
                System.out.println("Categoria: " + categoria);
                System.out.println("Preço: " + preco);
                System.out.println("Quantidade: " + quantidade);
                System.out.println();
            }
        }
    }
}

// Classe Produto
class Produto {
    private String nome;
    private String categoria;
    private double preco;
    private int quantidade;

    public Produto(String nome, String categoria, double preco, int quantidade) {
        this.nome = nome;
        this.categoria = categoria;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade;
    }
}